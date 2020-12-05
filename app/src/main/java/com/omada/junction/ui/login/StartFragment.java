package com.omada.junction.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.omada.junction.R;
import com.omada.junction.databinding.LoginStartFragmentLayoutBinding;
import com.omada.junction.viewmodels.LoginViewModel;

public class StartFragment extends Fragment {

    public static StartFragment newInstance() {

        Bundle args = new Bundle();

        StartFragment fragment = new StartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        LoginStartFragmentLayoutBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.login_start_fragment_layout, container, false);

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
