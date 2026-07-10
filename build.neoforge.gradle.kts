plugins {
    id("net.neoforged.moddev")
    id("me.modmuss50.mod-publish-plugin")
}

val modVersion = property("mod.version") as String
val mcVersion = property("deps.minecraft") as String
val minVersion = property("deps.minecraft.minVersion") as String
val additionalVersionsStr =
    (findProperty("deps.minecraft.additionalVersions") as String?)?.takeIf { it != "[VERSIONED]" }
val supportedVersions = setOf(minVersion, mcVersion) +
        (additionalVersionsStr?.split(',')?.map { it.trim() } ?: emptyList())

version = "$modVersion+$minVersion-neoforge"
base.archivesName = property("mod.id") as String

repositories {
    maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
}

dependencies {
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.10.2")
}

neoForge {
    version = property("deps.neoforge") as String
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = (property("deps.parchment") as String).split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        register("client") {
            gameDirectory = file("run/")
            client()
        }
        register("server") {
            gameDirectory = file("run/")
            server()
        }
    }

    mods {
        register(property("mod.id") as String) {
            sourceSet(sourceSets["main"])
        }
    }

    sourceSets["main"].resources.srcDir("src/main/generated")
}

tasks {
    processResources {
        fun prop(name: String) = project.property(name) as String

        val props = HashMap<String, String>().apply {
            this["id"] = prop("mod.id")
            this["name"] = prop("mod.name")
            this["version"] = modVersion
            this["minecraft_min_version"] = minVersion
        }

        filesMatching(listOf("META-INF/neoforge.mods.toml")) {
            expand(props)
        }

        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/mods.toml", "**/pack.mcmeta")
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(jar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=26.1")) {
        JavaVersion.VERSION_25
    } else if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
        JavaVersion.VERSION_21
    } else if (stonecutter.eval(stonecutter.current.version, ">=1.17")) {
        JavaVersion.VERSION_17
    } else {
        JavaVersion.VERSION_1_8
    }
    sourceCompatibility = javaCompat
    targetCompatibility = javaCompat
}

publishMods {
    file = tasks.jar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.named<Jar>("sourcesJar").map { it.archiveFile.get() })

    type = STABLE
    displayName = "[NeoForge] ${property("mod.name")} $modVersion"
    version = "$modVersion+$minVersion-neoforge"
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("neoforge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.addAll(supportedVersions)
    }
}
