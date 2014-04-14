gradle-imagemagick
==================

Gradle tasks to easy integrate ImageMagick and Inkscape

## Install

```groovy
buildscript {
  repositories {
    maven {
      url: 'https://oss.sonatype.org/content/repositories/snapshots/'
  }

  dependencies {
    classpath 'com.eowise:gradle-imagemagick:0.1.0-SNAPSHOT'
  }
}
```

## Features

## Usage

### Magick

```groovy
task addShadow(type: com.eowise.imagemagick.tasks.Magick) {
    convert {
        stack {
            +clone
            -background('black')
            -shadow('25x3+0+1.5')
        }
        +swap
        -background('none')
        -layers('merge')
        +repage
    }
    files fileTree('img', { include: '*.png' })
    to 'outputDir'
}
```

#### Convert

`convert` accepts a closure witch is converted to a string and added as arguments to the convert command line.

### SvgToPng

### ImageInfo



