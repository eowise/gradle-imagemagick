package com.eowise.imagemagick.tasks

import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification


class MagickTest extends Specification {

    def "Test string output file"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.files(inputFiles)
        task.to('out')
        task.rename { fileName -> "computed-${fileName}" }
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.getOutputFile(f) == project.file("out/computed-${f.getName()}") }
        task.outputDir == project.file('out')
    }

    def "Test string output file with rename"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.files(inputFiles)
        task.to('out')
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.getOutputFile(f) == project.file("out/${f.getName()}") }
        task.outputDir == project.file('out')
    }

    def "Test closure output file"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.files(inputFiles)
        task.to { relativePath -> "out/${relativePath}"}
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.getOutputFile(f) == project.file("out/${f.getName()}") }
        task.outputDir == project.file('out')
    }

    def "Test closure output file with rename"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.files(inputFiles)
        task.to { relativePath -> "out/${relativePath}"}
        task.rename { fileName -> "computed-${fileName}" }
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.getOutputFile(f) == project.file("out/computed-${f.getName()}") }
        task.outputDir == project.file('out')
    }

    def "Test without output dir"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.files(inputFiles)
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.getOutputFile(f) == project.file("images/${f.getName()}") }
        task.outputDir == project.file('images')
    }
}
