package com.ethamorim.home.persistence;

import com.ethamorim.home.persistence.model.HomeEntity;
import com.ethamorim.home.persistence.model.HomeEntity_;
import com.ethamorim.home.persistence.model.PlayerEntity;
import org.hibernate.Session;

import java.util.Optional;

/**
 * Utility class for reusable queries throughout the plugin.
 *
 * @author ethamorim
 */
public final class PluginQueries {

    /**
     * Get the player by the nickname.
     *
     * @param session Hibernate session
     * @param nickname Player nickname
     */
    public static Optional<PlayerEntity> getPlayerByNaturalId(Session session, String nickname) {
        return session.bySimpleNaturalId(PlayerEntity.class).loadOptional(nickname);
    }

    /**
     * Get a player's home record.
     *
     * @param session Hibernate session
     * @param name The name of the home
     * @param player The player owner
     */
    public static Optional<HomeEntity> getHomeByNaturalId(Session session, String name, PlayerEntity player) {
        return session.byNaturalId(HomeEntity.class)
                .using(HomeEntity_.NAME, name)
                .using(HomeEntity_.PLAYER, player)
                .loadOptional();
    }

}
