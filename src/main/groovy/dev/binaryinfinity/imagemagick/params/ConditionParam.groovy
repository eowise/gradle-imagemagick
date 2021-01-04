package dev.binaryinfinity.imagemagick.params

import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.util.PatternSet

/**
 * Created by aurel on 14/12/13.
 */
class ConditionParam implements Param {

    FileTree matchingFiles
    LinkedList<Param> params

    ConditionParam(FileCollection original, PatternSet pattern, LinkedList<Param> params) {
        this.matchingFiles = original.asFileTree.matching(pattern)
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
