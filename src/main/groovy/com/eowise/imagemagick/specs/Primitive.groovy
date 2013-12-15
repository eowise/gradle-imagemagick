package com.eowise.imagemagick.specs

/**
 * Created by aurel on 14/12/13.
 */
enum Primitive {
    point('%d,%d'),
    line('%d,%d %d,%d'),
    rectangle('%d,%d %d,%d')

    public final String coords

    Primitive(String coords) {
        this.coords = coords
    }
}
