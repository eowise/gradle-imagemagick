package com.eowise.imagemagick.params

import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class ConditionParam implements Param {

    FileCollection matchingFiles
    LinkedList<Param> params

    ConditionParam(FileCollection original, FileCollection matchingFiles, LinkedList<Param> params) {
        this.matchingFiles = matchingFiles
        this.params = params
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {

        LinkedList<String> toReturn = []

        if (matchingFiles.contains(details.getFile()))
            params.each { p -> toReturn.addAll(p.toParams(details)) }


        return toReturn
    }
    
    String toString() {
        return params.join(' ')
    }
}
