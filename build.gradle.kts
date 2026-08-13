@file:Suppress("AvoidDuplicateDependencies", "AvoidRepositoriesInBuildGradle")

import dev.nerdsoft.build.JsonMinifier
import dev.nerdsoft.build.OptimizeTexturesTask

plugins {
    id("net.neoforged.moddev")
    id("neoforge-mutex")
}

val modId = property("mod_id") as String

version = "${property("mod_version")}+${sc.current.version}"
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

dependencies {
    val jadeVersion = property("deps_jade_version") as String

    compileOnly("maven.modrinth:jade:$jadeVersion")
    runtimeOnly("maven.modrinth:jade:$jadeVersion")

    if (sc.current.parsed < "1.21.2") {
        val jeiVersion = property("deps_jei_version") as String
        val mcVersion = sc.current.version

        compileOnly("mezz.jei:jei-$mcVersion-common-api:$jeiVersion")
        compileOnly("mezz.jei:jei-$mcVersion-neoforge-api:$jeiVersion")
        runtimeOnly("mezz.jei:jei-$mcVersion-neoforge:$jeiVersion")
    }
}

neoForge {
    version = sc.properties["neo_version"] as String

    parchment {
        mappingsVersion = sc.properties["parchment_mappings_version"] as String
        minecraftVersion = sc.properties["parchment_minecraft_version"] as String
    }

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }

    runs {
        all {
            val runDir = rootProject.file("versions/${sc.current.version}/run")
            if (!runDir.exists()) runDir.mkdirs()

            sourceSet = sourceSets.main.get()
            gameDirectory = runDir
        }

        register("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("data") {
            // Thats not a clean solution... but it works, can remove if u dont use other mods in run/mods/
            gameDirectory = rootProject.file("versions/${sc.current.version}/run/data")

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
    withSourcesJar()
    targetCompatibility = requiredJava
    sourceCompatibility = requiredJava

    toolchain {
        vendor.set(JvmVendorSpec.AZUL)
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
        fun MutableMap<String, String>.register(key: String, prop: String) {
            val value: String = sc.properties[prop] as String
            inputs.property(key, value)
            put(key, value)
        }

        duplicatesStrategy = DuplicatesStrategy.INCLUDE

        val props = buildMap {
            register("loader_version_range", "loader_version_range")
            register("mod_license", "mod_license")
            register("mod_id", "mod_id")
            register("mod_version", "mod_version")
            register("mod_name", "mod_name")
            register("mod_authors", "mod_authors")
            register("mod_banner", "mod_banner")
            register("mod_side", "mod_side")
            register("mod_description", "mod_description")
            register("mod_issues", "mod_issues")
            register("neo_version_range", "neo_version_range")
            register("minecraft_version_range", "minecraft_version_range")

            register("jade_mod_id", "deps_jade_id")
            register("jade_type", "deps_jade_type")
            register("jade_version_range", "deps_jade_version_range")
            register("jade_ordering", "deps_jade_ordering")
            register("jade_side", "deps_jade_side")

            register("jei_mod_id", "deps_jei_id")
            register("jei_type", "deps_jei_type")
            register("jei_version_range", "deps_jei_version_range")
            register("jei_ordering", "deps_jei_ordering")
            register("jei_side", "deps_jei_side")
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

        if (project.hasProperty("deps_oxipng_version")) {
            oxipngVersion.set(project.property("deps_oxipng_version") as String)
        }
        if (project.hasProperty("deps_oxipng_enabled")) {
            oxipngEnabled.set((project.property("deps_oxipng_enabled") as String).toBoolean())
        }

        dependsOn("processResources")
    }

    withType<Jar>().configureEach {
        entryCompression = ZipEntryCompression.DEFLATED
        dependsOn(processResources)

        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
        exclude("META-INF/maven/**")
        exclude("**/*.kotlin_module")

        exclude("META-INF/LICENSE*", "META-INF/NOTICE*")
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
        inputs.property("version", project.property("mod_version"))
        into(rootProject.layout.buildDirectory.file("libs/${project.property("mod_version")}"))
    }
}
