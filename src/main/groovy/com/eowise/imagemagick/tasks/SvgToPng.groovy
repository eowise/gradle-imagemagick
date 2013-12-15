package com.eowise.imagemagick.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs

/**
 * Created by aurel on 14/12/13.
 */
class SvgToPng extends DefaultTask {


    String outputDir

    def files(FileCollection inputFiles) {
        inputs.files inputFiles
    }

    def into(String outputDir) {
        this.outputDir = outputDir
        outputs.dir outputDir
    }

    @TaskAction
    void execute(IncrementalTaskInputs inputs) {
        String outputFile
        inputs.outOfDate { change ->
            outputFile = outputDir.toString() + '/' + change.file.name.replace(".svg", ".png")
            project.exec {
                commandLine 'inkscape', '--export-png=' + outputFile, '--export-background-opacity=0', '--without-gui', change.file
            }
        }
    }
}
