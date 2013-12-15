package com.eowise.imagemagick.params

import com.eowise.imagemagick.specs.Border
import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class BorderParam extends ClosureParam {

    BorderParam(Closure c) {
        super(c)
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        Border b = new Border(details.getFile())
        closure.delegate = b
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure()

        return b.toParams()
    }
}
