package com.ethamorim.home.util;

import java.time.Duration;
import java.time.Instant;

/**
 * Classe utilitária para validar o cooldown do
 * jogador ao executar o comando /home
 *
 * @author ethamorim.
 */
public final class CooldownChecker {

    /**
     * Define se é possível executar o comando dado um instante especificado,
     * a última vez que o comando foi executado e o tempo do cooldown em milisegundos.
     */
    public static boolean canIssueCommandFromInstant(Instant instant, Instant lastIssued, int cooldown) {
        var difference = Duration.between(lastIssued, instant).toMillis();
        return difference > cooldown;
    }

    /**
     * Define se é possível executar o comando quando comparado o atual instante e
     * a última vez que o comando foi executado, dado um determinado tempo em milisegundos.
     */
    public static boolean canIssueCommandFromNow(Instant lastIssued, int cooldown) {
        return canIssueCommandFromInstant(Instant.now(), lastIssued, cooldown);
    }
}
