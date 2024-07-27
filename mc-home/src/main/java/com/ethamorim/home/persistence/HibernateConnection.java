package com.ethamorim.home.persistence;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

import static org.hibernate.cfg.JdbcSettings.JAKARTA_JDBC_DRIVER;
import static org.hibernate.cfg.SchemaToolingSettings.JAKARTA_HBM2DDL_DATABASE_ACTION;

/**
 * Classe Singleton para usar a conexão do Hibernate com MariaDB.
 *
 * @author ethamorim
 */
public final class HibernateConnection {
    /**
     * Impede a instanciação da classe.
     */
    private HibernateConnection() {}

    /**
     * Factory disponível para executar as lógicas de persistência.
     */
    public static SessionFactory sessionFactory;

    /**
     * Inicia uma conexão do banco de dados com o Hibernate.
     * Deve ser uma das primeiras rotinas a ser executada
     * para que sessionFactory se torne disponível.
     */
    public static void connect() {
        var configuration = new Configuration()
                .setProperty(JAKARTA_JDBC_DRIVER, "org.mariadb.jdbc.Driver")
                .setProperty(JAKARTA_HBM2DDL_DATABASE_ACTION, Action.CREATE)
                .setProperty("hibernate.agroal.maxSize", 5);
        sessionFactory = configuration.buildSessionFactory();
        sessionFactory.getSchemaManager().exportMappedObjects(true);
    }
}
