package mutual.aid.fixture

import java.nio.file.Path
import java.util.UUID
import kotlin.io.path.createDirectories

abstract class AbstractProject : AutoCloseable {

  val projectDir = Path.of("build/functionalTest/${slug()}").createDirectories()
  private val buildDir = projectDir.resolve("build")

  fun buildFile(filename: String): Path {
    return buildDir.resolve(filename)
  }

  private fun slug(): String {
    val worker = System.getProperty("org.gradle.test.worker")?.let { w ->
      "-$w"
    }.orEmpty()
    return "${javaClass.simpleName}-${UUID.randomUUID().toString().take(16)}$worker"
  }

  override fun close() {
    projectDir.parent.toFile().deleteRecursively()
  }
}