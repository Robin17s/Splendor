plugins {
    id("java")
}

group = "splendor-g-53"
version = "1.0-SNAPSHOT"

repositories() {
    mavenCentral()
}

dependencies {
    implementation("com.mysql:mysql-connector-j:8.0.31")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}