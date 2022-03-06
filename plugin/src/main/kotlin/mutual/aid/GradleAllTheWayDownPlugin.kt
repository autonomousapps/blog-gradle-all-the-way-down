package mutual.aid

import org.gradle.api.Plugin
import org.gradle.api.Project

class GradleAllTheWayDownPlugin : Plugin<Project> {
  override fun apply(project: Project): Unit = project.run {
    tasks.register("greeting") { task ->
      task.doLast {
        println("Hello from plugin 'mutual.aid.greeting'")
      }
    }
  }
}
