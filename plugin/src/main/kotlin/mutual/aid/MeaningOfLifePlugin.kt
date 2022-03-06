package mutual.aid

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.UntrackedTask

@Suppress("unused")
class MeaningOfLifePlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.tasks.register("meaningOfLife", MeaningOfLifeTask::class.java) { task ->
      task.output.set(project.layout.buildDirectory.file("meaning-of-life.txt"))
    }
  }
}

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
    // ...long-running computation...
    output.writeText("42")
  }
}
