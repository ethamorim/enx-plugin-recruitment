package com.ethamorim.home.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_DRIVER;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION;

public final class HibernateConnection {
    private HibernateConnection() {}

    public static SessionFactory sessionFactory;

    public static void connect() {
        var configuration = new Configuration()
                .setProperty(JAKARTA_JDBC_DRIVER, "org.mariadb.jdbc.Driver")
                .setProperty(JAKARTA_HBM2DDL_DATABASE_ACTION, Action.CREATE)
                .setProperty("hibernate.agroal.maxSize", 5);
        sessionFactory = configuration.buildSessionFactory();
        sessionFactory.getSchemaManager().exportMappedObjects(true);
    }
}
