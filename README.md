# androidx-colorpicker

[![](https://jitci.com/gh/syslogic/androidx-colorpicker/svg)](https://jitci.com/gh/syslogic/androidx-colorpicker) [![Release](https://jitpack.io/v/syslogic/androidx-colorpicker.svg)](https://jitpack.io/#io.syslogic/androidx-colorpicker)
[![MIT License](https://img.shields.io/github/license/syslogic/androidx-colorpicker)](https://github.com/syslogic/androidx-colorpicker/blob/master/LICENSE)<!-- @IGNORE PREVIOUS: link -->

A modern color-picker library for Android, which is compatible with Jetpack Navigation and Jetpack Compose.<br/><br/>
The library is generally available on JitPack; either by version tag or `master-SNAPSHOT`.<br/>
The JitPack repository URL would be: `maven { url 'https://jitpack.io' }`

    dependencies {
        implementation "io.syslogic:androidx-colorpicker:1.2.3"
    }

 ---

The `:mobile` application module provides these usage examples:

a) Use [`ColorPickerDialogFragmentImpl`](https://github.com/syslogic/androidx-colorpicker/blob/master/mobile/src/main/java/io/syslogic/demo/colorpicker/fragment/ColorPickerDialogFragmentImpl.java) `extends` [`ColorPickerDialogFragment`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/ColorPickerDialogFragment.java):
````java
class ColorPickerDialogFragmentImpl extends ColorPickerDialogFragment {
    public ColorPickerDialogFragmentImpl() {
        super();
    }
}
````

Then define `ColorPickerDialogFragmentImpl` as navigation destination in [`nav_graph.xml`](https://github.com/syslogic/androidx-colorpicker/blob/master/mobile/src/main/res/navigation/nav_graph.xml):
````xml
<dialog
    android:id="@+id/colorPickerDialogFragment"
    android:name="io.syslogic.demo.colorpicker.fragment.ColorPickerDialogFragmentImpl">
    <argument app:argType="integer" android:name="initialColor" android:defaultValue="-16777216"/>
    <argument app:argType="boolean" android:name="alphaSlider" android:defaultValue="false"/>
    <argument app:argType="boolean" android:name="hexValue" android:defaultValue="false"/>
</dialog>
````
Screenshot `ColorPickerDialogFragment`:

![DialogFragment](https://raw.githubusercontent.com/syslogic/androidx-colorpicker/master/screenshots/screenshot_01.png)

b) [`ColorPickerPreference`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/ColorPickerPreference.java)
can be added as XML node; into eg. [`preferences.xml`](https://github.com/syslogic/androidx-colorpicker/blob/master/mobile/src/main/res/xml/preferences.xml):
````xml
<io.syslogic.colorpicker.ColorPickerPreference
    android:title="@string/text_select_color"
    android:icon="@drawable/ic_baseline_invert_colors"
    android:defaultValue="@string/value_default_color"
    android:key="color_code_01"/>
````

c) Preview: `@Composable fun` [`ColorPickerComponent`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/compose/ColorPickerComponent.kt)

![Composable](https://raw.githubusercontent.com/syslogic/androidx-colorpicker/master/screenshots/screenshot_02.png)

d) Preview: `@Composable fun` [`ColorPickerDialog`](https://github.com/syslogic/androidx-colorpicker/blob/master/library/src/main/java/io/syslogic/colorpicker/compose/ColorPickerDialog.kt)

![Composable](https://raw.githubusercontent.com/syslogic/androidx-colorpicker/master/screenshots/screenshot_03.png)
