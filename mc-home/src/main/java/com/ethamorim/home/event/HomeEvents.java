package com.ethamorim.home.event;

import static com.ethamorim.home.persistence.HibernateConnection.sessionFactory;
import com.ethamorim.home.persistence.model.PlayerEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class HomeEvents implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var joined = event.getPlayer();
        var record = sessionFactory.fromSession(session -> session
                .bySimpleNaturalId(PlayerEntity.class)
                .load(joined.getName()));
        if (record == null) {
            sessionFactory.inTransaction(session -> {
                var entity = new PlayerEntity();
                entity.setUuid(joined.getUniqueId());
                entity.setNickname(joined.getName());

                session.persist(entity);
                session.flush();
            });
        }
    }

}
