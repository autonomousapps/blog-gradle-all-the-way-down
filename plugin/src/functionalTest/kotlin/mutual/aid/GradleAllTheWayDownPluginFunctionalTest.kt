package mutual.aid

import com.google.common.truth.Truth.assertThat
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.createFile
import kotlin.io.path.writeText

class GradleAllTheWayDownPluginFunctionalTest {

  @TempDir
  lateinit var tempFolder: Path

  private val projectDir by lazy { tempFolder.resolve("project").createDirectories() }
  private val buildFile by lazy { projectDir.resolve("build.gradle").createFile() }
  private val settingsFile by lazy { projectDir.resolve("settings.gradle").createFile() }

  @Test fun `can run task`() {
    // Given
    settingsFile.writeText("")
    buildFile.writeText(
      """
      plugins {
          id('mutual.aid.complex-thing')
      }
      """.trimIndent()
    )

    // When
    val result = GradleRunner.create()
      .forwardOutput()
      .withPluginClasspath()
      .withArguments("greeting")
      .withProjectDir(projectDir.toFile())
      .build()

    // Then
    assertThat(result.output).contains("Hello from plugin 'mutual.aid.greeting'")
  }
}
