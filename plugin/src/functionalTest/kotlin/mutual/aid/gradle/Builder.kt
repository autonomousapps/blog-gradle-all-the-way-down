package mutual.aid.gradle

import mutual.aid.fixture.AbstractProject
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.util.GradleVersion
import java.lang.management.ManagementFactory.getRuntimeMXBean
import java.nio.file.Path

object Builder {

  @JvmOverloads
  @JvmStatic
  fun build(
    gradleVersion: GradleVersion = GradleVersion.current(),
    project: AbstractProject,
    vararg args: String
  ): BuildResult = runner(gradleVersion, project.projectDir, *args).build()

  @JvmOverloads
  @JvmStatic
  fun build(
    gradleVersion: GradleVersion = GradleVersion.current(),
    projectDir: Path,
    vararg args: String
  ): BuildResult = runner(gradleVersion, projectDir, *args).build()

  @JvmOverloads
  @JvmStatic
  fun buildAndFail(
    gradleVersion: GradleVersion = GradleVersion.current(),
    project: AbstractProject,
    vararg args: String
  ): BuildResult = runner(gradleVersion, project.projectDir, *args).buildAndFail()

  @JvmOverloads
  @JvmStatic
  fun buildAndFail(
    gradleVersion: GradleVersion = GradleVersion.current(),
    projectDir: Path,
    vararg args: String
  ): BuildResult = runner(gradleVersion, projectDir, *args).buildAndFail()

  private fun runner(
    gradleVersion: GradleVersion,
    projectDir: Path,
    vararg args: String
  ): GradleRunner = GradleRunner.create().apply {
    forwardOutput()
    withPluginClasspath()
    withGradleVersion(gradleVersion.version)
    withProjectDir(projectDir.toFile())
    withArguments(args.toList() + "-s")
    // Ensure this value is true when `--debug-jvm` is passed to Gradle, and false otherwise
    withDebug(getRuntimeMXBean().inputArguments.toString().indexOf("-agentlib:jdwp") > 0)
  }
}
