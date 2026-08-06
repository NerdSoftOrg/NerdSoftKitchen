@file:Suppress("RedundantSuppression")

import dev.nerdsoft.build.JsonMinifier
import dev.nerdsoft.build.OptimizeTexturesTask

plugins {
    id("net.neoforged.moddev")
    id("neoforge-mutex")
}

val modId = property("mod.id") as String

version = "${property("mod.version")}+${sc.current.version}"
base.archivesName = modId

val requiredJava = when {
    sc.current.parsed >= "26.1" -> JavaVersion.VERSION_25
    sc.current.parsed >= "1.20.5" -> JavaVersion.VERSION_21
    sc.current.parsed >= "1.18" -> JavaVersion.VERSION_17
    sc.current.parsed >= "1.17" -> JavaVersion.VERSION_16
    else -> JavaVersion.VERSION_1_8
}

allprojects {
    repositories {
        mavenCentral()

        maven("https://api.modrinth.com/maven") {
            name = "Modrinth"
            content { includeGroup("maven.modrinth") }
        }

        maven("https://maven.blamejared.com/") {
            name = "BlameJared"
            content { includeGroup("mezz.jei") }
        }

        maven("https://maven.neoforged.net/releases/") {
            name = "NeoForged"
        }
    }
}

@Suppress("AvoidDuplicateDependencies")
dependencies {
    val jadeVersion = property("deps.jade.version") as String

    compileOnly("maven.modrinth:jade:$jadeVersion")
    runtimeOnly("maven.modrinth:jade:$jadeVersion")

    if (sc.current.parsed < "1.21.2") {
        val jeiVersion = property("deps.jei.version") as String
        val mcVersion = sc.current.version

        compileOnly("mezz.jei:jei-$mcVersion-common-api:$jeiVersion")
        compileOnly("mezz.jei:jei-$mcVersion-neoforge-api:$jeiVersion")
        runtimeOnly("mezz.jei:jei-$mcVersion-neoforge:$jeiVersion")
    }
}

neoForge {
    version = property("neo.version") as String

    parchment {
        mappingsVersion = property("parchment.mappings.version") as String
        minecraftVersion = property("parchment.minecraft.version") as String
    }

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        register("client") {
            gameDirectory = file("../../run/")
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("server") {
            gameDirectory = file("../../run/")
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("data") {
            gameDirectory = file("../../run/")
            data()
            programArguments.addAll(
                "--mod", modId,
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

java {
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor.set(JvmVendorSpec.ADOPTIUM)
        languageVersion.set(JavaLanguageVersion.of(requiredJava.majorVersion))
    }
}

if (project.hasProperty("release")) {
    tasks.withType<JavaCompile>().configureEach {
        options.compilerArgs.add("-g:none")
    }
}

tasks {
    processResources {
        fun MutableMap<String, String>.register(key: String, property: String) {
            val value: String = sc.properties[property]
            inputs.property(key, value)
            put(key, value)
        }

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        val props = buildMap {
            register("loader_version_range", "loader.version.range")
            register("mod_license", "mod.license")
            register("mod_id", "mod.id")
            register("mod_version", "mod.version")
            register("mod_banner", "mod.banner")
            register("mod_name", "mod.name")
            register("mod_authors", "mod.authors")
            register("mod_description", "mod.description")
            register("neo_version_range", "neo.version.range")
            register("mod_side", "mod.side")
            register("minecraft_version_range", "minecraft.version.range")

            register("jade_mod_id", "deps.jade_id")
            register("jade_type", "deps.jade_type")
            register("jade_version_range", "deps.jade.version.range")
            register("jade_ordering", "deps.jade_ordering")
            register("jade_side", "deps.jade_side")

            register("jei_mod_id", "deps.jei_id")
            register("jei_type", "deps.jei_type")
            register("jei_version_range", "deps.jei.version.range")
            register("jei_ordering", "deps.jei_ordering")
            register("jei_side", "deps.jei_side")
        }

        filesMatching("META-INF/neoforge.mods.toml") { expand(props) }

        val mixinJava = "JAVA_${requiredJava.majorVersion}"
        filesMatching("*.mixins.json") { expand("java" to mixinJava) }

        doLast {
            JsonMinifier.minifyInPlace(destinationDir, setOf(".json", ".mcmeta"))
        }
    }

    named("createMinecraftArtifacts") {
        dependsOn("stonecutterGenerate")
    }

    register<OptimizeTexturesTask>("optimizeTextures") {
        group = "build"
        description = "Losslessly recompresses PNG textures with oxipng after resource processing"
        resourcesDir.set(layout.buildDirectory.dir("resources/main"))

        rootCacheDir.set(rootProject.layout.projectDirectory.dir(".gradle"))

        if (project.hasProperty("deps.oxipng.version")) {
            oxipngVersion.set(project.property("deps.oxipng.version") as String)
        }
        if (project.hasProperty("deps.oxipng.enabled")) {
            oxipngEnabled.set((project.property("deps.oxipng.enabled") as String).toBoolean())
        }

        dependsOn("processResources")
    }

    named("jar") {
        dependsOn("optimizeTextures")
    }

    register<Copy>("buildAndCollect") {
        group = "build"
        description = "Build mod jar and copy result to `build/libs/{mod version}/`"

        dependsOn("jar")
        from(project.tasks.named("jar"))
        inputs.property("version", project.property("mod.version"))
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod.version")}"))
    }
}
