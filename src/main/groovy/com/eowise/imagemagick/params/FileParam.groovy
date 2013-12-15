package com.eowise.imagemagick.params

import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class FileParam implements Param {

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        return [details.getFile()]
    }
}
