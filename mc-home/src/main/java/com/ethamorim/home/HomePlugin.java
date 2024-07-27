package com.ethamorim.home;

import com.ethamorim.home.persistence.HibernateConnection;
import org.bukkit.plugin.java.JavaPlugin;

public class HomePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        HibernateConnection.connect();
    }
}
