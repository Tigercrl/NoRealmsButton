pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.6"
}

stonecutter {
    create(rootProject) {
        fun match(version: String, vararg loaders: String) = loaders
            .forEach {
                version("$version-$it", version).buildscript = if (it == "fabric") {
                    if (stonecutter.eval(version, ">=26.1")) "build.$it-modern.gradle.kts"
                    else "build.$it-old.gradle.kts"
                } else "build.$it.gradle.kts"
            }

        match("1.20.1", "fabric", "forge")
        match("1.21.1", "fabric", "neoforge")
        match("26.1.2", "fabric", "neoforge")

        vcsVersion = "1.20.1-fabric"
    }
}
