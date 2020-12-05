package com.omada.junction.ui.home.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.omada.junction.R;

public class CompeteFragment extends Fragment {

    public static CompeteFragment newInstance() {

        Bundle args = new Bundle();

        CompeteFragment fragment = new CompeteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup contents = (ViewGroup) inflater.inflate(R.layout.feed_compete_fragment_layout, container, false);
        return contents;
    }
}
