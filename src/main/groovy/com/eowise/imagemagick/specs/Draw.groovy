package com.eowise.imagemagick.specs

import com.eowise.imagemagick.params.Param
import com.eowise.imagemagick.params.SimpleParam

/**
 * Created by aurel on 14/12/13.
 */
class Draw extends MagickAction {

    Draw(LinkedList<Param> params) {
        super(params)
    }

    def fill(String color) {
        params.add(new SimpleParam('-fill'))
        params.add(new SimpleParam("$color"))
    }

    def stroke(String color) {
        params.add(new SimpleParam('-stroke'))
        params.add(new SimpleParam("$color"))
    }

    def draw(Primitive primitive, String coordinates) {
        params.add(new SimpleParam('-draw'))
        params.add(new SimpleParam("$primitive $coordinates"))
    }
}
