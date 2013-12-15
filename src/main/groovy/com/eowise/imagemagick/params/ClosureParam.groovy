package com.eowise.imagemagick.params

/**
 * Created by aurel on 14/12/13.
 */
abstract class ClosureParam implements Param {
    Closure closure

    ClosureParam(Closure c) {
        this.closure = c
    }
}
