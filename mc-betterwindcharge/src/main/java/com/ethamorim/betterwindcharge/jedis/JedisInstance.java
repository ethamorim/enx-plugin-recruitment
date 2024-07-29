package com.ethamorim.betterwindcharge.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Properties;

/**
 * Classe utilitária para auxilio no uso da biblioteca Jedis.
 *
 * @author ethamorim
 */
public class JedisInstance {

    private static Jedis jedis;
    private JedisInstance() {}

    public static void connect() {
        try {
            var props = new Properties();
            try (var input = JedisInstance.class.getResourceAsStream("/.env")) {
                props.load(input);
            }
            String HOST = (String) props.get("REDIS_HOST");
            int PORT = Integer.parseInt((String) props.get("REDIS_PORT"));
            try (JedisPool jedisPool = new JedisPool(HOST, PORT)) {
                jedis = jedisPool.getResource();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setValue(String key, String value) {
        jedis.set(key, value);
    }

    public static void setValue(String key, Integer value) {
        jedis.set(key, value.toString());
    }

    public static void setValue(String key, Double value) {
        jedis.set(key, value.toString());
    }

    public static void setValue(String key, Float value) {
        jedis.set(key, value.toString());
    }

    public static void setValue(String key, Boolean value) {
        jedis.set(key, value.toString());
    }

    public static String getString(String key) {
        return jedis.get(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(jedis.get(key));
    }

    public static double getDouble(String key) {
        return Double.parseDouble(jedis.get(key));
    }

    public static float getFloat(String key) {
        return Float.parseFloat(jedis.get(key));
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(jedis.get(key));
    }
}
