package io.syslogic.demo.colorpicker.fragment;

import android.content.Context;

import io.syslogic.colorpicker.ColorPickerDialogFragment;

/**
 * ColorPickerDialogFragment Impl
 *
 * @author Martin Zeitler
 */
public class ColorPickerDialogFragmentImpl extends ColorPickerDialogFragment {

    public ColorPickerDialogFragmentImpl() {
        super();
    }

    public ColorPickerDialogFragmentImpl(Context context, int initialColor) {
        super();
        setUp(initialColor);
    }
}
