plugins {
    id("mc-plugin-conventions")
}

application {
    mainClass = "com.ethamorim.betterwindcharge.BetterWindChargePlugin"
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
}