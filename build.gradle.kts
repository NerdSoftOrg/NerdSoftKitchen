import com.panzer.gradle.JsonMinifier
import com.panzer.gradle.PanzerModExtension

plugins {
    id("panzer.neoforge-mod")
    idea
}

val modProps = extensions.getByType(PanzerModExtension::class.java).props

val mainSourceSet = sourceSets.main.get()

repositories {
    maven("https://maven.blamejared.com/") {
        name = "BlameJared"
    }
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
    }
}

neoForge {
    runs {
        all {
            val runDir = rootProject.file("versions/${modProps.currentVersion}/run")
            if (!runDir.exists()) runDir.mkdirs()

            sourceSet = mainSourceSet
            gameDirectory = runDir
        }

        register("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modProps.modId)
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modProps.modId)
        }

        register("data") {
            gameDirectory = rootProject.file("versions/${modProps.currentVersion}/run/data")
            data()
            programArguments.addAll(
                "--mod", modProps.modId,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }
    }
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")
    }
}

dependencies {
    val jadeVersion = modProps.req(project, "jade_version")
    val jeiVersion = modProps.req(project, "jei_version")
    val mcVersion = modProps.currentVersion

    // Jade
    compileOnly("maven.modrinth:jade:$jadeVersion")
    runtimeOnly("maven.modrinth:jade:$jadeVersion")

    // JEI
    if (jeiVersion != "0") {
        compileOnly("mezz.jei:jei-$mcVersion-common-api:$jeiVersion")
        compileOnly("mezz.jei:jei-$mcVersion-neoforge-api:$jeiVersion")
        runtimeOnly("mezz.jei:jei-$mcVersion-neoforge:$jeiVersion")
    }
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        val props = buildMap {
            fun register(targetKey: String, tomlKey: String = targetKey) {
                val value = modProps.req(project, tomlKey)
                inputs.property(targetKey, value)
                put(targetKey, value)
            }

            register("loader_version_range", "neoforge.loader_version_range")
            register("mod_license", "mod.license")
            register("mod_id", "mod.id")
            register("mod_version", "mod.version")
            register("mod_name", "mod.name")
            register("mod_authors", "mod.authors")
            register("mod_banner", "mod.banner")
            register("mod_side", "mod.side")
            register("mod_description", "mod.description")
            register("mod_issues", "mod.issues")
            register("neo_version_range")
            register("minecraft_version_range")

            // Jade
            register("jade_mod_id", "integrations.jade_id")
            register("jade_type", "integrations.jade_type")
            register("jade_version_range", "jade_version_range")
            register("jade_ordering", "integrations.jade_ordering")
            register("jade_side", "integrations.jade_side")

            // JEI
            register("jei_mod_id", "integrations.jei_id")
            register("jei_type", "integrations.jei_type")
            register("jei_version_range", "jei_version_range")
            register("jei_ordering", "integrations.jei_ordering")
            register("jei_side", "integrations.jei_side")
        }

        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }

        val mixinJava = "JAVA_${modProps.requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }

        doLast {
            JsonMinifier.minifyInPlace(destinationDir, setOf(".json", ".mcmeta"))
        }
    }

    withType<Jar>().configureEach {
        entryCompression = ZipEntryCompression.DEFLATED
        dependsOn(processResources)

        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
        exclude("META-INF/maven/**")
        exclude("**/*.kotlin_module")
        exclude("META-INF/NOTICE*")
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/INDEX.LIST")
        exclude("**/*.kotlin_builtins")
    }

    named("jar") {
        dependsOn("optimizeTextures")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Build mod jar and copy result to `build/libs/{mod version}/`"

        dependsOn("jar")
        from(project.tasks.named("jar"))
        inputs.property("version", modProps.modVersion)
        into(rootProject.layout.buildDirectory.file("libs/${modProps.modVersion}"))
    }
}
