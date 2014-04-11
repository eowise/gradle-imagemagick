package com.eowise.imagemagick.specs

import com.eowise.imagemagick.tasks.Magick
import org.gradle.api.Task
import spock.lang.Specification

/**
 * Created by aurel on 11/04/14.
 */
class DefaultMagickSpecTest extends Specification {


    def "methodMissing is called"() {
        DefaultMagickSpec spec = new DefaultMagickSpec(Mock(Task))
        Closure closure = {
            -background('black')
        }

        closure.delegate = spec

        when:
        closure()
        then:
        spec.toString() == '-background black'
    }

    def "propertyMissing is called"() {
        DefaultMagickSpec spec = new DefaultMagickSpec(Mock(Task))
        Closure closure = {
            -clone
        }

        closure.delegate = spec

        when:
        closure()
        then:
        spec.toString() == '-clone'
    }


    def "- is added"() {
        DefaultMagickSpec spec = new DefaultMagickSpec(Mock(Task))
        Closure closure = {
            -width(1)
        }

        closure.delegate = spec

        when:
        closure()
        then:
        spec.toString() == '-width 1'
    }

    def "+ is added"() {
        DefaultMagickSpec spec = new DefaultMagickSpec(Mock(Task))
        Closure closure = {
            +repage
        }

        closure.delegate = spec

        when:
        closure()
        then:
        spec.toString() == '+repage'
    }
}
