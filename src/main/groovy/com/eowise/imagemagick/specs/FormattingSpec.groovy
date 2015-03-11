package com.eowise.imagemagick.specs

import com.eowise.imagemagick.params.ComputedFileParam
import com.eowise.imagemagick.params.FormattingParam
import org.gradle.api.Task

/**
 * Created by aurel on 11/03/15.
 */
class FormattingSpec {

    Task task
    Map formats
    String inputBasePath
    ComputedFileParam inputFile

    FormattingSpec(Task task) {
        this.task = task
        this.formats = [:]
    }

    def include(String id, String format) {
        if (inputFile == null) {
            inputFile = new ComputedFileParam(
                    task.getProject(),
                    { relativePath -> "${inputBasePath}/${relativePath}"  },
                    { fileName, extension -> "${fileName}.${extension}" }
            )
        }
        formats[id] = new FormattingParam(format, inputFile)
    }

    def setInputBasePath(String inputBasePath) {
        this.inputBasePath = inputBasePath
    }


}
