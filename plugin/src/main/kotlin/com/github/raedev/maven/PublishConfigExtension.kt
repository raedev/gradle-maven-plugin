@file:Suppress("MemberVisibilityCanBePrivate")

package com.github.raedev.maven

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.util.Properties

/**
 * 发布配置信息
 * @author RAE
 * @date 2023/06/15
 */
open class PublishConfigExtension(
    project: Project,
    private val config: Properties
) {
    var mavenUrl: String = config.getProperty("maven.url", "")
    var mavenUser: String = config.getProperty("maven.user", "")
    var mavenPwd: String = config.getProperty("maven.pwd", "")

    var group: String = project.group.toString()
    var artifactId: String = "undefined"
    var version: String = project.version.toString()


    var pomName: String = config.getProperty("pom.name", "RAE")
    var pomUrl: String = config.getProperty("pom.url", "https://github.com/raedev")
    var pomEmail: String = config.getProperty("pom.email", "raedev@qq.com")

    var name: String
        get() {
            return "$group:$artifactId:$version"
        }
        set(value) {
            val list = value.split(":")
            if (list.size < 3) throw GradleException("Wrong name, the name like this: com.github.raedev:lib:1.0.0")
            this.group = list[0]
            this.artifactId = list[1]
            this.version = list[2]
        }

    override fun toString(): String {
        return "[$name]: ${mavenUser}@${mavenPwd}:${mavenUrl}; $pomName:$pomEmail:$pomUrl;"
    }

}