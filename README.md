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

### com.eowise.imagemagick.tasks.ImageInfo

### com.eowise.imagemagick.tasks.SvgToPng

### com.eowise.imagemagick.tasks.Magick

```groovy
task addShadow(type: com.eowise.imagemagick.tasks.Magick) {
    input fileTree('img', { include: '*.png' })
    output 'outputDir'
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
}
```