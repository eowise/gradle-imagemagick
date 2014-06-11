gradle-imagemagick
==================

Gradle tasks to easy integrate ImageMagick

## Install

```groovy
buildscript {
  repositories {
    maven {
      url: 'https://oss.sonatype.org/content/repositories/snapshots/'
  }

  dependencies {
    classpath 'com.eowise:gradle-imagemagick:0.3.0-SNAPSHOT'
  }
}
```

## Usage

### Magick

```groovy
task addShadow(type: com.eowise.imagemagick.tasks.Magick) {
    convert 'img', { include: '*.png' }
    into 'outputDir'
    actions {
        inputFile()
        stack {
            +clone
            -background('black')
            -shadow('25x3+0+1.5')
        }
        +swap
        -background('none')
        -layers('merge')
        +repage
        outputFile()
    }

}
```

#### `convert`

`convert` accepts a closure witch is converted to a string and added as arguments to the [ImageMagick convert](http://www.imagemagick.org/script/convert.php) command line.

You can add a [stack](http://www.imagemagick.org/script/command-line-processing.php#stack) with the `stack` closure.

#### `files`

#### `to`

#### `rename`

### SvgToPng

### ImageInfo



