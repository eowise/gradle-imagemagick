package dev.binaryinfinity.imagemagick.params

import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 11/03/15.
 */
class FormattingParam implements Param {

    String format
    ComputedFileParam inputFile

    FormattingParam(String format, ComputedFileParam inputFile) {
        this.format = format
        this.inputFile = inputFile
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        def toReturn = []

        toReturn.addAll(inputFile.toParams(details))
        toReturn.add('-format')
        toReturn.add(format)
        toReturn.add('info:')


        return toReturn
    }

    @Override
    String toString() {
        return inputFile.toString() + ':' + format
    }
}
