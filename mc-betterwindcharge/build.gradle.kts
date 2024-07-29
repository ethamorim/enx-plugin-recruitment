plugins {
    // Use ShadowJar to create fat jars of the plugin (a .jar
    // with all the dependencies used in the source code,
    // so it'll run on the Spigot Server)
    id("com.github.johnrengelman.shadow") version "8.1.1"

    // Using shared build logic to build the subprojects
    id("mc-plugin-conventions")
}

dependencies {
    // Adding Spigot API to the classpath for mc-home
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")

    // Redis
    implementation("redis.clients:jedis:5.1.2")
}