# gradle-maven-plugin

一款适合国人开发模式的Maven上传插件。

## 开发环境

- gradle 8.0
- Android Studio Flamingo | 2022.2.1 Patch 2
- 发布本项目到Maven仓库命令：`:maven-plugin:publishAllPublicationsToMavenRepository`

## 使用方式

1、本地`local.properties` 配置以下信息：

```properties
# 仓库地址
maven.url=https://maven.xxx.com/repository/maven-release
# 仓库上传账号
maven.user=your account
# 仓库上传密码
maven.pwd=your password
```

2、根目录下的`build.gradle` 配置插件。

```groovy
plugins {
    // 添加插件
    id 'com.github.raedev.maven' version '1.0.0' apply false
}
```

```groovy
// 在 settings.gradle 配置仓库地址
pluginManagement {
    repositories {
        maven { url 'https://maven.raeblog.com/repository/public' }
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
```

3、项目下`build.gradle` 配置上传参数。

```groovy
plugins {
    id 'com.android.library'
    id 'org.jetbrains.kotlin.android'
    id 'com.github.raedev.maven'
}

// Maven上传配置
mavenPublishing {
    //【必填】名称
    name = "com.github.raedev:demo:1.0.1"
    
    //【可选】项目地址、作者名称、作者邮件
    pomUrl = "https://github.com/raedev"
    pomName = "rae"
    pomEmail = "raedev@qq.com"

    //【可选】默认读取local.properties的配置，若设置将以此为主
    mavenUrl = ""
    mavenUser = ""
    mavenPwd = ""
}
```

## 推送发布

直接执行任务 `:publishToMaven`。

温馨提示：在`idea`的`gradle`面板中找到你的项目，再找到分组`maven`，点击`publishToMaven`即可。

## kotlin 项目使用

若需要上传kotlin源码，请升级kotlin插件版本到`1.8.21` 以上版本：

```groovy
plugins {
    id 'com.android.application' version '8.0.2' apply false
    id 'com.android.library' version '8.0.2' apply false
    // kotlin 1.8.21+ 插件支持打包源码，其他版本请验证有没有 releaseSourcesJar gradle任务。
    id 'org.jetbrains.kotlin.android' version '1.8.21' apply false
}
```




