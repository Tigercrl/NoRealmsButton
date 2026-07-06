plugins {
    id("fabric-loom")
    id("me.modmuss50.mod-publish-plugin")
}

val modVersion = property("mod.version") as String
val mcVersion = property("deps.minecraft") as String
val minVersion = property("deps.minecraft.minVersion") as String
val additionalVersionsStr =
    (findProperty("deps.minecraft.additionalVersions") as String?)?.takeIf { it != "[VERSIONED]" }
val supportedVersions = setOf(minVersion, mcVersion) +
        (additionalVersionsStr?.split(',')?.map { it.trim() } ?: emptyList())

version = "$modVersion+$minVersion-fabric"
base.archivesName = property("mod.id") as String

//loom {
//    accessWidenerPath = rootProject.file("src/main/resources/${property("mod.id")}.accesswidener")
//}

repositories {
    mavenLocal()
    maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
}

dependencies {
    minecraft("com.mojang:minecraft:$mcVersion")
    mappings(loom.layered {
        officialMojangMappings()
        if (hasProperty("deps.parchment"))
            parchment("org.parchmentmc.data:parchment-${property("deps.parchment")}@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${property("deps.fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric-api")}")

    compileOnly("com.github.spotbugs:spotbugs-annotations:4.10.2")
}

fabricApi {
    configureDataGeneration {
        outputDirectory = file("$rootDir/src/main/generated")
        client = true
    }
}

tasks {
    processResources {
        dependsOn("stonecutterGenerate")

        fun prop(name: String) = project.property(name) as String

        val props = HashMap<String, String>().apply {
            this["id"] = prop("mod.id")
            this["name"] = prop("mod.name")
            this["version"] = modVersion
            this["minecraft_min_version"] = minVersion
        }

        filesMatching(listOf("fabric.mod.json")) {
            expand(props)
        }

        exclude("**/neoforge.mods.toml", "**/mods.toml", "**/accesstransformer.cfg", "**/pack.mcmeta")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        from(remapJar.map { it.archiveFile })
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
        dependsOn("build")
    }
}

java {
    withSourcesJar()
    val javaCompat = if (stonecutter.eval(stonecutter.current.version, ">=1.20.5")) {
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
    file = tasks.remapJar.map { it.archiveFile.get() }
    additionalFiles.from(tasks.remapSourcesJar.map { it.archiveFile.get() })

    type = STABLE
    displayName = "[Fabric] ${property("mod.name")} $modVersion"
    version = "$modVersion+$minVersion-fabric"
    changelog = provider { rootProject.file("CHANGELOG.md").readText() }
    modLoaders.add("fabric")

    modrinth {
        projectId = property("publish.modrinth") as String
        accessToken = env.MODRINTH_API_KEY.orNull()
        minecraftVersions.addAll(supportedVersions)
        requires("fabric-api")
    }
}
