package com.ethamorim.home.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.*;

/**
 * Classe para testar a funcionalidade do verificador de cooldown.
 *
 * @author ethamorim
 */
public class CooldownCheckerTest {

    /**
     * Diferença entre instantes deve ser maior que o cooldown,
     * consequentemente retornando `true`.
     */
    @Test
    void canIssueCommand() {
        var instant = ZonedDateTime.of(
                        LocalDateTime.of(2024, Month.JULY, 28, 14, 58, 18),
                        ZoneId.systemDefault())
                .toInstant();
        var lastIssued = ZonedDateTime.of(
                        LocalDateTime.of(2024, Month.JULY, 28, 14, 58, 15),
                        ZoneId.systemDefault())
                .toInstant();
        var canIssue = CooldownChecker.canIssueCommandFromInstant(
                instant,
                lastIssued,
                2000
        );
        Assertions.assertTrue(canIssue);
    }

    /**
     * Diferença entre instantes deve ser menor que o cooldown,
     * consequentemente retornando `false`.
     */
    @Test
    void cannotIssueCommand() {
        var instant = ZonedDateTime.of(
                        LocalDateTime.of(2024, Month.JULY, 28, 14, 58, 18),
                        ZoneId.systemDefault())
                .toInstant();
        var lastIssued = ZonedDateTime.of(
                        LocalDateTime.of(2024, Month.JULY, 28, 14, 58, 16),
                        ZoneId.systemDefault())
                .toInstant();
        var canIssue = CooldownChecker.canIssueCommandFromInstant(
                instant,
                lastIssued,
                10000
        );
        Assertions.assertFalse(canIssue);
    }

    /**
     * CooldownChecker deve sempre retornar true nesse exemplo,
     * visto que qualquer instante após a escrita desse código
     * será maior que o instante definido abaixo.
     */
    @Test
    void canIssueCommandBecauseNowIsAlwaysGreaterThanTheSpecifiedTime() {
        var lastIssued = ZonedDateTime.of(
                        LocalDateTime.of(2024, Month.JULY, 28, 14, 58, 16),
                        ZoneId.systemDefault())
                .toInstant();
        var canIssue = CooldownChecker.canIssueCommandFromNow(
                lastIssued,
                2000
        );
        Assertions.assertTrue(canIssue);
    }

}
