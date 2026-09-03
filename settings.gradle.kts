import java.io.File

val commonToml = File(rootDir, "../panzer-build-logic/common.stonecutter.properties.toml")
val modToml = File(rootDir, "mod.stonecutter.properties.toml")
val mergedToml = File(rootDir, "stonecutter.properties.toml")

if (!commonToml.exists()) {
    error("Strict Configuration Error: shared 'common.stonecutter.properties.toml' was not found at '${commonToml.path}'. Check that '../panzer-build-logic' exists next to this project.")
}
if (!modToml.exists()) {
    error("Strict Configuration Error: 'mod.stonecutter.properties.toml' was not found in root project directory.")
}

fun splitTomlBlocks(lines: List<String>): LinkedHashMap<String, MutableList<String>> {
    val tableHeaderRegex = Regex("""^\[(.+)]\s*$""")
    val blocks = LinkedHashMap<String, MutableList<String>>()
    var currentKey = ""
    blocks[currentKey] = mutableListOf()

    for (line in lines) {
        val match = tableHeaderRegex.find(line.trim())
        if (match != null) {
            currentKey = match.groupValues[1].trim()
        }
        blocks.getOrPut(currentKey) { mutableListOf() }.add(line)
    }

    if (blocks[""]?.all { it.isBlank() } == true) {
        blocks.remove("")
    }
    return blocks
}

val commonBlocks = splitTomlBlocks(commonToml.readLines())
val modBlocks = splitTomlBlocks(modToml.readLines())

val orderedKeys = LinkedHashSet<String>()
orderedKeys.addAll(commonBlocks.keys)
orderedKeys.addAll(modBlocks.keys)

val mergedBuilder = StringBuilder()
for (key in orderedKeys) {
    val chosen = modBlocks[key] ?: commonBlocks[key] ?: continue
    mergedBuilder.append(chosen.joinToString("\n"))
    mergedBuilder.append("\n\n")
}

mergedToml.writeText(mergedBuilder.toString().trimEnd() + "\n")

pluginManagement {
    includeBuild("../panzer-build-logic")

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
    }

    fun requirePluginVersion(key: String): String {
        val file = file("stonecutter.properties.toml")
        return file.useLines { lines ->
            lines.map { it.trim() }
                .firstOrNull { it.startsWith("$key =") || it.startsWith("$key=") }
                ?.split("=")?.get(1)?.trim()?.removeSurrounding("\"")?.removeSurrounding("'")
        } ?: error("Required plugin version key '$key' is missing in 'stonecutter.properties.toml'.")
    }

    plugins {
        id("dev.kikugie.stonecutter") version requirePluginVersion("stonecutter")
        id("org.gradle.toolchains.foojay-resolver-convention") version requirePluginVersion("foojay")
        id("net.neoforged.moddev") version requirePluginVersion("moddev")
    }
}

plugins {
    id("dev.kikugie.stonecutter")
    id("org.gradle.toolchains.foojay-resolver-convention")
}

val tomlFile = file("stonecutter.properties.toml")
val tomlLines = tomlFile.readLines().map { it.trim() }

fun getTomlString(key: String): String {
    return tomlLines.firstOrNull { it.startsWith("$key =") || it.startsWith("$key=") }
        ?.split("=")?.get(1)?.trim()?.removeSurrounding("\"")?.removeSurrounding("'")
        ?: error("Strict Configuration Error: Key '$key' is missing in 'stonecutter.properties.toml'.")
}

fun getTomlList(key: String): List<String> {
    val line = tomlLines.firstOrNull { it.startsWith("$key =") || it.startsWith("$key=") }
        ?: error("Strict Configuration Error: Array key '$key' is missing in 'stonecutter.properties.toml'.")
    val content = line.substringAfter("[").substringBefore("]").trim()
    if (content.isEmpty()) error("Strict Configuration Error: Array key '$key' cannot be empty in 'stonecutter.properties.toml'.")
    return content.split(",").map { it.trim().removeSurrounding("\"").removeSurrounding("'") }
}

val scVersions = getTomlList("versions")
val scVcsVersion = getTomlString("vcs_version")
val modName = getTomlString("name")

stonecutter {
    create(rootProject) {
        versions(scVersions)
        vcsVersion = scVcsVersion
    }
}

rootProject.name = modName
