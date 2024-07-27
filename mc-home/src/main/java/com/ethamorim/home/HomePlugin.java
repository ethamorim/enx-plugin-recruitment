package com.ethamorim.home;

import org.bukkit.plugin.java.JavaPlugin;
import static org.hibernate.cfg.AvailableSettings.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

public class HomePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        var configuration = new Configuration()
                .setProperty(JAKARTA_JDBC_DRIVER, "org.mariadb.jdbc.Driver")
                .setProperty(JAKARTA_HBM2DDL_DATABASE_ACTION, Action.CREATE)
                .setProperty("hibernate.agroal.maxSize", 5);
        try (var sessionFactory = configuration.buildSessionFactory()) {
            sessionFactory.getSchemaManager().exportMappedObjects(true);
        }
    }
}
