package com.eowise.imagemagick.specs

import com.eowise.imagemagick.params.*
import org.gradle.api.Task
import org.gradle.api.tasks.util.PatternSet

/**
 * Created by aurel on 14/12/13.
 */
class DefaultMagickSpec implements Serializable {

    LinkedList<Param> params
    Task task
    Closure output
    String inputBasePath

    public DefaultMagickSpec(Task task) {
        this.task = task
        this.params = []
    }

    def setOutput(Closure output) {
        this.output = output
    }

    def setInputBasePath(String inputBasePath) {
        this.inputBasePath = inputBasePath
    }


    def methodMissing(String name, args) {
        SimpleParam nameParam = new SimpleParam(name)
        params.add(nameParam)

        args.each {
            arg ->
                String argValue = arg.toString();
                if ( argValue.startsWith('@')) {
                    params.add(new FormattedParam(argValue[1..<arg.length()], task))
                }
                else {
                    params.add(new SimpleParam(argValue.toString()))
                }
        }

        return nameParam
    }

    def propertyMissing(String name) {
        if (task.hasProperty(name)) {
            return task[name]
        }
        else {
            SimpleParam nameParam = new SimpleParam(name)
            params.add(nameParam)
            return nameParam
        }

    }

    def inputFile() {
        params.add(
                new ComputedFileParam(
                        task.getProject(),
                        { relativePath -> "${inputBasePath}/${relativePath}"  },
                        { fileName, extension -> "${fileName}.${extension}" }
                )
        )
    }

    def outputFile() {
        params.add(
                new ComputedFileParam(
                        task.getProject(),
                        output,
                        { fileName, extension -> "${fileName}.${extension}" }
                )
        )
    }

    def outputFile(Closure rename) {
        params.add(
                new ComputedFileParam(
                        task.getProject(),
                        output,
                        rename
                )
        )
    }

    def file(File f) {
        params.add(new SimpleFileParam(f))
    }

    def file(Closure path, Closure rename) {
        params.add(new ComputedFileParam(task.getProject(), path, rename))
    }

    def xc(String color) {
        params.add(new SimpleParam("xc:${color}"))
    }

    // Memory program register: See http://www.imagemagick.org/Usage/files/#mpr
    def mpr(String label) {
        params.add(new SimpleParam("mpr:${label}"))
    }

    def stack(Closure closure) {
        params.add(new SimpleParam('('))
        closure.delegate = this
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
        params.add(new SimpleParam(')'))
    }

    def condition(PatternSet pattern, Closure closure) {
        DefaultMagickSpec spec = new DefaultMagickSpec(task)
        closure.delegate = spec
        closure.resolveStrategy = Closure.DELEGATE_FIRST
        closure()
        params.add(new ConditionParam(task.getInputs().getFiles(), pattern, spec.params))
        println(toString())
    }


    String toString() {
        return params.join(' ')
    }
}
