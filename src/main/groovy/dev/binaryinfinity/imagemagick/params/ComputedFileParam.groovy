package dev.binaryinfinity.imagemagick.params

import org.gradle.api.Project
import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class ComputedFileParam implements Param {

    Project project
    Closure output
    Closure rename


    ComputedFileParam(Project project, Closure output) {
        this(project, output, { name, extension -> "${name}.${extension}"});
    }

    ComputedFileParam(Project project, Closure output, Closure rename) {
        this.project = project;
        this.output = output;
        this.rename = rename;
    }

    @Override
    String toString() {
        return output("path") + '/' + (rename != null ? rename('file', 'ext') : '')
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {

        String name = details.getName()[0..<details.getName().lastIndexOf('.')]
        String extension = details.getName().tokenize('.').last()

        return [project.file(output(details.getRelativePath().getParent().getPathString()) + '/' + rename(name, extension))]
    }
}
