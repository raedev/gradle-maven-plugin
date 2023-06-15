package com.github.raedev.maven

import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar

/**
 * 平台，根据项目类型判断
 * @author RAE
 * @date 2023/06/15
 */
sealed class Platform {

    internal abstract fun configure(project: Project, maven: MavenPublication)

    object JavaPlatform : Platform() {

        override fun configure(project: Project, maven: MavenPublication) {
            maven.artifact(project.tasks.create("sourcesMavenJar", Jar::class.java) {
                info("java platform")
                it.group = "publishing"
                it.from(project.android.sourceSets.getByName("main").java.getSourceFiles())
                it.archiveClassifier.set("sources")
                it.archiveClassifier.convention("sources")
            })
        }

    }

    object KotlinPlatform : Platform() {

        override fun configure(project: Project, maven: MavenPublication) {
            // 无需实现
        }
    }
}

