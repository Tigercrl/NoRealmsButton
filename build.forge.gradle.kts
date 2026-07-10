plugins {
    id("net.neoforged.moddev.legacyforge")
    id("me.modmuss50.mod-publish-plugin")
}

val modId = property("mod.id") as String
val modVersion = property("mod.version") as String
val mcVersion = property("deps.minecraft") as String
val minVersion = property("deps.minecraft.minVersion") as String
val additionalVersionsStr =
    (findProperty("deps.minecraft.additionalVersions") as String?)?.takeIf { it != "[VERSIONED]" }
val supportedVersions = setOf(minVersion, mcVersion) +
        (additionalVersionsStr?.split(',')?.map { it.trim() } ?: emptyList())

version = "$modVersion+$minVersion-forge"
base.archivesName = modId

repositories {
    maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
}

dependencies {
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.4")!!)
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.5.4")!!)

    annotationProcessor("org.spongepowered:mixin:0.8.7:processor")
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.10.2")
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
        register(modId) {
            sourceSet(sourceSets["main"])
        }
    }

    sourceSets["main"].resources.srcDir("src/main/generated")
}

mixin {
    val refmap = add(sourceSets.main.get(), "$modId.refmap.json")
    tasks.named<Jar>("jar").configure {
        from(refmap)
    }
    config("$modId.mixins.json")
}

tasks {
    processResources {
        fun prop(name: String) = project.property(name) as String

        val props = HashMap<String, String>().apply {
            this["id"] = modId
            this["name"] = prop("mod.name")
            this["version"] = modVersion
            this["minecraft_min_version"] = minVersion
        }

        filesMatching(listOf("META-INF/mods.toml")) {
            expand(props)
        }

        doLast {
            val mixinsFile = destinationDir.resolve("$modId.mixins.json")
            if (!mixinsFile.exists()) return@doLast

            val slurper = groovy.json.JsonSlurper()
            val json = slurper.parse(mixinsFile) as MutableMap<String, Any?>
            json["refmap"] = "$modId-refmap.json"

            val builder = groovy.json.JsonBuilder(json)
            mixinsFile.writeText(builder.toPrettyString())
        }

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

    jar {
        manifest.attributes["MixinConfigs"] = "$modId.mixins.json"
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
