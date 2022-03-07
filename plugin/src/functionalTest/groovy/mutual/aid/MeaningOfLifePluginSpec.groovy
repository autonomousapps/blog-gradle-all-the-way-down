package mutual.aid

import mutual.aid.fixture.AbstractProject
import mutual.aid.fixture.MeaningOfLifeProject
import mutual.aid.gradle.Builder
import org.gradle.util.GradleVersion
import spock.lang.AutoCleanup
import spock.lang.Specification

import static com.autonomousapps.kit.truth.BuildTaskSubject.buildTasks
import static com.google.common.truth.Truth.assertAbout
import static com.google.common.truth.Truth.assertThat

class MeaningOfLifePluginSpec extends Specification {

  @AutoCleanup
  AbstractProject project

  def "can determine meaning of life (#gradleVersion)"() {
    given: 'A Gradle project'
    project = new MeaningOfLifeProject()

    when: 'We ask for the meaning of life'
    def taskPath = ':meaningOfLife'
    def result = Builder.build(gradleVersion, project, taskPath)

    then: 'Task succeeded'
    assertAbout(buildTasks()).that(result.task(taskPath)).succeeded()

    and: 'It is 42'
    def actual = project.buildFile('meaning-of-life.txt').text
    assertThat(actual).isEqualTo('42')

    where:
    gradleVersion << [GradleVersion.version('7.3.3'), GradleVersion.version('7.4')]
  }
}
