package io.syslogic.demo.colorpicker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.syslogic.demo.colorpicker.R;
import io.syslogic.demo.colorpicker.databinding.FragmentNavhostBinding;

/**
 * Navhost Fragment
 * @author Martin Zeitler
 */
public class NavhostFragment extends Fragment {

    /** Log Tag */
    protected static final String LOG_TAG = NavhostFragment.class.getSimpleName();

    /** Kept for reference */
    private static final int resId = R.layout.fragment_navhost;

    /** Data-Binding */
    private FragmentNavhostBinding mDataBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mDataBinding = FragmentNavhostBinding.inflate(inflater, container, false);
        return this.mDataBinding.getRoot();
    }
}
