package dev.binaryinfinity.imagemagick.params

import org.gradle.api.Task
import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 11/03/15.
 */
class FormattedParam implements Param {

    String id
    Task task

    FormattedParam(String id, Task task) {
        this.id = id
        this.task = task
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        return [ "@${task.temporaryDir}/${details.getRelativePath()}.${id}.mvg" ]
    }

    @Override
    String toString() {
        return id
    }
}
