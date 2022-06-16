package io.syslogic.colorpicker;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Locale;

import androidx.appcompat.widget.LinearLayoutCompat;

/**
 * The Color-Picker {@link Dialog} is (still) being used by ColorPickerPreference.
 *
 * @author Martin Zeitler
 */
public class ColorPickerDialog extends Dialog implements OnColorChangedListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private ColorPickerView mColorPicker;
    private ColorPickerPanelView mOldColor;
    private ColorPickerPanelView mNewColor;

    private EditText mHexVal;
    private ColorStateList mHexDefaultTextColor;
    private OnColorChangedListener mListener;
    private int mOrientation;
    private View mLayout;

    private boolean mHexValueEnabled = true;

    @Override
    public void onGlobalLayout() {
        if (getContext().getResources().getConfiguration().orientation != mOrientation) {

            mOrientation = getContext().getResources().getConfiguration().orientation;
            mLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            int oldColor = mOldColor.getColor();
            int newColor = mNewColor.getColor();
            setUp(oldColor);

            mNewColor.setColor(newColor);
            mColorPicker.setColor(newColor);
        }
    }

    ColorPickerDialog(Context context, int initialColor) {
        super(context);
        setUp(initialColor);
    }

    @SuppressLint("InflateParams")
    private void setUp(int initialColor) {

        // To fight color banding.
        getWindow().setFormat(PixelFormat.RGBA_8888);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mLayout = inflater.inflate(R.layout.dialog_color_picker, null);
        this.mLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mOrientation = getContext().getResources().getConfiguration().orientation;
        setContentView(mLayout);
        setTitle(R.string.text_color_picker_dialog);

        mColorPicker = mLayout.findViewById(R.id.color_picker_view);
        mOldColor = this.mLayout.findViewById(R.id.old_color_panel);
        mNewColor = this.mLayout.findViewById(R.id.new_color_panel);

        mHexVal = this.mLayout.findViewById(R.id.hexadecimal_value);
        mHexVal.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mHexDefaultTextColor = mHexVal.getTextColors();

        mHexVal.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String s = mHexVal.getText().toString();
                try {
                    int c = ColorPickerPreference.convertToColorInt(s);
                    mColorPicker.setColor(c, true);
                    mHexVal.setTextColor(mHexDefaultTextColor);
                } catch (IllegalArgumentException e) {
                    mHexVal.setTextColor(Color.RED);
                }
                return true;
            }
            return false;
        });

        ((LinearLayoutCompat) mOldColor.getParent()).setPadding(
                Math.round(mColorPicker.getDrawingOffset()),
                0,
                Math.round(mColorPicker.getDrawingOffset()),
                0
        );

        mOldColor.setOnClickListener(this);
        mNewColor.setOnClickListener(this);
        mColorPicker.setOnColorChangedListener(this);
        mOldColor.setColor(initialColor);
        mColorPicker.setColor(initialColor, true);
    }

    @Override
    public void onColorChanged(int color) {
        mNewColor.setColor(color);
        if (mHexValueEnabled) {updateHexValue(color);}
    }

    void setHexValueEnabled(@SuppressWarnings("SameParameterValue") boolean enable) {
        mHexValueEnabled = enable;
        if (enable) {
            mHexVal.setVisibility(View.VISIBLE);
            updateHexLengthFilter();
            updateHexValue(getColor());
        } else {
            mHexVal.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("unused")
    public boolean getHexValueEnabled() {
        return this.mHexValueEnabled;
    }

    private void updateHexLengthFilter() {
        if (getAlphaSliderVisible()) {
            mHexVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        } else {
            mHexVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        }
    }

    private void updateHexValue(int color) {
        if (getAlphaSliderVisible()) {
            mHexVal.setText(ColorPickerPreference.convertToARGB(color).toUpperCase(Locale.getDefault()));
        } else {
            mHexVal.setText(ColorPickerPreference.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
        mHexVal.setTextColor(mHexDefaultTextColor);
    }

    void setAlphaSliderVisible(@SuppressWarnings("SameParameterValue") boolean visible) {
        mColorPicker.setAlphaSliderVisible(visible);
        if (mHexValueEnabled) {
            updateHexLengthFilter();
            updateHexValue(getColor());
        }
    }

    private boolean getAlphaSliderVisible() {
        return mColorPicker.getAlphaSliderVisible();
    }

    /**
     * The OnColorChangedListener will get notified when the color selected by the user has changed.
     *
     * @param listener to be used for callbacks.
     */
    void setOnColorChangedListener(OnColorChangedListener listener) {
        mListener = listener;
    }

    public int getColor() {
        return mColorPicker.getColor();
    }

    @Override
    public void onClick(@NonNull View view) {
        if (view.getId() == R.id.new_color_panel && mListener != null) {
            mListener.onColorChanged(mNewColor.getColor());
        }
        dismiss();
    }

    @NonNull
    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt("old_color", mOldColor.getColor());
        state.putInt("new_color", mNewColor.getColor());
        return state;
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mOldColor.setColor(savedInstanceState.getInt("old_color"));
        mColorPicker.setColor(savedInstanceState.getInt("new_color"), true);
    }
}
