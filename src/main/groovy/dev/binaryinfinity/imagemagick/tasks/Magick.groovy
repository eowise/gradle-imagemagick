package dev.binaryinfinity.imagemagick.tasks

import dev.binaryinfinity.imagemagick.specs.FormattingSpec
import dev.binaryinfinity.imagemagick.specs.DefaultMagickSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Internal
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
    @Input
    String command
    @Input
    Closure output

    @Internal
    DefaultMagickSpec spec
    @Internal
    FormattingSpec formattingSpec
//    @Input
//    @Optional
    Closure outputFileFromInputFileClosure

    Magick() {
        this.spec = new DefaultMagickSpec(this)
        this.formattingSpec = new FormattingSpec(this)
    }

    def verb(String baseDir, PatternSet pattern, String command) {
        this.inputFiles = project.fileTree(baseDir).matching(pattern)
        this.output = { relativePath -> "${baseDir}/${relativePath}" }
        this.outputDir = project.file(output(''))
        this.spec.setInputBasePath(baseDir)
        this.formattingSpec.setInputBasePath(baseDir)
        this.command = command
    }
    def convert(String baseDir, PatternSet pattern) {
        verb(baseDir, pattern, 'convert')
    }

    def convert(String baseDir, Closure closure) {
        convert(baseDir, project.configure(new PatternSet(), closure) as PatternSet)
    }

    def magick(String baseDir, PatternSet pattern) {
        verb(baseDir, pattern, 'magick')
    }

    def magick(String baseDir, Closure closure) {
        magick(baseDir, project.configure(new PatternSet(), closure) as PatternSet)
    }

    def into(Closure outputClosure) {
        this.output = outputClosure
        this.outputDir = project.file(output(''))
        this.spec.setOutput(outputClosure)
    }

    def into(String path) {
        into({ relativePath -> "${path}/${relativePath}"  })
    }

    def formatting(Closure closure) {
        project.configure(formattingSpec, closure)
    }

    def actions(Closure closure) {
        project.configure(spec, closure)
        inputSpec = spec.toString()
    }

    def outputFileFromInputFile(Closure outputFileFromInputFile) {
        this.outputFileFromInputFileClosure = outputFileFromInputFile
    }

    LinkedList<String> buildArgs(FileVisitDetails file) {

        LinkedList<String> execArgs = []

        spec.params.each {
            p ->
                execArgs.addAll(p.toParams(file))
        }

        return execArgs
    }
    
    @TaskAction
    void execute(IncrementalTaskInputs incrementalInputs) {
        LinkedList<String> execArgs
        FileCollection changedFiles = project.files()

        incrementalInputs.outOfDate {
            change ->
                changedFiles.from(change.file)
        }


        inputFiles.visit {
            FileVisitDetails f ->

                if (changedFiles.contains(f.getFile())) {

                    if (!f.getFile().isDirectory()) {

                        formattingSpec.formats.each {
                            id, param ->
                                project.exec {
                                    commandLine command
                                    args param.toParams(f)
                                    standardOutput new FileOutputStream("${temporaryDir}/${f.getRelativePath()}.${id}.mvg")
                                }
                        }

                        execArgs = buildArgs(f)

                        project.exec {
                            commandLine command
                            args execArgs
                        }
                    }

                }
        }

        if (incrementalInputs.isIncremental() && outputFileFormInputFileClosure != null) {
            incrementalInputs.removed {
                remove ->
                    println "Applying outPutFileFromInputClosure to ${remove.file}"
                    File outputFileToRemove = outputFileFromInputFileClosure(remove.file)
                    outputFileToRemove.delete()
            }
        }
    }
}
