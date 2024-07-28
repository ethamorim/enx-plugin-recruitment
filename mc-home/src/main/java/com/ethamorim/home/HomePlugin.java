package com.ethamorim.home;

import com.ethamorim.home.command.HomeCommand;
import com.ethamorim.home.event.HomeEvents;
import com.ethamorim.home.persistence.HibernateConnection;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * Classe principal do plugin.
 *
 * @author ethamorim
 */
public class HomePlugin extends JavaPlugin {

    /**
     * Método executado quando plugin é ativado.
     * Inicia as rotinas necessárias.
     */
    @Override
    public void onEnable() {
        HibernateConnection.connect();

        Bukkit.getPluginManager()
                .registerEvents(new HomeEvents(), this);

        var homeCommand = getCommand("home");
        if (homeCommand != null) homeCommand.setExecutor(new HomeCommand(this));
    }
}
