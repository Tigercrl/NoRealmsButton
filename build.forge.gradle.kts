plugins {
    id("net.neoforged.moddev.legacyforge")
    id("me.modmuss50.mod-publish-plugin")
}

val modVersion = property("mod.version") as String
val minVersion = property("deps.minecraft.minVersion") as String
val additionalVersionsStr = findProperty("deps.minecraft.additionalVersions") as String?
val supportedVersions = listOf(minVersion, modVersion) +
        (additionalVersionsStr?.split(',')?.map { it.trim() } ?: emptyList())

tasks.named<ProcessResources>("processResources") {
    fun prop(name: String) = project.property(name) as String

    val props = HashMap<String, String>().apply {
        this["id"] = prop("mod.id")
        this["name"] = prop("mod.name")
        this["version"] = modVersion
        this["minecraft_min_version"] = minVersion
    }

    filesMatching(listOf("META-INF/mods.toml")) {
        expand(props)
    }
}

version = "$modVersion+$minVersion-forge"
base.archivesName = property("mod.id") as String

repositories {
    maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
}

dependencies {
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.10.2")

    implementation(jarJar("io.github.llamalad7:mixinextras-neoforge:0.5.4")!!)
}

legacyForge {
    version = property("deps.forge") as String
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

mixin {
//    add(sourceSets.main.get(), "${property("mod.id")}-refmap.json")
    config("${property("mod.id")}.mixins.json")
}

tasks {
    processResources {
        exclude("**/fabric.mod.json", "**/*.accesswidener", "**/neoforge.mods.toml")
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
    additionalFiles.from(tasks.named<org.gradle.jvm.tasks.Jar>("sourcesJar").map { it.archiveFile.get() })

    type = STABLE
    displayName = "[Forge] ${property("mod.name")} $modVersion"
    version = "$modVersion+$minVersion-forge"
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("forge")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.addAll(supportedVersions)
    }
}
