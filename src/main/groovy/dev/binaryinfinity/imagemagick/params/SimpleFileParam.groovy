package dev.binaryinfinity.imagemagick.params

import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 11/06/14.
 */
class SimpleFileParam implements Param {

    File file

    public SimpleFileParam(File file) {
        this.file = file
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        return [file]
    }

    @Override
    String toString() {
        return file.toString()
    }
}
