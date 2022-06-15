# androidx-colorpicker
A simple color-picker library for Android, which provides these classes:

 - [`ColorPickerDialogFragment`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/ColorPickerDialogFragment.java)
 - [`ColorPickerPreference`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/ColorPickerPreference.java)

It looks about like this:

  ![Screenshot 01](https://raw.githubusercontent.com/syslogic/androidx-colorpicker/master/screenshots/screenshot_01.png)

The library resides in the local `:library` module:

    dependencies {
        implementation project(path: ':library')
    }

It's also available on JitPack; either by version tag or by `master-SNAPSHOT`.<br/>
This requires the repository URL to be added: `maven { url 'https://jitpack.io' }`

    dependencies {
        implementation "io.syslogic:androidx-colorpicker:1.0.4"
    }

[![](https://jitci.com/gh/syslogic/androidx-colorpicker/svg)](https://jitci.com/gh/syslogic/androidx-colorpicker) [![Release](https://jitpack.io/v/syslogic/androidx-colorpicker.svg)](https://jitpack.io/#io.syslogic/androidx-colorpicker)
