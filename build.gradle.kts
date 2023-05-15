plugins {
    id("java")
    id("application")
    id("java-library")
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "splendor-g-53"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.mysql:mysql-connector-j:8.0.32")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withJavadocJar()
}

javafx {
    version = "17.0.2"
    modules = arrayListOf("javafx.controls", "javafx.fxml")
}

application {
    mainModule.set("splendor")
    mainClass.set("main.Main")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "main.Main"
    }
}