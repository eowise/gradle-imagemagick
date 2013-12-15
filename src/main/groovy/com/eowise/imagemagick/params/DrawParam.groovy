package com.eowise.imagemagick.params

import com.eowise.imagemagick.specs.Draw
import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class DrawParam extends ClosureParam {

    DrawParam(Closure c) {
        super(c)
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        Draw d = new Draw(details.getFile())
        closure.delegate = d
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure()

        return d.toParams()
    }
}
