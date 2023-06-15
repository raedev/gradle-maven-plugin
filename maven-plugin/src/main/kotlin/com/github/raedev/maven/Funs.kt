@file:Suppress("NOTHING_TO_INLINE")

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

/**
 * Maven Publishing 插件配置节点
 */
internal inline val Project.gradlePublishing: PublishingExtension
    get() = extensions.getByType(PublishingExtension::class.java)

/**
 * 项目的 Android 节点
 */
internal inline val Project.android: LibraryExtension
    get() = extensions.getByType(LibraryExtension::class.java)


/**
 * 读取 local.properties
 */
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

/**
 * 输出日志
 */
internal fun info(msg: Any) {
    println("> maven publishing plugin: $msg")
}

/**
 * 完成日志
 */
internal fun bingo(message: String) {
    println("> maven publishing plugin: bingo~~ $message\ndevelop by RAE (https://github.com/raedev) ٩(๑>◡<๑)۶ ")
}

/**
 * 清除本地缓存
 */
internal fun cleanMavenCache(project: Project, extension: PublishConfigExtension) {
    val path = "${extension.group}/${extension.artifactId}"
    val cacheDir = File(
        project.gradle.gradleUserHomeDir,
        "caches/modules-2/"
    )
    cacheDir.listFiles()?.forEach {
        if (it.startsWith("files")) {
            val dir = File(it, path)
            info("delete cache file dir: [${dir.deleteDir()}: $dir")
            return@forEach
        }
        if (it.startsWith("meta")) {
            val dir = File(it, "descriptors/$path")
            info("delete metadata file dir: [${dir.deleteDir()}: $dir")
            return@forEach
        }
    }
}

/**
 * 抛出插件异常
 */
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