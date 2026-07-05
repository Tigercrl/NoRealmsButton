plugins {
    id("dev.kikugie.stonecutter")
    id("co.uzzu.dotenv.gradle") version "4.0.0"
    id("fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.neoforged.moddev") version "2.0.141" apply false
    id("me.modmuss50.mod-publish-plugin") version "2.1.1" apply false
}

stonecutter active "1.20.1-fabric"

stonecutter parameters {
    constants.match(node.metadata.project.substringAfterLast('-'), "fabric", "forge", "neoforge")
    filters.include("**/*.fsh", "**/*.vsh")
}

stonecutter tasks {
    order("publishModrinth")
}

for (version in stonecutter.versions.map { it.version }.distinct()) tasks.register("publish$version") {
    group = "publishing"
    dependsOn(stonecutter.tasks.named("publishMods") { metadata.version == version })
}
