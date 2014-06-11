package com.eowise.imagemagick.tasks
import com.eowise.imagemagick.specs.DefaultMagickSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.api.tasks.util.PatternSet

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
    Closure output

    Magick() {
        this.spec = new DefaultMagickSpec(this)
    }


    def convert(String baseDir, PatternSet pattern) {
        this.inputFiles = project.fileTree(baseDir).matching(pattern)
        this.output = { relativePath -> "${baseDir}/${relativePath}"  }
        this.outputDir = project.file(output(''))
        this.spec.setInputBasePath(baseDir)
    }

    def convert(String baseDir, Closure closure) {
        PatternSet pattern = project.configure(new PatternSet(), closure) as PatternSet

        convert(baseDir, pattern)
    }

    def into(Closure outputClosure) {
        this.output = outputClosure
        this.outputDir = project.file(output(''))
        this.spec.setOutput(outputClosure)
    }

    def into(String path) {
        into({ relativePath -> "${path}/${relativePath}"  })
    }

    def actions(Closure closure) {
        project.configure(spec, closure)
        inputSpec = spec.toString()
    }

    LinkedList<String> buildArgs(FileVisitDetails file) {

        LinkedList<String> execArgs = []

        spec.params.each {
            p ->
                execArgs.addAll(p.toParams(file))
        }

        return execArgs

        /*
        String name = file.getName()[0..<file.getName().lastIndexOf('.')]
        String extension = file.getName().tokenize('.').last()

        return project.file(output(file.getRelativePath().getParent().getPathString()) + '/' + rename(name, extension) )
        */
    }
    
    @TaskAction
    void execute(IncrementalTaskInputs incrementalInputs) {
        LinkedList<String> execArgs
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

                if (changedFiles.contains(f.getFile())) {

                    if (!f.getFile().isDirectory()) {

                        execArgs = buildArgs(f)

                        project.exec {
                            commandLine 'convert'
                            args execArgs
                        }
                    }

                } else if (removedFiles.contains(f.getFile())) {
                    project.delete("${path}/${f.getPath()}")
                }
        }
    }
}
