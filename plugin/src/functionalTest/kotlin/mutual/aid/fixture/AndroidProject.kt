package mutual.aid.fixture

import mutual.aid.util.Resources.textResource
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

class AndroidProject @JvmOverloads constructor(
  private val agpVersion: String,
  private val useBuildScriptForAgp: Boolean = false,
  private val useBuildScriptForPlugin: Boolean = false,
  private val useMavenLocal: Boolean = false
) : AbstractProject() {

  companion object {
    // See build.gradle
    private val pluginVersion = System.getProperty("pluginVersion").toString()
  }

  private val gradlePropertiesFile = projectDir.resolve("gradle.properties")
  private val settingsFile = projectDir.resolve("settings.gradle")
  private val rootBuildFile = projectDir.resolve("build.gradle")

  init {
    // Yes, this is independent of our plugin project's properties file
    gradlePropertiesFile.writeText(
      """
      org.gradle.jvmargs=-Dfile.encoding=UTF-8
      android.useAndroidX=true
      """.trimIndent()
    )

    settingsFile.writeText(
      """
      pluginManagement {
        repositories {
          if ($useMavenLocal) mavenLocal()
          gradlePluginPortal()
          google()
          mavenCentral()
        }
        plugins {
          // Centralized version declarations. These do not directly impact the classpath. Rather,
          // this simply lets you have a single place to declare all plugin versions.
          id 'com.gradle.enterprise' version '3.8.1'
          id 'com.android.library' version '$agpVersion'
          id 'mutual.aid.meaning-of-life' version '$pluginVersion'
        }
      }
      
      // Yes, our project under test can use build scans. It's a real project!
      plugins {
        id 'com.gradle.enterprise'
      }
      
      gradleEnterprise {
        buildScan {
          publishAlways()
          termsOfServiceUrl = 'https://gradle.com/terms-of-service'
          termsOfServiceAgree = 'yes'
        }
      }
      
      rootProject.name = 'meaning-of-life'
      
      // Don't forget to add all your modules here!
      include ':lib'
      """.trimIndent()
    )

    // Put AGP on root classpath, one way or the other
    rootBuild()

    // Generate Android library project named 'lib'
    val lib = minimalAndroidLibrary("lib")
  }

  private fun rootBuild() {
    if (useBuildScriptForAgp) {
      rootBuildFile.writeText(
        """
        buildscript {
          repositories {
            if ($useMavenLocal) mavenLocal()
            google()
            mavenCentral()
          }
          dependencies {
            // This legacy mechanism requires a version 
            classpath 'com.android.tools.build:gradle:$agpVersion'
            if ($useMavenLocal && $useBuildScriptForPlugin) classpath 'mutual.aid.meaning-of-life:mutual.aid.meaning-of-life.gradle.plugin:$pluginVersion' 
          }
        }
        """.trimIndent()
      )
    } else {
      rootBuildFile.writeText(
        """
        plugins {
          // No version! We centralize version information in settings.gradle 
          id 'com.android.library' apply false
        }
        """.trimIndent()
      )
    }
  }

  private fun minimalAndroidLibrary(name: String): Path {
    val lib = projectDir.resolve(name).createDirectories()
    val libSrc = lib.resolve("src/main").createDirectories()
    libSrc.resolve("AndroidManifest.xml").writeText(
      """
      <?xml version="1.0" encoding="utf-8"?>
      <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        package="mutual.aid.$name"/>
      """.trimIndent()
    )

    lib.resolve("build.gradle").writeText(
      """
      plugins {
        // No version! One way or the other, the versions comes from the root project
        id 'com.android.library'
        id 'mutual.aid.meaning-of-life'
      }
      
      apply from: 'which.gradle'
      
      android {
        compileSdkVersion 29
        defaultConfig {
          minSdkVersion 21
          targetSdkVersion 29
          versionCode 1
          versionName "1.0"
        }
        compileOptions {
          sourceCompatibility JavaVersion.VERSION_1_8
          targetCompatibility JavaVersion.VERSION_1_8
        }
      }
      """.trimIndent()
    )

    // Write our helper script with a custom introspection task
    lib.resolve("which.gradle").writeText(textResource("which.gradle"))

    return lib
  }
}
