plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.github.raedev.maven") version ("0.0.1")
}

mavenPublishing {
    name = "com.github.raedev:demokt:1.0.0"
}

//tasks.create<org.gradle.jvm.tasks.Jar>("sourcesKtMavenJar") {
//    val dirs = project.android.sourceSets.getByName("main").kotlin.srcDirs() as com.android.build.gradle.internal.api.DefaultAndroidSourceDirectorySet
////    println(dirs.srcDirs)
//    from(dirs.srcDirs)
//    archiveClassifier.set("sources")
//}

android {
    namespace = "com.github.raedev.demo.kt"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}