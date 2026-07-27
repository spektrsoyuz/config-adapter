plugins {
    id("java")
    id("io.freefair.lombok") version "9.5.0"
}

group = "com.spektrsoyuz.configadapter"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation("org.spongepowered:configurate-hocon:4.3.0-SNAPSHOT")

    compileOnly("io.papermc.paper:paper-api:26.2.build.+")

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.1")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.test {
    useJUnitPlatform()
}