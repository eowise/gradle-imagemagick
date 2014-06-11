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
        task.convert('images', {include: '*.png'})
        task.into('out')
        task.actions {
            inputFile()
            -background('black')
            outputFile()
        }
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.buildArgs(f).join(' ') == project.file('images/gradle.png').toString() + " -background black " + project.file('out/gradle.png').toString() }
        task.outputDir == project.file('out')
    }

    def "Test string output file with rename"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.convert('images', {include: '*.png'})
        task.into('out')
        task.actions {
            inputFile()
            -background('black')
            outputFile { fileName, extension -> "computed-${fileName}.${extension}" }
        }
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.buildArgs(f).join(' ') == project.file('images/gradle.png').toString() + " -background black " + project.file('out/computed-gradle.png').toString() }
        task.outputDir == project.file('out')
    }

    def "Test closure output file"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.convert('images', {include: '*.png'})
        task.into { relativePath -> "out/${relativePath}"}
        task.actions {
            inputFile()
            -background('black')
            outputFile()
        }
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.buildArgs(f).join(' ') == project.file('images/gradle.png').toString() + " -background black " + project.file('out/gradle.png').toString() }
        task.outputDir == project.file('out')
    }

    def "Test closure output file with rename"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.convert('images', {include: '*.png'})
        task.into { relativePath -> "out/${relativePath}"}
        task.actions {
            inputFile()
            -background('black')
            outputFile { fileName, extension -> "computed-${fileName}.${extension}" }
        }
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.buildArgs(f).join(' ') == project.file('images/gradle.png').toString() + " -background black " + project.file('out/computed-gradle.png').toString() }
        task.outputDir == project.file('out')
    }

    def "Test without output dir"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'})

        when:
        task.convert('images', {include: '*.png'})
        task.actions {
            inputFile()
            -background('black')
            inputFile()
        }
        then:
        inputFiles.visit { FileVisitDetails f -> assert task.buildArgs(f).join(' ') == project.file('images/gradle.png').toString() + " -background black " + project.file('images/gradle.png').toString() }
        task.outputDir == project.file('images')
    }
}
