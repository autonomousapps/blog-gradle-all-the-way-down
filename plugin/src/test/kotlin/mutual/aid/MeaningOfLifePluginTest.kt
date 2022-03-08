package mutual.aid

import com.google.common.truth.Truth.assertThat
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test

class MeaningOfLifePluginTest {
  @Test fun `plugin registers task`() {
    // Given
    val project = ProjectBuilder.builder().build()
    project.plugins.apply("mutual.aid.meaning-of-life")

    // Expect
    assertThat(project.tasks.findByName("meaningOfLife")).isNotNull()
  }
}
