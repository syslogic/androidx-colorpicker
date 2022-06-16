# androidx-colorpicker
A simple color-picker library for Android, which works with Navigation Component.<br/>
It's generally available on JitPack; either by version tag or `master-SNAPSHOT`.<br/>
The JitPack repository URL needs to be added: `maven { url 'https://jitpack.io' }`

    dependencies {
        implementation "io.syslogic:androidx-colorpicker:1.0.5"
    }

Alternatively, one can also depend on the local `:library` module:

    dependencies {
        implementation project(path: ':library')
    }

It provides these classes:

 - [`ColorPickerDialogFragment`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/ColorPickerDialogFragment.java)
 - [`ColorPickerPreference`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/ColorPickerPreference.java)

Usage examples:

 - [`ColorPickerDialogFragmentImpl.java`](https://github.com/syslogic/androidx-colorpicker/blob/master/mobile/src/main/java/io/syslogic/demo/colorpicker/fragment/ColorPickerDialogFragmentImpl.java)
 - [`preferences.xml`](https://github.com/syslogic/androidx-colorpicker/blob/master/mobile/src/main/res/xml/preferences.xml)

And looks about like this:

  ![Screenshot 01](https://raw.githubusercontent.com/syslogic/androidx-colorpicker/master/screenshots/screenshot_01.png)

[![](https://jitci.com/gh/syslogic/androidx-colorpicker/svg)](https://jitci.com/gh/syslogic/androidx-colorpicker) [![Release](https://jitpack.io/v/syslogic/androidx-colorpicker.svg)](https://jitpack.io/#io.syslogic/androidx-colorpicker)

