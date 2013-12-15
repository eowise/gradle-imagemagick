package com.eowise.imagemagick.specs

/**
 * Created by aurel on 14/12/13.
 */
class Border extends MagickAction {

    private int width
    private String color

    Border(File f) {
        super(f)
    }

    def width(int value) {
        width = value
    }


    def color(String value) {
        color = value
    }

    @Override
    LinkedList<String> toParams() {
        innerParams.add('-matte')

        if (color != null) {
            innerParams.add('-bordercolor')
            innerParams.add("$color")
        }

        if (width != null) {
            innerParams.add('-border')
            innerParams.add("$width")
        }

        return innerParams
    }
}
