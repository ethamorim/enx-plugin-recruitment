package com.ethamorim.home.persistence.model;

import static org.hibernate.cfg.AvailableSettings.*;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * Testa a persistência das entidades de tabelas e suas associações
 *
 * @author ethamorim
 */
public class PersistenceTest {

    static SessionFactory sessionFactory;

    @BeforeAll
    static void bootstrapHibernate() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Player.class)
                .setProperty(JAKARTA_JDBC_URL, "jdbc:h2:mem:db1")
                .setProperty(SHOW_SQL, true)
                .setProperty(FORMAT_SQL, true)
                .setProperty(HIGHLIGHT_SQL, true)
                .setProperty("hibernate.agroal.maxSize", 5)
                .buildSessionFactory();
        sessionFactory.getSchemaManager().exportMappedObjects(true);
    }

    /*
        Testa a persistência de Player
        e verifica por valores padrões não definidos na criação.
     */
    @Test
    void shouldPersistPlayer() {
        sessionFactory.inTransaction(session -> {
            var player = new Player();
            player.setUuid(UUID.randomUUID());
            player.setNickname("enx");

            session.persist(player);
        });

        sessionFactory.inSession(session -> {
            var builder = sessionFactory.getCriteriaBuilder();
            var query = builder.createQuery(Player.class);
            var playerRoot = query.from(Player.class);
            var where = builder.equal(playerRoot.get(Player_.NICKNAME), "enx");
            query.select(playerRoot).where(where);
            Player playerTuple = session.createSelectionQuery(query)
                    .getSingleResult();

            Assertions.assertNotNull(playerTuple);
            Assertions.assertEquals("enx", playerTuple.getNickname());
            Assertions.assertEquals(0, playerTuple.getCooldown());
            Assertions.assertFalse(playerTuple.isParticlesActive());
        });
    }

}
