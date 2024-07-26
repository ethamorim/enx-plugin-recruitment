plugins {
    id("application")
}

group = "com.ethamorim"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass = "com.ethamorim.betterwindcharge.BetterWindChargePlugin"
}

tasks.test {
    useJUnitPlatform()
}