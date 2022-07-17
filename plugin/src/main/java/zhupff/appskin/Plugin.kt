package zhupff.appskin

import android.databinding.tool.ext.toCamelCase
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.android.utils.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.*
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class SkinPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw IllegalStateException("${javaClass.simpleName} can only apply on application module.")
        }
        println("${javaClass.simpleName} apply on ${project.name}.")
        project.extensions.getByType(AppExtension::class.java).let { extensions ->
            extensions.sourceSets.getByName("main").assets.srcDirs(project.buildDir.resolve("skin-assets"))
            extensions.applicationVariants.all { variant ->
                val once = AtomicBoolean(false)
                variant.outputs.all { _ ->
                    if (once.compareAndSet(false, true)) {
                        project.tasks.create("${SkinPackageTask::class.java.simpleName}Anchor${variant.name.toCamelCase()}")
                            .also { variant.preBuildProvider.get().dependsOn(it) }
                    }
                }
            }
        }
    }
}


class SkinPackagePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin("com.android.application")) {
            throw IllegalStateException("${javaClass.simpleName} can only apply on application module.")
        }
        println("${javaClass.simpleName} apply on ${project.name}.")
        project.extensions.getByType(AppExtension::class.java).applicationVariants.all { variant ->
            findModuleWithSkinPlugin(project.rootProject)?.let { app ->
                val task = project.tasks.register("${SkinPackageTask::class.java.simpleName}${variant.name.toCamelCase()}", SkinPackageTask::class.java) {
                    it.outputFile = app.buildDir.resolve("skin-assets/${project.name}.skin")
                }
                app.tasks.named("${SkinPackageTask::class.java.simpleName}Anchor${variant.name.toCamelCase()}") {
                    task.dependsOn("packageRelease")
                    it.dependsOn(task)
                }
            }
        }
    }

    private fun findModuleWithSkinPlugin(rootProject: Project): Project? {
        rootProject.allprojects.forEach { project ->
            if (project.plugins.hasPlugin(SkinPlugin::class.java)) {
                return project
            }
        }
        return null
    }
}


@CacheableTask
open class SkinPackageTask : DefaultTask() {
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.NONE)
    val srcDir = project.projectDir.resolve("src")

    @OutputFile
    lateinit var outputFile: File

    @TaskAction
    fun action() {
        project.buildDir.resolve("outputs/apk/release").listFiles()
            ?.find { it.name.endsWith(".apk") }
            ?.let { FileUtils.copyFile(it, outputFile) }
    }
}