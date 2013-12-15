package com.eowise.imagemagick.params

import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class SimpleParam implements Param {

    String value

    SimpleParam(String value) {
        this.value = value
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        return [value]
    }
}
