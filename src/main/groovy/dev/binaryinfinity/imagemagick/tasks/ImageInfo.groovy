package dev.binaryinfinity.imagemagick.tasks

import org.gradle.api.DefaultTask

/**
 * Created by aurel on 14/12/13.
 */
class ImageInfo extends DefaultTask {
    def size(File file) {
        new ByteArrayOutputStream().withStream { os ->
            project.exec {
                commandLine 'convert', file, '-format', '"%w"', 'info:'
                standardOutput = os
            }
            ext.width = Integer.parseInt((os.toString() =~ /"([0-9]+)"/)[0][1])

        }

        new ByteArrayOutputStream().withStream { os ->
            project.exec {
                commandLine 'convert', file, '-format', '"%h"', 'info:'
                standardOutput = os
            }
            ext.height = Integer.parseInt((os.toString() =~ /"([0-9]+)"/)[0][1])
        }
    }
}
