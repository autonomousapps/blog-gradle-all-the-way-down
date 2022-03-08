package mutual.aid

import org.gradle.api.Plugin
import org.gradle.api.Project

@Suppress("unused")
class MeaningOfLifePlugin : Plugin<Project> {

  override fun apply(project: Project) {
    project.tasks.register("meaningOfLife", MeaningOfLifeTask::class.java) { t ->
      t.output.set(project.layout.buildDirectory.file("meaning-of-life.txt"))
    }
  }
}
