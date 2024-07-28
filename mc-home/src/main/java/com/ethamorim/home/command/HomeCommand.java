package com.ethamorim.home.command;

import static com.ethamorim.home.persistence.HibernateConnection.sessionFactory;

import com.ethamorim.home.persistence.model.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hibernate.HibernateException;

import java.util.Optional;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String s,
            String[] args
    ) {
        if (sender instanceof Player playerSender) {
            try {
                if (args.length == 0) throw new CommandException(
                        "Invalid command syntax: /home <name>");

                if (args[0].equals("set")) {
                    if (args.length != 2) throw new CommandException(
                            "Invalid command syntax: /home set <name>");
                    return setNewHome(playerSender, args[1]);
                } else {
                    var name = args[0];
                    var homeEntity = sessionFactory.fromSession(session -> {
                        var playerEntity = session.bySimpleNaturalId(PlayerEntity.class)
                                .load(playerSender.getName());

                        var home = session.byNaturalId(HomeEntity.class)
                                .using(HomeEntity_.NAME, name)
                                .using(HomeEntity_.PLAYER, playerEntity)
                                .load();
                        return home != null ? Optional.of(home) : Optional.empty();
                    });
                    if (homeEntity.isEmpty())
                        throw new CommandException("No home found with name " + name);
                }
                return true;
            } catch (CommandException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
        }
        return false;
    }

    private boolean setNewHome(Player playerSender, String name) {
        try {
            sessionFactory.inTransaction(session -> {
                var playerRecord = session
                        .bySimpleNaturalId(PlayerEntity.class)
                        .load(playerSender.getName());
                var registeredHome = session.byNaturalId(HomeEntity.class)
                        .using(HomeEntity_.NAME, name)
                        .using(HomeEntity_.PLAYER, playerRecord)
                        .load();
                if (registeredHome != null)
                    throw new HibernateException("Home with name '" + name + "' already registered");

                var newHome = new HomeEntity();
                newHome.setName(name);
                newHome.setPlayer(playerRecord);

                var location = playerSender.getLocation();
                newHome.setLocation(new HomeLocation(
                        location.getX(),
                        location.getY(),
                        location.getZ()
                ));

                session.persist(newHome);
                session.flush();
            });
            playerSender.sendMessage(ChatColor.GREEN + "New home created with name " + name);
            return true;
        } catch (HibernateException e) {
            throw new CommandException("Something went wrong: " + e.getMessage());
        }
    }
}
