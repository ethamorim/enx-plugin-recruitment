package com.ethamorim.home.persistence.model;

import static org.hibernate.cfg.AvailableSettings.*;

import com.ethamorim.home.persistence.PluginQueries;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.UUID;

/**
 * Testa a persistência das entidades de tabelas e suas associações
 *
 * @author ethamorim
 */
public class PersistenceTest {

    static SessionFactory sessionFactory;

    /**
     * Inicia a conexão com uma instância de banco de dados na memória
     * antes que os testes sejam executados.
     */
    @BeforeAll
    static void bootstrapHibernate() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(PlayerEntity.class)
                .addAnnotatedClass(HomeEntity.class)
                .setProperty(JAKARTA_JDBC_URL, "jdbc:h2:mem:db1")
                .setProperty(SHOW_SQL, true)
                .setProperty(FORMAT_SQL, true)
                .setProperty(HIGHLIGHT_SQL, true)
                .setProperty("hibernate.agroal.maxSize", 5)
                .buildSessionFactory();
        sessionFactory.getSchemaManager().exportMappedObjects(true);
    }

    /**
     * A cada teste executado, limpa todos os registros das tabelas.
     */
    @AfterEach
    void cleanUp() {
        sessionFactory.inTransaction(session -> {
            session.createMutationQuery("delete from HomeEntity")
                    .executeUpdate();
            session.createMutationQuery("delete from PlayerEntity")
                    .executeUpdate();
        });
    }

    /**
     * Testa a persistência de PlayerEntity
     * e verifica por valores padrões não definidos na criação.
     */
    @Test
    void shouldPersistPlayer() {
        sessionFactory.inTransaction(session -> {
            var player = new PlayerEntity();
            player.setUuid(UUID.randomUUID());
            player.setNickname("enx");

            session.persist(player);
            session.flush();
        });

        sessionFactory.inSession(session -> {
            var player = PluginQueries.getPlayerByNaturalId(session, "enx");
            Assertions.assertTrue(player.isPresent());
            Assertions.assertNotEquals(0, player.get().getId());
            Assertions.assertEquals("enx", player.get().getNickname());
            Assertions.assertEquals(0, player.get().getCooldown());
            Assertions.assertFalse(player.get().isParticlesActive());
        });
    }

    /**
     * Testa a persistência de HomeEntity
     * buscando através de sua associação forte PlayerEntity
     */
    @Test
    void shouldPersistHome() {
        sessionFactory.inTransaction(session -> {
            var player = new PlayerEntity();
            player.setUuid(UUID.randomUUID());
            player.setNickname("enx");

            var home = new HomeEntity();
            home.setName("casa");
            home.setPlayer(player);
            home.setLocation(new HomeLocation(0, 0, 0));

            session.persist(player);
            session.persist(home);
            session.flush();
        });

        sessionFactory.inSession(session -> {
            var player = PluginQueries.getPlayerByNaturalId(session, "enx");
            Assertions.assertTrue(player.isPresent());

            var home = PluginQueries.getHomeByNaturalId(session, "casa", player.get());
            Assertions.assertTrue(home.isPresent());
            Assertions.assertNotEquals(0, home.get().getId());
            Assertions.assertEquals("casa", home.get().getName());
        });
    }

    /**
     * Verifica se updates usando `merge` com entidades desanexadas funciona sem erros.
     * Utiliza o atributo `lastIssued` de `PlayerEntity` para fazer a alteração
     * e comparação.
     */
    @Test
    void shouldUpdatePlayer() {
        sessionFactory.inTransaction(session -> {
            var player = new PlayerEntity();
            player.setNickname("enx");
            player.setUuid(UUID.randomUUID());

            var lastIssued = ZonedDateTime.of(
                        LocalDateTime.of(2024, Month.JULY, 28, 15, 44),
                        ZoneId.systemDefault())
                .toInstant();
            player.setLastIssued(lastIssued);
            session.persist(player);
            session.flush();
        });

        var optionalPlayer = sessionFactory.fromSession(session ->
                PluginQueries.getPlayerByNaturalId(session, "enx"));
        Assertions.assertTrue(optionalPlayer.isPresent());
        var player = optionalPlayer.get();

        var timeToCompare = ZonedDateTime.of(
                    LocalDateTime.of(2024, Month.JULY, 28, 15, 44),
                    ZoneId.systemDefault())
            .toInstant();
        Assertions.assertEquals(0, player.getLastIssued().compareTo(timeToCompare));

        sessionFactory.inTransaction(session -> {
            player.setLastIssued(Instant.now());
            session.merge(player);
            session.flush();
        });

        var playerAfterUpdate = sessionFactory.fromSession(session ->
                PluginQueries.getPlayerByNaturalId(session, "enx"))
                .orElseThrow();

        int comparison = playerAfterUpdate.getLastIssued().compareTo(timeToCompare);
        Assertions.assertTrue(comparison > 0);

    }
}
