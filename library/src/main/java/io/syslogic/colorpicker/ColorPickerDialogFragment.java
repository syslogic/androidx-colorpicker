package io.syslogic.colorpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;

import io.syslogic.colorpicker.databinding.DialogColorPickerBinding;

/**
 * The ColorPicker {@link DialogFragment}
 *
 * @author Martin Zeitler
 */
public abstract class ColorPickerDialogFragment extends DialogFragment implements ColorPickerView.OnColorChangedListener, View.OnClickListener, ViewTreeObserver.OnGlobalLayoutListener {

    private boolean mHexValueEnabled = false;
    private ColorStateList mHexDefaultTextColor;
    private OnColorChangedListener mListener;
    private int mOrientation;

    /** Data-Binding */
    DialogColorPickerBinding mDataBinding;

    private View mLayout;

    public ColorPickerDialogFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public ColorPickerDialogFragment(Context context, int initialColor) {
        super();
        requireActivity().getWindow().setFormat(PixelFormat.RGBA_8888);
        setUp(initialColor);
    }

    public interface OnColorChangedListener {
        void onColorChanged(int color);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mDataBinding = DialogColorPickerBinding.inflate(inflater, container, false);
        return this.mLayout = this.mDataBinding.getRoot();
    }

    protected void setUp(int color) {

        this.mOrientation = requireActivity().getResources().getConfiguration().orientation;
        this.mLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        this.mDataBinding.hexVal.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        mHexDefaultTextColor = this.mDataBinding.hexVal.getTextColors();
        this.mDataBinding.hexVal.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                String s = mDataBinding.hexVal.getText().toString();
                try {
                    int c = ColorPickerPreference.convertToColorInt(s);
                    mDataBinding.colorPickerView.setColor(c, true);
                    mDataBinding.hexVal.setTextColor(mHexDefaultTextColor);
                } catch (IllegalArgumentException e) {
                    mDataBinding.hexVal.setTextColor(Color.RED);
                }
                return true;
            }
            return false;
        });

        ((LinearLayoutCompat) this.mDataBinding.oldColorPanel.getParent()).setPadding(
                Math.round(this.mDataBinding.colorPickerView.getDrawingOffset()),
                0,
                Math.round(this.mDataBinding.colorPickerView.getDrawingOffset()),
                0
        );

        this.mDataBinding.oldColorPanel.setColor(color);
        this.mDataBinding.oldColorPanel.setOnClickListener(this);
        this.mDataBinding.newColorPanel.setOnClickListener(this);
        this.mDataBinding.colorPickerView.setOnColorChangedListener(this);
        this.mDataBinding.colorPickerView.setColor(color, true);
    }

    @Override
    public void onGlobalLayout() {
        if (requireContext().getResources().getConfiguration().orientation != mOrientation) {
            int oldColor = this.mDataBinding.oldColorPanel.getColor();
            int newColor = this.mDataBinding.newColorPanel.getColor();
            mLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            setUp(oldColor);
            this.mDataBinding.newColorPanel.setColor(newColor);
            this.mDataBinding.colorPickerView.setColor(newColor);
        }
    }

    @Override
    public void onColorChanged(int color) {
        this.mDataBinding.newColorPanel.setColor(color);
        if (mHexValueEnabled) {updateHexValue(color);}
    }

    @SuppressWarnings("unused")
    void setHexValueEnabled(boolean enable) {
        mHexValueEnabled = enable;
        if (enable) {
            this.mDataBinding.hexVal.setVisibility(View.VISIBLE);
            updateHexLengthFilter();
            updateHexValue(getColor());
        } else {
            this.mDataBinding.hexVal.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("unused")
    public boolean getHexValueEnabled() {
        return mHexValueEnabled;
    }

    private void updateHexLengthFilter() {
        if (getAlphaSliderVisible()) {
            this.mDataBinding.hexVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        } else {
            this.mDataBinding.hexVal.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        }
    }

    private void updateHexValue(int color) {
        if (getAlphaSliderVisible()) {
            this.mDataBinding.hexVal.setText(ColorPickerPreference.convertToARGB(color).toUpperCase(Locale.getDefault()));
        } else {
            this.mDataBinding.hexVal.setText(ColorPickerPreference.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
        this.mDataBinding.hexVal.setTextColor(mHexDefaultTextColor);
    }

    @SuppressWarnings("unused")
    void setAlphaSliderVisible(boolean visible) {
        this.mDataBinding.colorPickerView.setAlphaSliderVisible(visible);
        if (mHexValueEnabled) {
            updateHexLengthFilter();
            updateHexValue(getColor());
        }
    }

    private boolean getAlphaSliderVisible() {
        return this.mDataBinding.colorPickerView.getAlphaSliderVisible();
    }

    /**
     * The OnColorChangedListener will get notified when the color selected by the user has changed.
     *
     * @param listener to be used for callbacks.
     */
    @SuppressWarnings("unused")
    void setOnColorChangedListener(OnColorChangedListener listener) {
        mListener = listener;
    }

    public int getColor() {
        return this.mDataBinding.colorPickerView.getColor();
    }

    @Override
    public void onClick(@NonNull View view) {
        if (view.getId() == R.id.new_color_panel) {
            if (mListener != null) {
                mListener.onColorChanged(
                        this.mDataBinding.newColorPanel.getColor()
                );
            }
        }
        dismiss();
    }
}
