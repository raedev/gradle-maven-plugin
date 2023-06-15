package com.github.raedev.maven

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin

/**
 * 插件主入口
 * @author RAE
 * @date 2023/06/15
 */
open class MavenPublishPlugin : Plugin<Project> {

    protected open val extensionName = "mavenPublishing"
    protected open val taskName = "publishToMaven"
    protected open val taskGroup = "maven"
    protected lateinit var extension: PublishConfigExtension

    /**
     * 插件入口
     */
    override fun apply(project: Project) {
        // 应用Gradle官方的Maven插件
        project.plugins.apply(MavenPublishPlugin::class.java)
        project.extensions.create(
            extensionName, PublishConfigExtension::class.java,
            project,
            project.localProperties
        )
        // 自动创建publishing 节点并注入参数
        project.createPublishingNode()
        // 创建任务
        project.tasks.create(taskName) {
            it.group = taskGroup
            it.dependsOn("publishReleasePublicationToMavenRepository")
            it.doLast {
                cleanMavenCache(project, extension)
                bingo("the [${extension.name}] has been successfully pushed to ${extension.mavenUrl} ")
            }
        }
    }

    protected open fun Project.createPublishingNode() {
        this.afterEvaluate { project ->
            extension = project.extensions.getByType(PublishConfigExtension::class.java)
            configPublishExtension(extension)
            project.gradlePublishing.repositories.maven {
                it.url = uri(extension.mavenUrl)
                it.isAllowInsecureProtocol = true
                it.credentials { p ->
                    p.username = extension.mavenUser
                    p.password = extension.mavenPwd
                }
            }
            project.gradlePublishing.publications.create("release", MavenPublication::class.java) {
                it.from(project.components.getByName("release"))
                it.groupId = extension.group
                it.artifactId = extension.artifactId
                it.version = extension.version
                val platform: Platform = when {
                    project.plugins.hasPlugin("org.jetbrains.kotlin.android") -> Platform.KotlinPlatform
                    else -> Platform.JavaPlatform
                }
                // 根据不同平台配置
                platform.configure(project, it)
                it.pom { p ->
                    p.name.set(extension.pomName)
                    p.url.set(extension.pomUrl)
                    p.developers { d ->
                        d.developer { dp ->
                            dp.id.set(extension.pomName)
                            dp.name.set(extension.pomName)
                            dp.email.set(extension.pomEmail)
                        }
                    }
                }
            }

            project.tasks.named("publishReleasePublicationToMavenRepository").configure {
                it.doFirst {
                    if (extension.mavenUrl.isEmpty()) pluginError("please set url in mavenPublishing { mavenUrl='' }")
                    if (extension.group.isEmpty()) pluginError(" please set group in mavenPublishing { group='' }")
                    if (extension.artifactId.isEmpty()) pluginError("please set artifactId in mavenPublishing { artifactId='' }")
                    if (extension.artifactId == "undefined") pluginError("please set artifactId in mavenPublishing { artifactId='' }")
                    if (extension.version.isEmpty()) pluginError("please set version in mavenPublishing { version='' }")
                }
            }

            info("$extension")
        }
    }

    /**
     * 配置的时候触发
     */
    protected open fun configPublishExtension(extension: PublishConfigExtension) = Unit

}
