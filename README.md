# androidx-colorpicker
A simple color-picker library for Android, which provides these classes:

 - [`ColorPickerDialogFragment`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/ColorPickerDialogFragment.java)
 - [`ColorPickerPreference`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/ColorPickerPreference.java)

One can either depend on the local `:library` module:

    dependencies {
        implementation project(path: ':library')
    }

Or depend on JitPack `maven { url 'https://jitpack.io' }`; either by version tag or by `master-SNAPSHOT`:

    dependencies {
        implementation "io.syslogic:androidx-colorpicker:1.0.4"
    }

[![](https://jitci.com/gh/syslogic/androidx-colorpicker/svg)](https://jitci.com/gh/syslogic/androidx-colorpicker) [![Release](https://jitpack.io/v/syslogic/androidx-colorpicker.svg)](https://jitpack.io/#io.syslogic/androidx-colorpicker)
