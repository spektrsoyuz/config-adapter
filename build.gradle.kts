plugins {
    id("java")
    id("maven-publish")
    id("io.freefair.lombok") version "9.5.0"
}

group = "com.spektrsoyuz"
version = "1.0.1"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

val paperApi = "io.papermc.paper:paper-api:26.2.build.+"

dependencies {
    implementation("org.spongepowered:configurate-hocon:4.3.0-SNAPSHOT")

    compileOnly(paperApi)

    testImplementation("org.junit.jupiter:junit-jupiter:6.1.2")
    testImplementation("org.mockito:mockito-core:5.23.0")
    testImplementation(paperApi)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

tasks.named<Test>("test") {
    useJUnitPlatform()

    maxHeapSize = "1G"
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
                username = System.getenv("REPOSITORY_USER")
                password = System.getenv("REPOSITORY_TOKEN")
            }
        }
    }
}
