plugins {
    // Using shared build logic to build the subprojects
    id("mc-plugin-conventions")
}

application {
    mainClass = "com.ethamorim.home.HomePlugin"
}

dependencies {
    // Adding Spigot API to the classpath for mc-home
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")

    // MariaDB JDBC
    implementation("org.mariadb.jdbc:mariadb-java-client:3.4.0")

    // Hibernate ORM
    implementation("org.hibernate.orm:hibernate-core:6.5.2.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.0.Final")
    implementation("org.glassfish:jakarta.el:4.0.2")
    implementation("org.hibernate.orm:hibernate-agroal:6.5.2.Final")
    implementation("io.agroal:agroal-pool:2.1")
    implementation("org.apache.logging.log4j:log4j-core:2.20.0")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.5.2.Final")
}