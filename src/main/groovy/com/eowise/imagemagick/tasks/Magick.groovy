package com.eowise.imagemagick.tasks
import com.eowise.imagemagick.specs.DefaultMagickSpec
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileTree
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
    Closure rename

    Magick() {
        this.spec = new DefaultMagickSpec(this)
        this.rename = { fileName, extension -> "${fileName}.${extension}" }
    }


    def from(String baseDir, PatternSet pattern) {
        this.inputFiles = project.fileTree(baseDir).matching(pattern)
        this.output = { relativePath -> "${baseDir}/${relativePath}"  }
        this.outputDir = project.file(output(''))
    }

    def from(String baseDir, Closure closure) {
        PatternSet pattern = project.configure(new PatternSet(), closure) as PatternSet

        from(baseDir, pattern)
    }

    def into(Closure outputClosure) {
        this.output = outputClosure
        this.outputDir = project.file(output(''))
    }

    def into(String path) {
        this.output = { relativePath -> "${path}/${relativePath}"  }
        this.outputDir = project.file(path)
    }

    def rename(Closure renameClosure) {
        this.rename = renameClosure
    }

    def convert(Closure closure) {
        project.configure(spec, closure)
        inputSpec = spec.toString()
    }

    File getOutputFile(FileVisitDetails file) {

        String name = file.getName()[0..<file.getName().lastIndexOf('.')]
        String extension = file.getName().tokenize('.').last()

        return project.file(output(file.getRelativePath().getParent().getPathString()) + '/' + rename(name, extension) )
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
                outputFile = getOutputFile(f)

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
