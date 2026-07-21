plugins {
    id("net.fabricmc.fabric-loom")
    `maven-publish`
    java
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("base_group").get()

base {
    archivesName = providers.gradleProperty("mod_name").get()
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.ccbluex.net/snapshots")
    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")

    implementation("net.fabricmc:fabric-loader:${property("loader_version")}")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")

    runtimeOnly("me.djtheredstoner:DevAuth-fabric:1.2.2")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")
}

tasks {
    processResources {
        val modId = providers.gradleProperty("mod_id").get()

        filesMatching(listOf("fabric.mod.json", "$modId.mixins.json")) {
            expand(getProperties())
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
