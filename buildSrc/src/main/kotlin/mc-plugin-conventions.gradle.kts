plugins {
    id("java")
}

group = "com.ethamorim"
version = "1.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven {
        // Spigot API repository
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        // Repository Spigot API needs
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

val javaVersion = JavaVersion.VERSION_21
java {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion.toString()))
}

tasks.withType(JavaCompile::class).configureEach {
    options.release.set(javaVersion.toString().toInt())
}

tasks.test {
    useJUnitPlatform()
}
