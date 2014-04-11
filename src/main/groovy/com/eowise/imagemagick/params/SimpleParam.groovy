package com.eowise.imagemagick.params

import org.gradle.api.file.FileVisitDetails

/**
 * Created by aurel on 14/12/13.
 */
class SimpleParam implements Param {

    String value
    String sign

    SimpleParam(String value) {
        this.value = value
        this.sign = ''
    }

    @Override
    LinkedList<String> toParams(FileVisitDetails details) {
        return [sign + value]
    }
    
    String toString() {
        return sign + value
    }

    @Override
    def positive() {
        sign = '+';
    }

    @Override
    def negative() {
        sign = '-';
    }
}
