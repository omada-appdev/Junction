package com.omada.junction.ui.login;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.omada.junction.R;
import com.omada.junction.databinding.LoginForgotPasswordFragmentLayoutBinding;
import com.omada.junction.viewmodels.LoginViewModel;

public class ForgotPasswordFragment extends Fragment {

    private LoginForgotPasswordFragmentLayoutBinding binding;
    LoginViewModel loginViewModel;

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

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.login_forgot_password_fragment_layout, container, false);
        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getLinkButton.setOnClickListener(v -> {
            loginViewModel.sendPasswordResetLink().observe(getViewLifecycleOwner(), booleanLiveEvent -> {
                if (booleanLiveEvent == null) {
                    return;
                }
                Boolean result = booleanLiveEvent.getDataOnceAndReset();
                if (result == null) {
                    return;
                }
                if (result) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Password reset")
                            .setMessage("A password reset link has been sent to " + binding.emailInput.getText().toString())
                            .setPositiveButton("OK", (dialog, which) -> {
                                requireActivity().onBackPressed();
                            })
                            .create()
                            .show();
                } else {
                    Toast.makeText(requireContext(), "Please ensure the email is correct", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
