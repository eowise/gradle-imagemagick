package com.eowise.imagemagick.specs

/**
 * Created by aurel on 14/12/13.
 */
abstract class MagickAction {

    protected LinkedList<String> innerParams
    protected File file;


    MagickAction(File file) {
        this.file = file
        this.innerParams = []
    }

    abstract LinkedList<String> toParams()
}
