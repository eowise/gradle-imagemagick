package com.eowise.imagemagick.specs

import com.eowise.imagemagick.params.Param

/**
 * Created by aurel on 14/12/13.
 */
abstract class MagickAction {

    protected LinkedList<Param> params

    MagickAction(LinkedList<Param> params) {
        this.params = params
    }

}
