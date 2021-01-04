package dev.binaryinfinity.imagemagick.tasks

import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

class ConvertTest extends Specification {

    def "Test string output file"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'; exclude: '*2.png'})

        when:
        task.convert('images', {include: '*.png'; exclude: '*2.png'})
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
        FileTree inputFiles = project.fileTree('images', {include: '*.png'; exclude: '*2.png'})

        when:
        task.convert('images', {include: '*.png'; exclude: '*2.png'})
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

    def "Test closure output dir"() {
        when:
        GradleRunner.create()
                .withProjectDir(new File("src/test/resources"))
                .withArguments("testWithClosureOutputDir", "--rerun-tasks")
                .build()

        then:
        new File("src/test/resources/out/gradle.png").exists()
    }

    def "Test closure output file with rename"() {
        Project project = ProjectBuilder.builder().withProjectDir(new File('src/test/resources')).build()
        Magick task = (Magick)project.task('magick', type: Magick)
        FileTree inputFiles = project.fileTree('images', {include: '*.png'; exclude: '*2.png'})

        when:
        task.convert('images', {include: '*.png'; exclude: '*2.png'})
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
        FileTree inputFiles = project.fileTree('images', {include: '*.png'; exclude: '*2.png'})

        when:
        task.convert('images', {include: '*.png'; exclude: '*2.png'})
        task.actions {
            inputFile()
            -background('black')
            inputFile()
        }

        then:
        inputFiles.visit { FileVisitDetails f -> assert task.buildArgs(f).join(' ') == project.file('images/gradle.png').toString() + " -background black " + project.file('images/gradle.png').toString() }
        task.outputDir == project.file('images')
    }

    def "Test removing input file remove also output file"() {
        FileUtils.copyFile(new File('src/test/resources/images/gradle.png'), new File('src/test/resources/images/gradle2.png'))

        when:
        GradleRunner.create()
                .withProjectDir(new File("src/test/resources"))
                .withArguments("basicTest", "--rerun-tasks")
                .build()

        FileUtils.deleteQuietly(new File('src/test/resources/images/gradle2.png'))

        GradleRunner.create()
                .withProjectDir(new File("src/test/resources"))
                .withArguments("basicTest")
                .build()

        then:
        new File("src/test/resources/out/gradle.png").exists()
        !new File("src/test/resources/out/gradle2.png").exists()

        cleanup:
        FileUtils.deleteQuietly(new File('src/test/resources/images/gradle2.png'))
    }
}
