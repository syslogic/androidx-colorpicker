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

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.DialogFragment;

import io.syslogic.colorpicker.databinding.DialogColorPickerBinding;

/**
 * The Color-Picker {@link DialogFragment}
 *
 * @author Martin Zeitler
 */
public abstract class ColorPickerDialogFragment extends DialogFragment implements
        ViewTreeObserver.OnGlobalLayoutListener,
        ColorPickerView.OnColorChangedListener,
        View.OnClickListener {

    private ColorStateList mHexDefaultTextColor;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private boolean mAlphaSliderEnabled = false;
    private boolean mHexValueEnabled = false;
    private int mOrientation;

    /**
     * Data-Binding
     * @hidden
     */
    DialogColorPickerBinding mDataBinding;

    public ColorPickerDialogFragment() {
        super();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.mOrientation = requireActivity().getResources().getConfiguration().orientation;
        this.mDataBinding = DialogColorPickerBinding.inflate(inflater, container, false);
        this.mDataBinding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(this);
        requireActivity().getWindow().setFormat(PixelFormat.RGBA_8888);

        this.mDataBinding.hexadecimalValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.mHexDefaultTextColor = this.mDataBinding.hexadecimalValue.getTextColors();

        /* The arguments of DialogFragment aren't known in the constructor. */
        if (getArguments() != null) {
            int initialColor = getArguments().getInt("initialColor", Color.BLACK);
            this.mAlphaSliderEnabled = getArguments().getBoolean("alphaSlider", false);
            this.mHexValueEnabled = getArguments().getBoolean("hexValue", false);
            setUp(initialColor);
        }

        this.mDataBinding.hexadecimalValue.setOnEditorActionListener((view, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                if (mDataBinding.hexadecimalValue.getText() != null) {
                    String s = mDataBinding.hexadecimalValue.getText().toString();
                    try {
                        int c = ColorPickerPreference.convertToColorInt(s);
                        mDataBinding.colorPickerView.setColor(c, true);
                        mDataBinding.hexadecimalValue.setTextColor(mHexDefaultTextColor);
                    } catch (IllegalArgumentException e) {
                        mDataBinding.hexadecimalValue.setTextColor(Color.RED);
                    }
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

        this.mDataBinding.oldColorPanel.setOnClickListener(this);
        this.mDataBinding.newColorPanel.setOnClickListener(this);
        this.mDataBinding.colorPickerView.setOnColorChangedListener(this);

        return this.mDataBinding.getRoot();
    }

    protected void setUp(int color) {
        this.mDataBinding.oldColorPanel.setColor(color);
        this.mDataBinding.colorPickerView.setColor(color, true);
    }

    @Override
    public void onGlobalLayout() {
        if (requireContext().getResources().getConfiguration().orientation != mOrientation) {
            int oldColor = this.mDataBinding.oldColorPanel.getColor();
            int newColor = this.mDataBinding.newColorPanel.getColor();
            this.mDataBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
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
            this.mDataBinding.hexadecimalValue.setVisibility(View.VISIBLE);
            updateHexLengthFilter();
            updateHexValue(getColor());
        } else {
            this.mDataBinding.hexadecimalValue.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("unused")
    public boolean getHexValueEnabled() {
        return mHexValueEnabled;
    }

    private void updateHexLengthFilter() {
        if (getAlphaSliderVisible()) {
            this.mDataBinding.hexadecimalValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        } else {
            this.mDataBinding.hexadecimalValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
        }
    }

    private void updateHexValue(int color) {
        if (getAlphaSliderVisible()) {
            this.mDataBinding.hexadecimalValue.setText(ColorPickerPreference.convertToARGB(color).toUpperCase(Locale.getDefault()));
        } else {
            this.mDataBinding.hexadecimalValue.setText(ColorPickerPreference.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
        this.mDataBinding.hexadecimalValue.setTextColor(mHexDefaultTextColor);
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

    public int getColor() {
        return this.mDataBinding.colorPickerView.getColor();
    }

    /**
     * The FragmentResult will be returned to the parent fragment,
     * when the color selected by the user has been confirmed.
     */
    @Override
    public void onClick(@NonNull View view) {
        if (view.getId() == R.id.new_color_panel) {
            Bundle bundle = new Bundle();
            bundle.putInt("color", this.mDataBinding.newColorPanel.getColor());
            getParentFragmentManager().setFragmentResult("colorpicker", bundle);
            dismiss();
        }
    }
}
