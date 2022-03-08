package mutual.aid.fixture

import kotlin.io.path.writeText

class MeaningOfLifeProject : AbstractProject() {

  private val gradlePropertiesFile = projectDir.resolve("gradle.properties")
  private val settingsFile = projectDir.resolve("settings.gradle")
  private val buildFile = projectDir.resolve("build.gradle")

  init {
    // Yes, this is independent of our plugin project's properties file
    gradlePropertiesFile.writeText("""
      org.gradle.jvmargs=-Dfile.encoding=UTF-8
    """.trimIndent())

    // Yes, our project under test can use build scans. It's a real project!
    settingsFile.writeText("""
      plugins {
        id 'com.gradle.enterprise' version '3.8.1'
      }
      
      gradleEnterprise {
        buildScan {
          publishAlways()
          termsOfServiceUrl = 'https://gradle.com/terms-of-service'
          termsOfServiceAgree = 'yes'
        }
      }
      
      rootProject.name = 'meaning-of-life'
    """.trimIndent())

    // Apply our plugin
    buildFile.writeText("""
      plugins {
        id 'mutual.aid.meaning-of-life'
      }
    """.trimIndent())
  }
}
