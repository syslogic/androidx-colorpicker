package io.syslogic.colorpicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.Contract;

/**
 * The Color-Picker {@link Preference}
 *
 * @author Martin Zeitler
 */
public class ColorPickerPreference extends Preference implements
        Preference.OnPreferenceChangeListener,
        Preference.OnPreferenceClickListener,
        OnColorChangedListener {

    /** {@link Log} Tag */
    private static final String LOG_TAG = ColorPickerPreference.class.getSimpleName();

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;
    
    private int mCurrentValue = Color.BLACK;
    private boolean mShowAlphaSlider = false;
    private boolean mShowHexValue = false;
    
    private ColorPickerDialog mDialog;
    private SharedPreferences prefs;

    @SuppressWarnings("unused")
    public ColorPickerPreference(Context context) {
        super(context);
        init(context, null);
    }

    @SuppressWarnings("unused")
    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    @SuppressWarnings("unused")
    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(@NonNull Context context, AttributeSet attrs) {

        if (attrs != null) {
            this.mShowAlphaSlider = attrs.getAttributeBooleanValue(null, "alphaSlider", false);
            this.mShowHexValue = attrs.getAttributeBooleanValue(null, "hexValue", false);
        }

        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mCurrentValue = this.prefs.getInt(this.getKey(), this.mCurrentValue);
        if(mDebug) {
            Log.d(LOG_TAG, String.format("%s: %s", this.getKey(), convertToARGB(this.mCurrentValue)));
        }
        this.setOnPreferenceClickListener(this);
    }

    /**
     * Accept hex string and resources reference string (eg. @color/someColor) as defaultValue.
     *
     * @param a resource typed array
     * @param index the index to get
     */
    @Override
    protected Object onGetDefaultValue(@NonNull TypedArray a, int index) {
        int colorInt;
        String mHexDefaultValue = a.getString(index);
        if (mHexDefaultValue != null && mHexDefaultValue.startsWith("#")) {
            colorInt = convertToColorInt(mHexDefaultValue);
            return colorInt;
        } else {
            return a.getColor(index, this.mCurrentValue);
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onSetInitialValue(Object defaultValue) {
        if(defaultValue != null) {
            this.onColorChanged((Integer) defaultValue);
            super.onSetInitialValue(defaultValue);
        } else {
            this.mCurrentValue = this.prefs.getInt(this.getKey(), this.mCurrentValue);
            this.onColorChanged(this.mCurrentValue);
            super.onSetInitialValue(this.mCurrentValue);
        }
    }

    @Override
    public boolean onPreferenceClick(@NonNull Preference preference) {
        this.mDialog = new ColorPickerDialog(getContext(), this.mCurrentValue);
        if (this.mShowAlphaSlider) {this.mDialog.setAlphaSliderVisible(true);}
        if (this.mShowHexValue) {this.mDialog.setHexValueEnabled(true);}
        this.mDialog.setOnColorChangedListener((OnColorChangedListener) this);
        this.mDialog.show();
        return false;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean onPreferenceChange(@NonNull Preference preference, @NonNull Object newValue) {
        this.mCurrentValue = Integer.parseInt(newValue.toString());
        this.setSummary(convertToARGB(this.mCurrentValue));
        if (preference.getIcon() instanceof BitmapDrawable) {
            BitmapDrawable drawable = (BitmapDrawable) preference.getIcon();
            drawable.setTintMode(PorterDuff.Mode.ADD);
            drawable.setTint(this.mCurrentValue);
        } else if (preference.getIcon() instanceof VectorDrawable) {
            VectorDrawable drawable = (VectorDrawable) preference.getIcon();
            drawable.setTintMode(PorterDuff.Mode.ADD);
            drawable.setTint(this.mCurrentValue);
        } else if(mDebug) {
            Log.e(LOG_TAG, "onPreferenceChange: neither BitmapDrawable nor VectorDrawable");
        }
        this.prefs.edit().putInt(preference.getKey(), this.mCurrentValue).apply();
        return false;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onColorChanged(int color) {
        this.mCurrentValue = color;
        try {
            this.onPreferenceChange(this, color);
        } catch (NullPointerException e) {
            if(mDebug) {Log.e(LOG_TAG, String.format("%s", e.getMessage()));}
        }
    }

    /** it crashes the preferences screen */
    private void showDialog(Bundle state) {
        this.mDialog = new ColorPickerDialog(getContext(), this.mCurrentValue);
        this.mDialog.setOnColorChangedListener((OnColorChangedListener) this);
        if (this.mShowAlphaSlider) {this.mDialog.setAlphaSliderVisible(true);}
        if (this.mShowHexValue) {this.mDialog.setHexValueEnabled(true);}
        if (state != null) {this.mDialog.onRestoreInstanceState(state);}
        this.mDialog.show();
    }

    /**
     * Toggle Alpha Slider visibility (by default it's disabled).
     *
     * @param value true or false.
     */
    @SuppressWarnings("unused")
    public void setAlphaSliderEnabled(boolean value) {
        this.mShowAlphaSlider = value;
    }

    /**
     * Toggle Hex-Value visibility (by default it's disabled).
     *
     * @param value true or false.
     */
    @SuppressWarnings("unused")
    public void setHexValueEnabled(boolean value) {
        this.mShowHexValue = value;
    }

    /**
     * @param color the color value to convert.
     */
    @NonNull
    static String convertToARGB(int color) {
        String alpha = Integer.toHexString(Color.alpha(color)).toUpperCase();
        String red   = Integer.toHexString(Color.red(color)).toUpperCase();
        String green = Integer.toHexString(Color.green(color)).toUpperCase();
        String blue  = Integer.toHexString(Color.blue(color)).toUpperCase();
        if (alpha.length() == 1) {alpha = String.format("0%s", alpha);}
        if (red.length()   == 1) {red   = String.format("0%s", red);}
        if (green.length() == 1) {green = String.format("0%s", green);}
        if (blue.length()  == 1) {blue  = String.format("0%s", blue);}
        return String.format("#%s%s%s%s", alpha, red, green, blue);
    }

    /**
     * Method currently used by onGetDefaultValue method to convert hex string provided in android:defaultValue to color integer.
     *
     * @param color the color value to convert.
     * @return A string representing the hex value of color without the alpha value
     */
    @NonNull
    static String convertToRGB(int color) {
        String red = Integer.toHexString(Color.red(color)).toUpperCase();
        String green = Integer.toHexString(Color.green(color)).toUpperCase();
        String blue = Integer.toHexString(Color.blue(color)).toUpperCase();
        if (red.length()   == 1) {red   = String.format("0%s", red);}
        if (green.length() == 1) {green = String.format("0%s", green);}
        if (blue.length()  == 1) {blue  = String.format("0%s", blue);}
        return String.format("#%s%s%s", red, green, blue);
    }

    /**
     * For custom purposes.
     * Not used by ColorPickerPreference.
     *
     * @param argb the ARGB string to convert.
     */
    static int convertToColorInt(@NonNull String argb) {
        if (! argb.startsWith("#")) {argb = String.format("#%s", argb);}
        return Color.parseColor(argb);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        if (this.mDialog == null || !this.mDialog.isShowing()) {return superState;}
        final SavedState myState = new SavedState(superState);
        myState.dialogBundle = this.mDialog.onSaveInstanceState();
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            // didn't save state in onSaveInstanceState
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        showDialog(myState.dialogBundle);
    }

    private static class SavedState extends BaseSavedState {

        Bundle dialogBundle;
        SavedState(Parcel source) {
            super(source);
            this.dialogBundle = source.readBundle(getClass().getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBundle(this.dialogBundle);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @NonNull @Contract("_ -> new")
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @NonNull @Contract(value = "_ -> new", pure = true)
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
