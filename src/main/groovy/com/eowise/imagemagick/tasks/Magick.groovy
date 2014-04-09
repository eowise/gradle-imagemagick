package com.eowise.imagemagick.tasks
import com.eowise.imagemagick.specs.DefaultMagickSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.StopExecutionException
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
/**
 * Created by aurel on 14/12/13.
 */
class Magick extends DefaultTask {

    
    @InputFiles
    FileTree inputFiles
    @OutputDirectory
    File outputDir
    @Input
    String inputSpec
    DefaultMagickSpec spec

    Magick() {
        this.spec = new DefaultMagickSpec(this)
    }



    def input(FileTree inputFiles) {
        this.inputFiles = inputFiles
    }

    def output(String path) {
        outputDir = project.file(path)
    }

    def convert(Closure closure) {
        project.configure(spec, closure)
        inputSpec = spec.toString()
    }
    
    @TaskAction
    void execute(IncrementalTaskInputs incrementalInputs) {
        LinkedList<String> execArgs = []
        FileCollection changedFiles = project.files()
        FileCollection removedFiles = project.files()
        File outputFile

        incrementalInputs.outOfDate {
            change ->
                changedFiles.from(change.file)
        }

        incrementalInputs.removed {
            remove ->
                removedFiles.from(remove.file)
        }


        inputFiles.visit {
            FileVisitDetails f ->
                outputFile = project.file("${outputDir}/${f.getPath()}")

                if (changedFiles.contains(f.getFile()) || !outputFile.exists()) {

                    if (!f.getFile().isDirectory()) {
                        spec.params.each {
                            p ->
                                execArgs.addAll(p.toParams(f))
                        }

                        execArgs.addFirst(f.getFile().toString())
                        execArgs.addLast(outputFile.toString())

                        outputFile.parentFile.mkdirs()

                        logger.info("args", execArgs)

                        project.exec {
                            commandLine 'convert'
                            args execArgs
                        }

                        execArgs.clear()
                    }


                } else if (removedFiles.contains(f.getFile())) {
                    project.delete("${path}/${f.getPath()}")
                }
        }
    }
}
