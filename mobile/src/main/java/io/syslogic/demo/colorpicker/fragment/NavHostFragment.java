package io.syslogic.demo.colorpicker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.syslogic.demo.colorpicker.R;
import io.syslogic.demo.colorpicker.databinding.FragmentNavHostBinding;

/**
 * NavHost Fragment
 *
 * @author Martin Zeitler
 */
public class NavHostFragment extends Fragment {

    /** Kept for reference */
    @SuppressWarnings("unused")
    private static final int resId = R.layout.fragment_nav_host;

    /** Data-Binding */
    FragmentNavHostBinding mDataBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mDataBinding = FragmentNavHostBinding.inflate(inflater, container, false);
        return this.mDataBinding.getRoot();
    }
}
