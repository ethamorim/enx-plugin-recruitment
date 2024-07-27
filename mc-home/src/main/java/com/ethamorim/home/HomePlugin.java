package com.ethamorim.home;

import org.bukkit.plugin.java.JavaPlugin;
import static org.hibernate.cfg.AvailableSettings.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

public class HomePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Hellooooo from Homie Plugin!");
        var configuration = new Configuration()
                .setProperty(JAKARTA_JDBC_URL, "jdbc:mariadb://localhost:3306/home")
                .setProperty(JAKARTA_JDBC_USER, "root")
                .setProperty(JAKARTA_JDBC_PASSWORD, "")
                .setProperty(JAKARTA_HBM2DDL_DATABASE_ACTION, Action.CREATE)
                .setProperty(SHOW_SQL, true)
                .setProperty(FORMAT_SQL, true)
                .setProperty(HIGHLIGHT_SQL, true)
                .setProperty("hibernate.agroal.maxSize", 5);
        try (var sessionFactory = configuration.buildSessionFactory()) {
            sessionFactory.getSchemaManager().exportMappedObjects(true);
        }
    }
}
