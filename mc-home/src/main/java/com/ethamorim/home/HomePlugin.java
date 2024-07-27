package com.ethamorim.home;

import com.ethamorim.home.persistence.HibernateConnection;
import org.bukkit.plugin.java.JavaPlugin;

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
    }
}
