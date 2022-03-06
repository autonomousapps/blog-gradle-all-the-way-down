package mutual.aid.spec

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static com.autonomousapps.kit.truth.BuildTaskSubject.buildTasks
import static com.google.common.truth.Truth.assertAbout
import static com.google.common.truth.Truth.assertThat

class MeaningOfLifeSpec extends Specification {

  def "can determine meaning of life"() {
    given:
    def projectDir = defaultFile()
    def buildFile = new File(projectDir, "build.gradle")
    def settingsFile = new File(projectDir, "settings.gradle")

    settingsFile << ""
    buildFile << """\
      plugins {
        id 'mutual.aid.meaning-of-life'
      }
    """.stripIndent()

    when:
    def taskPath = ':meaningOfLife'
    def result = GradleRunner.create()
      .forwardOutput()
      .withPluginClasspath()
      .withArguments(taskPath, '--stacktrace')
      .withProjectDir(projectDir)
      .build()

    then: 'Task succeeded'
    assertAbout(buildTasks()).that(result.task(taskPath)).succeeded()

    and: 'We get the expected value'
    def actual = new File(projectDir, 'build/meaning-of-life.txt').text
    assertThat(actual).isEqualTo("42")
  }

  private String className = getClass().simpleName

  private File defaultFile() {
    return new File("build/functionalTest/${newSlug()}").tap {
      it.mkdirs()
    }
  }

  private String newSlug() {
    def worker = System.getProperty('org.gradle.test.worker') ?: ''
    if (!worker.isEmpty()) {
      worker = "-$worker"
    }
    return "$className-${UUID.randomUUID().toString().take(16)}$worker"
  }
}
