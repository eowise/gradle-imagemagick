package com.eowise.imagemagick.params

import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class ConditionParam implements Param {

    FileCollection filteredFiles
    LinkedList<Param> params

    ConditionParam(FileCollection original, String filePart, LinkedList<Param> params) {
        this.filteredFiles = original.filter({ f -> f.name.contains(filePart) })
        this.params = params
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {

        LinkedList<String> toReturn = []

        if (filteredFiles.contains(details.getFile()))
            params.each { p -> toReturn.addAll(p.toParams(details)) }


        return toReturn
    }
}
