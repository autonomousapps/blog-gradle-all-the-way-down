package mutual.aid.util

import org.gradle.util.GradleVersion

final class Combinations {

  private Combinations() {
    throw new UnsupportedOperationException("Don't instantiate")
  }

  static List<GradleVersion> gradleVersions() {
    return [
      GradleVersion.version('7.3.3'),
      GradleVersion.version('7.4.1')
    ]
  }

  static List<String> agpVersions() {
    return ['4.2.2', '7.1.2']
  }

  /**
   *  Returns a list with four elements. Each element is a list of two elements.
   *  <pre>
   *  [
   *    [Gradle 7.3.3, AGP 4.2.2],
   *    [Gradle 7.3.3, AGP 7.4.1],
   *    [Gradle 7.4.1, AGP 4.2.2],
   *    [Gradle 7.4.1, AGP 7.4.1]
   *  ]
   * </pre>
   *
   *  nb: AGP versions generally have a minimal version of Gradle that they work with, so if that
   *  comes up in your own project, you'll need to elaborate on this to strip out incompatible
   *  combinations.
   */
  static List<List> gradleAgpCombinations(List<Object>... others = []) {
    return [gradleVersions(), agpVersions(), *others].combinations()
  }
}
