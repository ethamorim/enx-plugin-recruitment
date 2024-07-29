package com.ethamorim.home.command;

import static com.ethamorim.home.persistence.HibernateConnection.sessionFactory;

import com.ethamorim.home.HomePlugin;
import com.ethamorim.home.persistence.PluginQueries;
import com.ethamorim.home.persistence.model.*;
import com.ethamorim.home.util.CooldownChecker;
import jakarta.persistence.EntityExistsException;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.NoSuchElementException;

/**
 * Classe responsável por implementar o comando /home.
 *
 * @author ethamorim
 */
public class HomeCommand implements CommandExecutor {

    /**
     * Referência à classe principal do plugin para
     * acessar propriedades específicas.
     */
    private final HomePlugin main;

    /**
     * Recebe a classe principal do plugin.
     * @param main classe principal
     */
    public HomeCommand(HomePlugin main) {
        this.main = main;
    }

    /**
     * Método chamado quando o comando /home é executado.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param s Alias of the command which was used
     * @param args Passed command arguments
     */
    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String s,
            String[] args
    ) {
        if (sender instanceof Player playerSender) {
            try {
                if (args.length == 0) throw new CommandException("Invalid command syntax: /home <name>");

                /*
                    Resgata o registro do jogador no banco de dados.
                    É esperado que esse registro sempre exista,
                    visto que o jogador é inserido no banco de dados na primeira
                    vez em que entra o servidor.
                    Sem isso, nenhuma operação nesse método é viável.
                 */
                var playerEntity = sessionFactory.fromSession(session ->
                        PluginQueries.getPlayerByNaturalId(session, playerSender.getName()))
                        .orElseThrow();

                var operation = args[0];
                if (operation.equals("set")) {
                    if (args.length != 2)
                        throw new CommandException("Invalid command syntax: /home set <name>");

                    var name = args[1];
                    setNewHome(playerSender.getLocation(), playerEntity, name);
                    playerSender.sendMessage(ChatColor.GREEN + "New home created with name " + name);
                } else if (operation.equals("cooldown")) {
                    if (args.length != 2)
                        throw new CommandException("Invalid command syntax: /home cooldown <seconds>");

                    var seconds = args[1];
                    setCooldown(playerEntity, seconds);
                    playerSender.sendMessage(ChatColor.GREEN + "Cooldown set to " + seconds + " seconds");
                } else if (operation.equals("particles")) {
                    if (args.length != 2)
                        throw new CommandException("Invalid command syntax: /home particles [true|false]");

                    var status = args[1];
                    var message = setParticlesStatus(playerEntity, status);
                    playerSender.sendMessage(ChatColor.GREEN + message);
                } else {
                    /*
                        A condição abaixo evita que o método seja interrompido,
                        caso o atributo `lastIssued` seja `null`.
                        Pois mesmo que `lastIssued` não esteja definido, será
                        definido a seguir.
                     */
                    if (playerEntity.getLastIssued() != null) {
                        var canIssue = CooldownChecker.canIssueCommandFromNow(
                                playerEntity.getLastIssued(),
                                playerEntity.getCooldown()
                        );
                        if (!canIssue) throw new CommandException("Wait for cooldown!");
                    }
                    accessHome(playerSender, playerEntity, args[0]);
                }
                return true;
            } catch (CommandException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            } catch (NoSuchElementException e) {
                sender.sendMessage(ChatColor.RED + "Invalid sender.");
            }
        }
        return false;
    }

    /**
     * Responsável por registrar uma nova home.
     */
    private void setNewHome(
            Location senderLocation,
            PlayerEntity playerRecord,
            String name
    ) {
        try {
            sessionFactory.inTransaction(session -> {
                var registeredHome = PluginQueries
                        .getHomeByNaturalId(session, name, playerRecord);
                if (registeredHome.isPresent())
                    throw new EntityExistsException("Home with name '" + name + "' already registered");

                var newHome = new HomeEntity();
                newHome.setName(name);
                newHome.setPlayer(playerRecord);

                newHome.setLocation(new HomeLocation(
                        senderLocation.getX(),
                        senderLocation.getY(),
                        senderLocation.getZ()
                ));

                session.persist(newHome);
                session.flush();
            });
        } catch (EntityExistsException e) {
            throw new CommandException("Something went wrong: " + e.getMessage());
        }
    }

    /**
     * Define um novo valor em segundos para o cooldown do jogador,
     * que por sua vez é salva em milisegundos.
     */
    private void setCooldown(PlayerEntity playerEntity, String secondsArg) {
        try {
            var seconds = Integer.parseInt(secondsArg);
            if (seconds < 2 || seconds > 30) {
                throw new CommandException("Cooldown should be greater than 2 and less than 30");
            }
            var milli = seconds * 1000;
            sessionFactory.inTransaction(session -> {
                playerEntity.setCooldown(milli);
                session.merge(playerEntity);
                session.flush();
            });
        } catch (NumberFormatException e) {
            throw new CommandException("Invalid argument for given operation");
        }
    }

    /**
     * Ativa ou desativa as partículas de teletransporte.
     */
    private String setParticlesStatus(PlayerEntity playerEntity, String statusArg) {
        if (statusArg.equalsIgnoreCase("true") || statusArg.equalsIgnoreCase("false")) {
            var status = Boolean.parseBoolean(statusArg);
            sessionFactory.inTransaction(session -> {
                playerEntity.setParticlesActive(status);
                session.merge(playerEntity);
                session.flush();
            });
            return status
                    ? "Particles are now activated"
                    : "Particles are now deactivated";
        } else {
            throw new CommandException("Invalid argument for given operation");
        }
    }

    /**
     * Responsável por teleportar o jogador à home desejada.
     */
    private void accessHome(
            Player playerSender,
            PlayerEntity playerEntity,
            String name
    ) {
        try {
            var homeEntity = sessionFactory.fromSession(session ->
                    PluginQueries.getHomeByNaturalId(session, name, playerEntity));
            if (homeEntity.isEmpty())
                throw new CommandException("No home found with name " + name);

            sessionFactory.inTransaction(session -> {
                playerEntity.setLastIssued(Instant.now());
                session.merge(playerEntity);
                session.flush();
            });

            var world = playerSender.getWorld();
            var playerCurrentLocation = playerSender.getLocation();
            if (playerEntity.isParticlesActive()) {
                world.spawnParticle(
                        Particle.PORTAL,
                        playerCurrentLocation,
                        30);
            }

            /*
                A tarefa registrada teleporta o jogador apenas
                após 20 ticks (1s).
             */
            Bukkit.getScheduler().runTaskLater(main, () -> {
                world.playSound(
                        playerCurrentLocation,
                        Sound.ENTITY_ENDERMAN_TELEPORT,
                        5,
                        1
                );

                var savedLocation = homeEntity.get().getLocation();
                playerSender.teleport(new Location(
                        world,
                        savedLocation.x(),
                        savedLocation.y(),
                        savedLocation.z())
                );
            }, 20);
        } catch (NoSuchElementException e) {
            throw new CommandException("Something went wrong: Operation failed while trying to access home");
        }
    }
}
