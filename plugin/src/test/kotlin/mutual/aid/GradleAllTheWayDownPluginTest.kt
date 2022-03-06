package mutual.aid

import com.google.common.truth.Truth.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class GradleAllTheWayDownPluginTest {
  @Test fun `plugin registers task`() {
    // Given
    val project = ProjectBuilder.builder().build()
    project.plugins.apply("mutual.aid.greeting")

    // Expect
    assertThat(project.tasks.findByName("greeting")).isNotNull()
  }
}
