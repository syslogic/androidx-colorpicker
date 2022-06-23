package io.syslogic.demo.colorpicker.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import io.syslogic.demo.colorpicker.R;
import io.syslogic.demo.colorpicker.databinding.FragmentHomeBinding;

/**
 * Home Fragment
 *
 * @author Martin Zeitler
 */
public class HomeFragment extends Fragment implements FragmentResultListener {

    /** Kept for reference */
    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_home;

    /** Data-Binding */
    FragmentHomeBinding mDataBinding;

    /** Preferences */
    SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("colorpicker", requireActivity(), this);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.mDataBinding = FragmentHomeBinding.inflate(inflater, container, false);
        int initialColor = this.prefs.getInt("color_code_01", Color.DKGRAY);
        this.mDataBinding.layoutHome.setBackgroundColor(initialColor);

        /* Navigating to ColorPickerDialogFragment */
        this.mDataBinding.buttonDialog.setOnClickListener(view -> {
            NavDirections action = HomeFragmentDirections
                    .actionHomeFragmentToColorPickerDialogFragment()
                    .setInitialColor(getBackgroundColor(mDataBinding.layoutHome))
                    .setAlphaSlider(false)
                    .setHexValue(false);

            Navigation.findNavController(view).navigate(action);
        });

        /* Navigating to PreferencesFragment */
        this.mDataBinding.buttonPreferences.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(HomeFragmentDirections
                        .actionMainFragmentToPreferencesFragment()
                ));

        return this.mDataBinding.getRoot();
    }

    private int getBackgroundColor(@NonNull View view) {
        Drawable drawable = view.getBackground();
        if (drawable != null) {

            /* Obtain the current drawable, if required. */
            if(drawable instanceof StateListDrawable) {
                drawable = drawable.getCurrent();
            }

            /* Return the color's value, if possible. */
            if (drawable instanceof ColorDrawable) {
                return ((ColorDrawable) drawable).getColor();
            }
        }
        return Color.BLACK;
    }

    /**
     * Callback used to handle results passed between fragments.
     * The result of ColorPickerDialogFragment is being set as background color.
     *
     * @param requestKey key used to store the result
     * @param result     result passed to the callback
     */
    @Override
    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
        if (requestKey.equals("colorpicker")) {
            this.mDataBinding.layoutHome.setBackgroundColor(result.getInt("color"));
        }
    }
}
