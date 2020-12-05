package com.omada.junction.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.omada.junction.R;
import com.omada.junction.databinding.LoginDetailsFragmentLayoutBinding;
import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.utils.transform.TransformUtilities;
import com.omada.junction.viewmodels.LoginViewModel;

public class DetailsFragment extends Fragment {

    private LoginDetailsFragmentLayoutBinding binding;

    public static DetailsFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, R.layout.login_details_fragment_layout, container, false);

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.getViewModel()
                .getDataValidationAction()
                .observe(getViewLifecycleOwner(), dataValidationInformationLiveEvent -> {

                    if(dataValidationInformationLiveEvent.getData() == null){
                        return;
                    }

                    DataValidator.DataValidationInformation dataValidationInformation = dataValidationInformationLiveEvent.getDataOnceAndReset();

                    switch (dataValidationInformation.getValidationPoint()){
                        case VALIDATION_POINT_EMAIL:
                            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                                binding.emailLayout.setError("Invalid email");
                            }
                            break;
                        case VALIDATION_POINT_GENDER:
                            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                                binding.genderLayout.setError("Invalid gender");
                            }
                            break;
                        case VALIDATION_POINT_PASSWORD:
                            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                                binding.passwordLayout.setError("Invalid password");
                            }
                            break;
                        case VALIDATION_POINT_DATE_OF_BIRTH:
                            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                                binding.dateOfBirthLayout.setError("Please ensure formatting");
                            }
                            break;
                        case VALIDATION_POINT_NAME:
                            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                                binding.nameLayout.setError("Invalid name");
                            }
                            break;
                        case VALIDATION_POINT_INSTITUTE:
                            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                                binding.emailLayout.setError("Invalid institute");
                            }
                            break;
                        case VALIDATION_POINT_INTERESTS:
                            break;
                        case VALIDATION_POINT_ALL:
                            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                                binding.nextButton.setEnabled(false);

                                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.nextButton.getWindowToken(), 0);
                                binding.nextButton.setEnabled(false);

                            }
                            break;
                    }

                });

        binding.getViewModel()
                .getAuthResultAction()
                .observe(getViewLifecycleOwner(), authStatusLiveEvent -> {

                    if(authStatusLiveEvent.getData() == null){
                        return;
                    }
                    switch (authStatusLiveEvent.getData()){
                        case SIGNUP_FAILURE:
                        case ADD_EXTRA_DETAILS_FAILURE:
                            Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_LONG).show();
                            binding.nextButton.setEnabled(true);
                            break;
                    }
                });

        binding.nextButton.setOnClickListener(v->{

            binding.emailLayout.setError("");
            binding.genderLayout.setError("");
            binding.passwordLayout.setError("");
            binding.dateOfBirthLayout.setError("");
            binding.nameLayout.setError("");
            binding.getViewModel().detailsEntryDone();
        });

        binding.emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.emailLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.nameLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.instituteInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.instituteLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.dateOfBirthInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.dateOfBirthLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.genderInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.genderLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        String[] GENDERS = new String[] {"Male", "Female", "Other"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.drop_down_menu_layout,
                        GENDERS);

        binding.genderInput.setAdapter(adapter);

        binding.passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                binding.passwordLayout.setErrorEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        binding.dateOfBirthLayout.setEndIconOnClickListener(v -> {

            MaterialDatePicker.Builder<?> builder = binding.getViewModel().setupDateSelectorBuilder();
            CalendarConstraints.Builder constraintsBuilder = binding.getViewModel().setupConstraintsBuilder();
            builder.setTheme(R.style.ThemeOverlay_MaterialComponents_MaterialCalendar);
            builder.setTitleText("Your Date of Birth");

            try {
                builder.setCalendarConstraints(constraintsBuilder.build());
                MaterialDatePicker<?> picker = builder.build();

                picker.addOnPositiveButtonClickListener(selection -> binding.dateOfBirthInput.setText(
                        TransformUtilities.convertMillisecondsToDDMMYYYY((Long)selection, "/")
                ));

                picker.show(getChildFragmentManager(), picker.toString());
            }
            catch (Exception e){
                Log.e("CALENDAR", "error");
            }
        });

    }
}
