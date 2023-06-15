package com.github.raedev.maven

import com.android.build.gradle.LibraryExtension
import org.codehaus.groovy.runtime.ResourceGroovyMethods
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.Properties

internal inline val Project.gradlePublishing: PublishingExtension
    get() = extensions.getByType(PublishingExtension::class.java)

internal inline val Project.android: LibraryExtension
    get() = extensions.getByType(LibraryExtension::class.java)


internal inline val Project.localProperties: Properties
    get() {
        val properties = Properties()
        val localProperties = File(this.rootDir, "local.properties")
        if (localProperties.isFile) {
            InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
                properties.load(reader)
            }
        }
        return properties
    }

internal fun info(msg: Any) {
    println("> maven publishing plugin: $msg")
}

internal fun bingo(message: String) {
    println("> maven publishing plugin: bingo~~ $message\ndevelop by RAE (https://github.com/raedev) ٩(๑>◡<๑)۶ ")
}

internal fun cleanMavenCache(project: Project, extension: PublishConfigExtension) {
    var cacheDir = File(
        project.gradle.gradleUserHomeDir,
        "caches/modules-2/files-2.1/${extension.group}/${extension.artifactId}"
    )
    var successfully = cacheDir.deleteDir()
    info("delete cache file dir: [$successfully]: $cacheDir")
    cacheDir = File(
        project.gradle.gradleUserHomeDir,
        "caches/modules-2/metadata-2.97/descriptors/${extension.group}/${extension.artifactId}"
    )
    successfully = cacheDir.deleteDir()
    info("delete metadata cache dir: [$successfully]: $cacheDir")
}

inline fun pluginError(message: String): Nothing = throw GradleException(message)

/**
 * 递归删除目录
 */
private fun File.deleteDir(): Boolean {
    val self = this
    return if (!self.exists()) {
        true
    } else if (!self.isDirectory) {
        false
    } else {
        val files: Array<File> = self.listFiles() as Array<File>
        run {
            var result = true
            val var4 = files.size
            for (var5 in 0 until var4) {
                val file = files[var5]
                if (file.isDirectory) {
                    if (!ResourceGroovyMethods.deleteDir(file)) {
                        result = false
                    }
                } else if (!file.delete()) {
                    result = false
                }
            }
            if (!self.delete()) {
                result = false
            }
            result
        }
    }
}