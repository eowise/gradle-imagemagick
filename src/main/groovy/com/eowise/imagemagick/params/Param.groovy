package com.eowise.imagemagick.params

import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
interface Param {
    LinkedList<String> toParams(FileVisitDetails details)
}
