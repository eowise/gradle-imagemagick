package com.eowise.imagemagick.specs

import com.eowise.imagemagick.params.*
import com.eowise.imagemagick.tasks.Magick
import org.gradle.api.file.FileCollection

/**
 * Created by aurel on 14/12/13.
 */
class DefaultMagickSpec implements MagickSpec {

    LinkedList<Param> params
    Magick task

    public DefaultMagickSpec(Magick task) {
        this.task = task
        params = []
    }

    def verbose() {
        params.add(new SimpleParam('-verbose'))
    }

    def input() {
        params.add(new FileParam())
    }

    def xc(String color) {
        params.add(new SimpleParam("xc:${color}"))
    }

    def color(String color) {
        params.add(new SimpleParam(color))
    }

    def size(String size) {
        params.add(new SimpleParam('-size'))
        params.add(new SimpleParam(size))
    }

    def resize(float ratio) {

        params.add(new SimpleParam('-resize'))
        params.add(new SimpleParam("${ratio * 100}%"))

    }

    def gravity(String gravity) {
        params.add(new SimpleParam('-gravity'))
        params.add(new SimpleParam(gravity))
    }

    def geometry(String geometry) {
        params.add(new SimpleParam('-geometry'))
        params.add(new SimpleParam(geometry))
    }

    def swap() {
        params.add(new SimpleParam('+swap'))
    }

    def cloneLast() {
        params.add(new SimpleParam('+clone'))
    }

    def background(String color) {
        params.add(new SimpleParam('-background'))
        params.add(new SimpleParam(color))
    }

    def stack(Closure closure) {
        params.add(new SimpleParam('('))
        closure.delegate = this
        closure()
        params.add(new SimpleParam(')'))
    }

    def condition(FileCollection matchingFiles, Closure closure) {

        MagickSpec spec = new DefaultMagickSpec()
        closure.delegate = spec
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure()

        params.add(new ConditionParam(task.inputs.getFiles(), matchingFiles, spec.params))
    }


    def shadow(String param) {
        params.add(new SimpleParam('-shadow'))
        params.add(new SimpleParam(param))
    }


    def draw(Closure closure) {
        params.add(new DrawParam(closure))
    }

    def border(Closure closure) {
        params.add(new BorderParam(closure))
    }

    def layers(String operation) {
        params.add(new SimpleParam('-layers'))
        params.add(new SimpleParam(operation))
    }

    def repage() {
        params.add(new SimpleParam('+repage'))
    }

    def composite() {
        params.add(new SimpleParam('-composite'))
    }
}
