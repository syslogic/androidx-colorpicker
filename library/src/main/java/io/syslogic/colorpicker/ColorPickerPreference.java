package io.syslogic.colorpicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;

import androidx.preference.Preference;

import androidx.preference.PreferenceManager;

/**
 * The ColorPicker {@link Preference}.
 * @author Martin Zeitler
 */
public class ColorPickerPreference extends Preference implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener, ColorPickerDialog.OnColorChangedListener {

    /** {@link Log} Tag */
    private static final String LOG_TAG = ColorPickerPreference.class.getSimpleName();

    /** Debug Output */
    protected static final boolean mDebug = BuildConfig.DEBUG;

    private ColorPickerDialog mDialog;

    private boolean mAlphaSliderEnabled = false;
    private boolean mHexValueEnabled    = false;

    private int mValue = Color.BLACK;

    private float mDensity = 0;

    private SharedPreferences sharedPrefs;

    public ColorPickerPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ColorPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.mValue = this.sharedPrefs.getInt(this.getKey(), this.mValue);

        if(mDebug) {
            Log.d(LOG_TAG, this.getKey() + ": " + convertToARGB(this.mValue));
        }

        this.setOnPreferenceClickListener(this);

        if (attrs != null) {
            this.mAlphaSliderEnabled = attrs.getAttributeBooleanValue(null, "alphaSlider", false);
            this.mHexValueEnabled = attrs.getAttributeBooleanValue(null, "hexValue", false);
        }
    }

    /**
     * accept hex string and resources reference string (eg. @color/someColor) as defaultValue.
     * @param a
     * @param index
    **/
    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        int colorInt;
        String mHexDefaultValue = a.getString(index);
        if (mHexDefaultValue != null && mHexDefaultValue.startsWith("#")) {
            colorInt = convertToColorInt(mHexDefaultValue);
            return colorInt;
        } else {
            return a.getColor(index, this.mValue);
        }
    }

    /** uses the new api */
    @Override
    protected void onSetInitialValue(Object defaultValue) {
        if(defaultValue != null) {
            this.onColorChanged((Integer) defaultValue);
            super.onSetInitialValue(defaultValue);
        } else {
            this.mValue = this.sharedPrefs.getInt(this.getKey(), this.mValue);
            this.onColorChanged(this.mValue);
            super.onSetInitialValue(this.mValue);
        }
    }

    /** a fixed version of the function */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        this.mDialog = new ColorPickerDialog(getContext(), this.mValue);
        if (this.mAlphaSliderEnabled) {this.mDialog.setAlphaSliderVisible(true);}
        if (this.mHexValueEnabled) {this.mDialog.setHexValueEnabled(true);}
        this.mDialog.setOnColorChangedListener(this);
        this.mDialog.show();
        return false;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        this.mValue = Integer.parseInt(newValue.toString());
        this.setSummary(convertToARGB(this.mValue));

        if (preference.getIcon() instanceof BitmapDrawable) {
            BitmapDrawable drawable = (BitmapDrawable) preference.getIcon();
            drawable.setTintMode(PorterDuff.Mode.ADD);
            drawable.setTint(this.mValue);
        } else if (preference.getIcon() instanceof VectorDrawable) {
            VectorDrawable drawable = (VectorDrawable) preference.getIcon();
            drawable.setTintMode(PorterDuff.Mode.ADD);
            drawable.setTint(this.mValue);
        } else if(mDebug) {
            Log.e(LOG_TAG, "onPreferenceChange: neither BitmapDrawable nor VectorDrawable");
        }

        this.sharedPrefs.edit().putInt(preference.getKey(), this.mValue).apply();
        return false;
    }

    @Override
    public void onColorChanged(int color) {
        this.mValue = color;
        try {
            this.onPreferenceChange(this, color);
        } catch (NullPointerException e) {
            if(mDebug) {Log.e(LOG_TAG, "" + e.getMessage());}
        }
    }

    /** it crashes the preferences screen */
    private void showDialog(Bundle state) {
        this.mDialog = new ColorPickerDialog(getContext(), this.mValue);
        this.mDialog.setOnColorChangedListener(this);
        if (this.mAlphaSliderEnabled) {this.mDialog.setAlphaSliderVisible(true);}
        if (this.mHexValueEnabled) {this.mDialog.setHexValueEnabled(true);}
        if (state != null) {this.mDialog.onRestoreInstanceState(state);}
        this.mDialog.show();
    }

    /**
     * toggle Alpha Slider visibility (by default it's disabled)
     * @param value
    **/
    public void setAlphaSliderEnabled(boolean value) {
        this.mAlphaSliderEnabled = value;
    }

    /**
     * toggle Hex Value visibility (by default it's disabled)
     * @param value
    **/
    public void setHexValueEnabled(boolean value) {
        this.mHexValueEnabled = value;
    }

    /**
     * for custom purposes. Not used by ColorPickerPreference
     * @param color
    **/
    static String convertToARGB(int color) {
        String alpha = Integer.toHexString(Color.alpha(color)).toUpperCase();
        String red   = Integer.toHexString(Color.red(color)).toUpperCase();
        String green = Integer.toHexString(Color.green(color)).toUpperCase();
        String blue  = Integer.toHexString(Color.blue(color)).toUpperCase();
        if (alpha.length() == 1) {alpha = "0" + alpha;}
        if (red.length()   == 1) {red   = "0" + red;}
        if (green.length() == 1) {green = "0" + green;}
        if (blue.length()  == 1) {blue  = "0" + blue;}
        return "#" + alpha + red + green + blue;
    }

    /**
     * Method currently used by onGetDefaultValue method to convert hex string provided in android:defaultValue to color integer.
     * @param color
     * @return A string representing the hex value of color without the alpha value
    **/
    static String convertToRGB(int color) {
        String red = Integer.toHexString(Color.red(color)).toUpperCase();
        String green = Integer.toHexString(Color.green(color)).toUpperCase();
        String blue = Integer.toHexString(Color.blue(color)).toUpperCase();
        if (red.length()   == 1) {red   = "0" + red;}
        if (green.length() == 1) {green = "0" + green;}
        if (blue.length()  == 1) {blue  = "0" + blue;}
        return "#" + red + green + blue;
    }

    /**
     * For custom purposes. Not used by ColorPickerPreference
     * @param argb
     * @throws IllegalArgumentException
    **/
    static int convertToColorInt(String argb) throws IllegalArgumentException {
        if (!argb.startsWith("#")) {argb = "#" + argb;}
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
            // didn't save state for us in onSaveInstanceState
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
            // dialogBundle = source.readBundle();
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
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}