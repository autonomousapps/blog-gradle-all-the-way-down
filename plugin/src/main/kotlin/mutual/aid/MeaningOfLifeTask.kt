package mutual.aid

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask

@UntrackedTask(because = "No meaningful inputs")
abstract class MeaningOfLifeTask : DefaultTask() {

  init {
    group = "Gradle All the Way Down"
    description = "Computes the meaning of life"
  }

  @get:OutputFile
  abstract val output: RegularFileProperty

  @TaskAction fun action() {
    // Clean prior output
    val output = output.get().asFile.also { it.delete() }

    // Do a computation
    val meaningOfLife = DeepThought().meaningOfLife()

    // Write output to disk
    output.writeText(meaningOfLife.toString())
  }
}
