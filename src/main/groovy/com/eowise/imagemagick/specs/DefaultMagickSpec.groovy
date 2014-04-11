package com.eowise.imagemagick.specs

import com.eowise.imagemagick.params.*
import com.eowise.imagemagick.tasks.Magick
import org.gradle.api.Task
import org.gradle.api.file.FileCollection

/**
 * Created by aurel on 14/12/13.
 */
class DefaultMagickSpec implements Serializable {

    LinkedList<Param> params
    transient Task task

    public DefaultMagickSpec(Task task) {
        this.task = task
        params = []
    }

    def methodMissing(String name, args) {
        SimpleParam nameParam = new SimpleParam(name)
        params.add(nameParam)
        args.each { arg -> params.add(new SimpleParam(arg.toString())) }
        return nameParam
    }

    def propertyMissing(String name) {
        SimpleParam nameParam = new SimpleParam(name)
        params.add(nameParam)
        return nameParam
    }

    def input() {
        params.add(new FileParam())
    }

    def xc(String color) {
        params.add(new SimpleParam("xc:${color}"))
    }

    def stack(Closure closure) {
        params.add(new SimpleParam('('))
        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure()
        params.add(new SimpleParam(')'))
    }

    def condition(FileCollection matchingFiles, Closure closure) {

        DefaultMagickSpec spec = new DefaultMagickSpec()
        closure.delegate = spec
        closure.resolveStrategy = Closure.DELEGATE_ONLY
        closure()
        params.add(new ConditionParam(task.getInputs().getFiles(), matchingFiles, spec.params))
    }

    String toString() {
        return params.join(' ')
    }
}
