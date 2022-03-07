package mutual.aid

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

internal class DeepThoughtTest {

  @Test fun `but what's the question`() {
    assertThat(DeepThought().meaningOfLife()).isEqualTo(42)
  }
}