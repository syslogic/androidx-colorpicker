package io.syslogic.demo.colorpicker.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
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
        int colorCode01 = this.prefs.getInt("color_code_01", Color.DKGRAY);
        int colorCode02 = this.prefs.getInt("color_code_02", Color.WHITE);
        this.mDataBinding.setColorCode01(colorCode01);
        this.mDataBinding.setColorCode02(colorCode02);

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

        /* Navigating to GitHub Sponsors */
        this.mDataBinding.textGitHubSponsors.setOnClickListener(view -> {
            Uri uri = Uri.parse(requireActivity().getString(R.string.url_git_hub_sponsors));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

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
