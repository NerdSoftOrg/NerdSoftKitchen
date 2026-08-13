package dev.nerdsoft.build

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.File
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Locale
import javax.inject.Inject

abstract class OptimizeTexturesTask @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {

    @get:InputDirectory
    abstract val resourcesDir: DirectoryProperty

    @get:Internal
    abstract val rootCacheDir: DirectoryProperty

    @get:Input
    @get:Optional
    abstract val oxipngVersion: Property<String>

    @get:Input
    @get:Optional
    abstract val oxipngEnabled: Property<Boolean>

    init {
        oxipngVersion.convention("10.1.1")
        oxipngEnabled.convention(true)
    }

    @TaskAction
    fun run() {
        if (!oxipngEnabled.get()) {
            logger.lifecycle("[optimizeTextures] Disabled in gradle.properties (deps.oxipng.enabled=false).")
            return
        }

        val pngs = resourcesDir.get().asFile.walkTopDown()
            .filter { it.isFile && it.extension.equals("png", ignoreCase = true) }
            .map { it.absolutePath }
            .toList()

        if (pngs.isEmpty()) return

        val binaryFile = resolveOrDownloadOxipng() ?: run {
            logger.warn("[optimizeTextures] Could not obtain oxipng binary. Skipping optimization.")
            return
        }

        logger.lifecycle("[optimizeTextures] Optimizing {} PNG textures using oxipng v{}...", pngs.size, oxipngVersion.get())

        execOperations.exec {
            commandLine(listOf(binaryFile.absolutePath, "-o", "6", "--strip", "safe", "--alpha") + pngs)
        }
    }

    @Synchronized
    private fun resolveOrDownloadOxipng(): File? {
        val version = oxipngVersion.get()
        val osName = System.getProperty("os.name").lowercase(Locale.ROOT)
        val osArch = System.getProperty("os.arch").lowercase(Locale.ROOT)

        val (target, extension) = when {
            osName.contains("win") -> {
                val arch = if (osArch.contains("64")) "x86_64" else "i686"
                "$arch-pc-windows-msvc" to "zip"
            }
            osName.contains("mac") -> {
                val arch = if (osArch.contains("aarch64") || osArch.contains("arm")) "aarch64" else "x86_64"
                "$arch-apple-darwin" to "tar.gz"
            }
            osName.contains("nux") || osName.contains("nix") -> {
                val arch = if (osArch.contains("aarch64") || osArch.contains("arm")) "aarch64" else "x86_64"
                "$arch-unknown-linux-musl" to "tar.gz"
            }
            else -> return null
        }

        val exeName = if (osName.contains("win")) "oxipng.exe" else "oxipng"
        val cacheDir = rootCacheDir.dir("oxipng-cache/$version").get().asFile
        val targetExe = File(cacheDir, exeName)

        if (targetExe.exists() && targetExe.length() > 0L) return targetExe

        cacheDir.mkdirs()

        val assetName = "oxipng-$version-$target.$extension"
        val downloadUrl = "https://github.com/oxipng/oxipng/releases/download/v$version/$assetName"

        val tempArchive = File(cacheDir, "download-${System.currentTimeMillis()}.$extension")

        return try {
            logger.lifecycle("[optimizeTextures] Downloading oxipng v{} ($assetName)...", version)

            URI.create(downloadUrl).toURL().openStream().use { input ->
                Files.copy(input, tempArchive.toPath(), StandardCopyOption.REPLACE_EXISTING)
            }

            if (extension == "zip") {
                java.util.zip.ZipFile(tempArchive).use { zip ->
                    val entry = zip.entries().asSequence().firstOrNull { it.name.endsWith(exeName) }
                        ?: throw IllegalStateException("$exeName binary not found inside ZIP archive")

                    zip.getInputStream(entry).use { inStream ->
                        val tempExe = File(cacheDir, "$exeName.tmp")
                        Files.copy(inStream, tempExe.toPath(), StandardCopyOption.REPLACE_EXISTING)

                        if (!osName.contains("win")) {
                            tempExe.setExecutable(true)
                        }

                        Files.move(tempExe.toPath(), targetExe.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
                    }
                }
            } else {
                execOperations.exec {
                    commandLine("tar", "-xzf", tempArchive.absolutePath, "-C", cacheDir.absolutePath)
                }
                if (!osName.contains("win")) {
                    targetExe.setExecutable(true)
                }
            }

            tempArchive.delete()
            targetExe
        } catch (e: Exception) {
            tempArchive.delete()
            if (targetExe.exists() && targetExe.length() > 0L) {
                return targetExe
            }
            logger.warn("[optimizeTextures] Failed downloading or extracting oxipng: ${e.message}")
            null
        }
    }
}
