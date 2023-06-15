import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("java-gradle-plugin")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}


gradlePlugin {
    // 插件定义
    val mavenPlugin by plugins.creating {
        id = "com.github.raedev.maven"
        implementationClass = "com.github.raedev.maven.MavenPublishPlugin"
    }
}

// 插件依赖
dependencies {
    api(gradleApi())
    api("org.jetbrains.kotlin:kotlin-stdlib:1.8.21")
    compileOnly("com.android.tools.build:gradle:8.0.2")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.21")
}

// 请在 local.properties 配置当前库发布到的Maven仓库信息。
val properties = gradleLocalProperties(rootDir)
val mavenUrl: String = properties.getProperty("maven.url")
val mavenUser: String = properties.getProperty("maven.user")
val mavenPwd: String = properties.getProperty("maven.pwd")

// 发布配置信息
group = "com.github.raedev"
version = "1.0.0"
publishing {
    repositories {
        maven {
            url = uri(mavenUrl)
            credentials {
                username = mavenUser
                password = mavenPwd
            }
        }
    }
}
//val sourcesJar by tasks.registering(Jar::class) {
//    from(sourceSets.main.get().allSource)
//    archiveClassifier.set("sources")
//}
//publishing {
//    repositories {
//        maven {
//            url = uri(mavenUrl)
//            credentials {
//                username = mavenUser
//                password = mavenPwd
//            }
//        }
//    }
//    publications {
//        register("release", MavenPublication::class) {
//            from(components["java"])
//            artifactId = "maven"
//            artifact(sourcesJar.get())
//        }
//    }
//}