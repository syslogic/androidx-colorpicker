package io.syslogic.compose.legacy;

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

import io.syslogic.colorpicker.legacy.R;
import io.syslogic.colorpicker.legacy.databinding.DialogFragmentColorPickerBinding;

/**
 * The Color-Picker {@link DialogFragment} accepts NavArgs and returns a FragmentResult.
 * @author Martin Zeitler
 */
public class ColorPickerDialogFragment extends DialogFragment implements
        ViewTreeObserver.OnGlobalLayoutListener, OnColorChangedListener, View.OnClickListener {

    private OnColorChangedListener listener = null;
    private ColorStateList mHexDefaultTextColor;
    private int mOrientation;

    /**
     * Data-Binding
     * @hidden
     */
    DialogFragmentColorPickerBinding mDataBinding;

    /** Constructor */
    public ColorPickerDialogFragment() {
        super();
    }

    /**
     * Default Constructor
     * @param listener instance of {@link OnColorChangedListener}.
     */
    public ColorPickerDialogFragment(@NonNull OnColorChangedListener listener) {
        super();
        this.listener = listener;
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.mOrientation = requireActivity().getResources().getConfiguration().orientation;
        this.mDataBinding = DialogFragmentColorPickerBinding.inflate(inflater, container, false);
        this.mDataBinding.getRoot().getViewTreeObserver().addOnGlobalLayoutListener(this);
        requireActivity().getWindow().setFormat(PixelFormat.RGBA_8888);

        this.mDataBinding.hexadecimalValue.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        this.mHexDefaultTextColor = this.mDataBinding.hexadecimalValue.getTextColors();

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

        /* The arguments of DialogFragment aren't known in the constructor. */
        if (getArguments() != null) {
            this.setShowHexValue(getArguments().getBoolean("hexValue", false));
            this.setShowAlphaSlider(getArguments().getBoolean("alphaSlider", false));
            this.setUp(getArguments().getInt("initialColor", Color.BLACK));
        }
        return this.mDataBinding.getRoot();
    }

    /** @param color the initial color. */
    protected void setUp(int color) {
        this.mDataBinding.oldColorPanel.setColor(color);
        this.mDataBinding.newColorPanel.setColor(color);
        this.mDataBinding.colorPickerView.setColor(color, true);
    }

    @Override
    public void onGlobalLayout() {
        if (requireContext().getResources().getConfiguration().orientation != mOrientation) {
            int oldColor = this.mDataBinding.oldColorPanel.getColor();
            int newColor = this.mDataBinding.newColorPanel.getColor();
            this.mDataBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
            setUp(oldColor);
            this.mDataBinding.colorPickerView.setColor(newColor);
            this.mDataBinding.newColorPanel.setColor(newColor);
        }
    }

    @Override
    public void onColorChanged(int color) {
        if (this.mDataBinding.getShowHexValue()) {this.updateHexValue(color);}
        this.mDataBinding.newColorPanel.setColor(color);
        if (this.listener != null) {
            this.listener.onColorChanged(color);
        }
    }

    void setShowAlphaSlider(boolean value) {
        if (this.mDataBinding != null) {
            this.mDataBinding.setShowAlphaSlider(value);
        }
        if (value) {
            updateHexLengthFilter();
            updateHexValue(getColor());
        }
    }

    void setShowHexValue(boolean value) {
        if (this.mDataBinding != null) {
            this.mDataBinding.setShowHexValue(value);
        }
        if (value) {
            this.updateHexLengthFilter();
            this.updateHexValue(getColor());
        }
    }

    private void updateHexLengthFilter() {
        if (this.mDataBinding.hexadecimalValue.getVisibility() == View.VISIBLE) {
            this.mDataBinding.hexadecimalValue.setFilters(
                    new InputFilter[] {new InputFilter.LengthFilter(9)}
            );
        } else {
            this.mDataBinding.hexadecimalValue.setFilters(
                    new InputFilter[] {new InputFilter.LengthFilter(7)}
            );
        }
    }

    private void updateHexValue(int color) {
        if (this.mDataBinding.getShowAlphaSlider()) {
            this.mDataBinding.hexadecimalValue.setText(ColorPickerPreference.convertToARGB(color).toUpperCase(Locale.getDefault()));
        } else {
            this.mDataBinding.hexadecimalValue.setText(ColorPickerPreference.convertToRGB(color).toUpperCase(Locale.getDefault()));
        }
        this.mDataBinding.hexadecimalValue.setTextColor(mHexDefaultTextColor);
    }

    /** @return the current integer color. */
    public int getColor() {
        return this.mDataBinding.colorPickerView.getColor();
    }

    /**
     * The FragmentResult will be returned to the parent Fragment,
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
