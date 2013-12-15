package com.eowise.imagemagick.params

import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class ReMapFileParam implements Param {

    File path

    ReMapFileParam(File path) {
        this.path = path
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        //details.getFile().mkdirs()
        return ["${path}/${details.getPath()}"]
    }
}
