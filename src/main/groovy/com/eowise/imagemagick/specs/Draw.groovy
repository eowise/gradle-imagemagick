package com.eowise.imagemagick.specs

/**
 * Created by aurel on 14/12/13.
 */
class Draw extends MagickAction {

    private Primitive primitive
    private String coordinates
    private String fillColor
    private String strokeColor

    Draw(File f) {
        super(f)
        this.coordinates = ''
    }

    def fill(String color) {
        fillColor = color
    }

    def stroke(String color) {
        strokeColor = color
    }

    def primitive(Primitive p) {
        primitive = p
    }

    def coordinates(String c) {
        coordinates = c
    }

    @Override
    LinkedList<String> toParams() {
        if (fillColor != null) {
            innerParams.add('-fill')
            innerParams.add("$fillColor")
        }

        if (strokeColor != null) {
            innerParams.add('-stroke')
            innerParams.add("$strokeColor")
        }

        innerParams.add('-draw')
        innerParams.add("$primitive $coordinates")

        return innerParams
    }
}
