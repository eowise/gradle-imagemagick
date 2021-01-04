package dev.binaryinfinity.imagemagick.params

import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
interface Param extends Serializable {
    LinkedList<String> toParams(FileVisitDetails details)
}
