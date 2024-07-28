package com.ethamorim.home.command;

import static com.ethamorim.home.persistence.HibernateConnection.sessionFactory;

import com.ethamorim.home.persistence.PluginQueries;
import com.ethamorim.home.persistence.model.*;
import jakarta.persistence.EntityExistsException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.NoSuchElementException;

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
                if (args.length == 0) throw new CommandException("Invalid command syntax: /home <name>");

                if (args[0].equals("set")) {
                    if (args.length != 2) throw new CommandException("Invalid command syntax: /home set <name>");
                    return setNewHome(playerSender, args[1]);
                } else {
                    return accessHome(playerSender, args[0]);
                }
            } catch (CommandException e) {
                sender.sendMessage(ChatColor.RED + e.getMessage());
            }
        }
        return false;
    }

    private boolean setNewHome(Player playerSender, String name) {
        try {
            sessionFactory.inTransaction(session -> {
                var playerRecord = PluginQueries
                        .getPlayerByNaturalId(session, playerSender.getName())
                        .orElseThrow();

                var registeredHome = PluginQueries
                        .getHomeByNaturalId(session, name, playerRecord);
                if (registeredHome.isPresent())
                    throw new EntityExistsException("Home with name '" + name + "' already registered");

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
        } catch (EntityExistsException e) {
            throw new CommandException("Something went wrong: " + e.getMessage());
        } catch (NoSuchElementException e) {
            throw new CommandException("Operation failed while trying to register a home");
        }
    }

    private boolean accessHome(Player playerSender, String name) {
        try {
            var homeEntity = sessionFactory.fromSession(session -> {
                var playerEntity = PluginQueries
                        .getPlayerByNaturalId(session, playerSender.getName())
                        .orElseThrow();
                return PluginQueries.getHomeByNaturalId(session, name, playerEntity);
            });
            if (homeEntity.isEmpty())
                throw new CommandException("No home found with name " + name);
            var savedLocation = homeEntity.get().getLocation();
            playerSender.teleport(new Location(
                    playerSender.getWorld(),
                    savedLocation.x(),
                    savedLocation.y(),
                    savedLocation.z()
            ));
            return true;
        } catch (NoSuchElementException e) {
            throw new CommandException("Operation failed while trying to access home");
        }
    }
}
