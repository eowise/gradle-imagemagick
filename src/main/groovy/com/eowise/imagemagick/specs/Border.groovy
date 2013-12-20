package com.eowise.imagemagick.specs

import com.eowise.imagemagick.params.Param
import com.eowise.imagemagick.params.SimpleParam

/**
 * Created by aurel on 14/12/13.
 */
class Border extends MagickAction {

    Border(LinkedList<Param> params) {
        super(params)
        params.add(new SimpleParam('-matte'))
    }

    def width(int width) {
        params.add(new SimpleParam('-border'))
        params.add(new SimpleParam("$width"))
    }


    def color(String color) {
        params.add(new SimpleParam('-bordercolor'))
        params.add(new SimpleParam("$color"))
    }
}
