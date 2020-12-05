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
import com.omada.junction.databinding.LoginConfirmFragmentLayoutBinding;
import com.omada.junction.viewmodels.LoginViewModel;

public class ConfirmFragment extends Fragment {

    private LoginViewModel loginViewModel;

    public static Fragment getInstance() {
        return new ConfirmFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        LoginConfirmFragmentLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.login_confirm_fragment_layout, container, false);

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
