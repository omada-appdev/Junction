package com.omada.junction.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.omada.junction.R;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.databinding.LoginDetailsFragmentLayoutBinding;
import com.omada.junction.utils.ImageUtilities;
import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.utils.TransformUtilities;
import com.omada.junction.viewmodels.LoginViewModel;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class DetailsFragment extends Fragment {

    // Some arbitrary identifier
    private static final int REQUEST_CODE_PROFILE_PICTURE_CHOOSER = 4;
    private final AtomicBoolean compressingImage = new AtomicBoolean(false);

    private final ActivityResultLauncher<String> storagePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    startFilePicker();
                }
            });

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

                    if(dataValidationInformationLiveEvent == null){
                        return;
                    }

                    DataValidator.DataValidationInformation dataValidationInformation = dataValidationInformationLiveEvent.getDataOnceAndReset();
                    if(dataValidationInformation == null) {
                        return;
                    }

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
                        case VALIDATION_POINT_INSTITUTE_HANDLE:
                            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                                binding.instituteLayout.setError("Invalid institute");
                            }
                            break;
                        case VALIDATION_POINT_INTERESTS:
                            break;
                        case VALIDATION_POINT_ALL:
                            Log.e("Details", "VALIDATION_POINT_ALL");
                            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(binding.nextButton.getWindowToken(), 0);
                            } else {
                                binding.nextButton.setEnabled(true);
                            }
                            break;
                    }

                });

        binding.getViewModel()
                .getAuthResultAction()
                .observe(getViewLifecycleOwner(), authStatusLiveEvent -> {

                    if(authStatusLiveEvent == null){
                        return;
                    }
                    UserDataHandler.AuthStatus authStatus = authStatusLiveEvent.getDataOnceAndReset();
                    if(authStatus == null) {
                        return;
                    }
                    switch (authStatus){
                        case SIGNUP_FAILURE:
                        case ADD_EXTRA_DETAILS_FAILURE:
                            Toast.makeText(requireContext(), "Please try again", Toast.LENGTH_LONG).show();
                            binding.nextButton.setEnabled(true);
                            break;
                    }
                });

        binding.profilePictureImage.setOnClickListener(v -> {

            if(compressingImage.get()){
                Toast.makeText(requireContext(), "Please wait", Toast.LENGTH_SHORT).show();
                return;
            }

            ((ShapeableImageView)v).setStrokeColor(null);
            if (ContextCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                startFilePicker();
            }
            else {
                storagePermissionLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }

        });

        binding.nextButton.setOnClickListener(v->{
            binding.nextButton.setEnabled(false);
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

        disableEditText(binding.dateOfBirthInput);

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
                        requireContext(),
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

    private void disableEditText(TextInputEditText editText) {
        editText.setFocusable(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }

    private void startFilePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_PROFILE_PICTURE_CHOOSER);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(data == null) {
            return;
        }

        if( requestCode == REQUEST_CODE_PROFILE_PICTURE_CHOOSER) {

            compressingImage.set(true);
            Uri selectedImage = data.getData();

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("Details", "Compressing image... " + bitmap.getWidth() + "  " + bitmap.getHeight());
            ImageUtilities
                    .scaleToProfilePictureGetBitmap(requireActivity(), bitmap)
                    .observe(getViewLifecycleOwner(), bitmapLiveEvent -> {
                        if(bitmapLiveEvent != null){
                            Bitmap picture = bitmapLiveEvent.getDataOnceAndReset();
                            if(picture != null) {
                                binding.profilePictureImage.setColorFilter(getResources().getColor(R.color.transparent, requireActivity().getTheme()));
                                binding.profilePictureImage.setImageBitmap(picture);
                            }
                            else {
                                // Handle null case
                            }
                        }
                        else{
                            Log.e("Details", "Error ");
                        }
                    });

            try {
                bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }


            ImageUtilities
                    .scaleToProfilePictureGetFile(requireActivity(), bitmap)
                    .observe(getViewLifecycleOwner(), fileLiveEvent -> {
                        compressingImage.set(false);
                        if(fileLiveEvent != null){
                            File file = fileLiveEvent.getDataOnceAndReset();
                            if(file != null) {
                                binding.getViewModel().profilePicture.setValue(Uri.fromFile(file));
                            }
                            else {
                                // Handle null case
                            }
                        }
                        else{
                            Log.e("Details", "Error ");
                        }
                    });

        }
    }
}
