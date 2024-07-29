package com.ethamorim.betterwindcharge.event;

import com.ethamorim.betterwindcharge.BetterWindChargePlugin;
import com.ethamorim.betterwindcharge.jedis.JedisInstance;
import com.ethamorim.betterwindcharge.key.ConfigKeys;
import com.ethamorim.betterwindcharge.key.PowerWindCharge;
import org.bukkit.Particle;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WindCharge;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Classe de eventos do plugin, necessária para
 * modificação do comportamento do projétil do Wind Charge.
 *
 * @author ethamorim
 */
public class WindChargeEvent implements Listener {

    BetterWindChargePlugin main;

    public WindChargeEvent(BetterWindChargePlugin main) {
        this.main = main;
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent e) {
        if (e.getEntity() instanceof WindCharge) {
            Fireball wc = (Fireball) e.getEntity();
            main.addProjectile(wc);

            var velocity = wc.getVelocity();
            var factorModifier = JedisInstance.getDouble(ConfigKeys.VELOCITY_FACTOR.toString());
            wc.setVelocity(new Vector(
                    velocity.getX() * factorModifier,
                    velocity.getY() * factorModifier,
                    velocity.getZ() * factorModifier
            ));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (e.getEntity() instanceof WindCharge wc) {
            main.removeProjectile(wc.getUniqueId());

            var location = wc.getLocation();
            var factorModifier = JedisInstance.getFloat(ConfigKeys.EXPLOSION_FACTOR.toString());
            wc.setYield(factorModifier);

            var world = wc.getWorld();
            world.createExplosion(
                    location,
                    factorModifier,
                    false,
                    false,
                    wc
            );

            var random = new Random();
            if (factorModifier == PowerWindCharge.MEDIUM.getValue()) {
                world.spawnParticle(
                        Particle.GUST,
                        location,
                        random.nextInt(2),
                        random.nextInt(2),
                        random.nextInt(2),
                        random.nextInt(2)
                );
            } else if (factorModifier == PowerWindCharge.HIGH.getValue()) {
                world.spawnParticle(
                        Particle.EXPLOSION,
                        location,
                        random.nextInt(5),
                        random.nextInt(2),
                        random.nextInt(2),
                        random.nextInt(2)
                );
            } else if (factorModifier == PowerWindCharge.HUGE.getValue()) {
                world.spawnParticle(
                        Particle.EXPLOSION_EMITTER,
                        location,
                        random.nextInt(10),
                        random.nextInt(5),
                        random.nextInt(5),
                        random.nextInt(5)
                );
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (
                e.getEntity() instanceof Player &&
                e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
        ) {
            e.setDamage(0);
        }
    }
}
