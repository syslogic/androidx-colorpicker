package io.syslogic.demo.colorpicker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.Navigation;

import io.syslogic.demo.colorpicker.BuildConfig;
import io.syslogic.demo.colorpicker.R;
import io.syslogic.demo.colorpicker.databinding.FragmentHomeBinding;

/**
 * Home Fragment
 *
 * @author Martin Zeitler
 */
public class HomeFragment extends Fragment implements FragmentResultListener {

    /** Log Tag */
    @SuppressWarnings("unused")
    protected static final String LOG_TAG = HomeFragment.class.getSimpleName();

    /** Debug Output */
    @SuppressWarnings("unused")
    protected static final boolean mDebug = BuildConfig.DEBUG;

    /** Kept for reference */
    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_home;

    /** Data-Binding */
    FragmentHomeBinding mDataBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getParentFragmentManager().setFragmentResultListener("colorpicker", requireActivity(), this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mDataBinding = FragmentHomeBinding.inflate(inflater, container, false);
        this.mDataBinding.buttonPreferences.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(
                        HomeFragmentDirections.actionMainFragmentToPreferencesFragment()
                ));
        this.mDataBinding.buttonDialog.setOnClickListener(view ->
                Navigation.findNavController(view).navigate(
                        HomeFragmentDirections.actionHomeFragmentToColorPickerDialogFragment()
                ));
        return this.mDataBinding.getRoot();
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
