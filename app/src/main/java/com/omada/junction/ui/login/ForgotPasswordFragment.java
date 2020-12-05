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
import com.omada.junction.databinding.LoginForgotPasswordFragmentLayoutBinding;
import com.omada.junction.viewmodels.LoginViewModel;

public class ForgotPasswordFragment extends Fragment {

    public static ForgotPasswordFragment newInstance() {
        
        Bundle args = new Bundle();

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        LoginForgotPasswordFragmentLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.login_forgot_password_fragment_layout, container, false);
        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();

    }
}
