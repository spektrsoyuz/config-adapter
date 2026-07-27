plugins {
    id("java")
    id("maven-publish")
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
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            artifactId = "config-adapter"
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/spektrsoyuz/config-adapter")

            credentials {
                System.getenv("REPOSITORY_USER")
                System.getenv("REPOSITORY_TOKEN")
            }
        }
    }
}
