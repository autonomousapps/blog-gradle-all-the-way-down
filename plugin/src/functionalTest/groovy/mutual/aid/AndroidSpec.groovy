//file:noinspection GroovyAssignabilityCheck
package mutual.aid

import mutual.aid.fixture.AbstractProject
import mutual.aid.fixture.AndroidProject
import mutual.aid.gradle.Builder
import org.spockframework.runtime.extension.builtin.PreconditionContext
import spock.lang.AutoCleanup
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Specification

import static com.google.common.truth.Truth.assertThat
import static mutual.aid.util.Combinations.gradleAgpCombinations

class AndroidSpec extends Specification {

  @AutoCleanup
  AbstractProject project

  @IgnoreIf({ PreconditionContext it -> it.sys.impl == 'true' })
  def "gets the expected version of AGP on the classpath for compileOnly (#gradleVersion AGP #agpVersion useBuildScriptForAgp=#useBuildScriptForAgp useBuildScriptForPlugin=#useBuildScriptForPlugin useMavenLocal=#useMavenLocal)"() {
    given: 'An Android Gradle project'
    project = new AndroidProject(
      agpVersion,
      useBuildScriptForAgp,
      useBuildScriptForPlugin,
      useMavenLocal
    )

    when: 'We check the version of AGP on the classpath'
    def result = Builder.build(gradleVersion, project, 'lib:which', '-e', 'android')

    // The output will contain a line like this:
    // jar for 'android': file:/path/to/gradle-all-the-way-down/plugin/build/tmp/functionalTest/work/.gradle-test-kit/caches/jars-9/f19c6db5e8f27caa4113e88608762369/gradle-4.2.2.jar
    then: 'It matches what the project provides, not the plugin'
    def androidJar = result.output.split('\n').find { it.startsWith("jar for 'android'") }
    // Note that the assertion is always the same
    assertThat(androidJar).endsWith("gradle-${agpVersion}.jar")

    where:
    [gradleVersion, agpVersion, useBuildScriptForAgp, useBuildScriptForPlugin, useMavenLocal] << gradleAgpCombinations(
      // useBuildScriptForAgp
      [true, false],
      // useBuildScriptForPlugin
      [true, false],
      // useMavenLocal
      [true, false],
    )
  }

  @Requires({ PreconditionContext it -> it.sys.impl == 'true' })
  def "gets the expected version of AGP on the classpath for implementation (#gradleVersion AGP #agpVersion useBuildScriptForAgp=#useBuildScriptForAgp useBuildScriptForPlugin=#useBuildScriptForPlugin useMavenLocal=#useMavenLocal)"() {
    given: 'An Android Gradle project'
    project = new AndroidProject(
      agpVersion,
      useBuildScriptForAgp,
      useBuildScriptForPlugin,
      useMavenLocal
    )

    when: 'We check the version of AGP on the classpath'
    def result = Builder.build(
      gradleVersion,
      project,
      !useMavenLocal,
      'lib:which', '-e', 'android'
    )

    then: 'Result depends'
    def androidJar = result.output.split('\n').find { it.startsWith("jar for 'android'") }
    def expected
    if (useBuildScriptForAgp && useBuildScriptForPlugin && useMavenLocal) {
      // our 'implementation' dependency has greater priority
      expected = 'gradle-7.2.0-beta04.jar'
    } else if (useBuildScriptForAgp || useMavenLocal) {
      // the project's requirements have greater priority
      expected = "gradle-${agpVersion}.jar"
    } else {
      // our 'implementation' dependency has greater priority
      expected = 'gradle-7.2.0-beta04.jar'
    }

    // Note that the assertion is... complicated.
    assertThat(androidJar).endsWith(expected)

    where: 'There is a truly atrocious combinatorial explosion'
    [gradleVersion, agpVersion, useBuildScriptForAgp, useBuildScriptForPlugin, useMavenLocal] << gradleAgpCombinations(
      // useBuildScriptForAgp
      [true, false],
      // useBuildScriptForPlugin
      [true, false],
      // useMavenLocal
      [true, false],
    )
  }
}
