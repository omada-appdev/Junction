package com.omada.junction.ui.more;

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
import com.omada.junction.databinding.UserProfileDetailsFragmentLayoutBinding;
import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.viewmodels.UserProfileViewModel;

public class UserProfileEditDetailsFragment extends Fragment {

    private UserProfileDetailsFragmentLayoutBinding binding;
    private UserProfileViewModel userProfileViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserProfileDetailsFragmentLayoutBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.user_profile_details_fragment_layout, container, false
        );

        userProfileViewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        binding.setViewModel(userProfileViewModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userProfileViewModel.getDataValidationAction().observe(getViewLifecycleOwner(), dataValidationInformationLiveEvent -> {

            if(dataValidationInformationLiveEvent.getData() != null){
                DataValidator.DataValidationInformation information = dataValidationInformationLiveEvent.getDataOnceAndReset();

                if(information.getValidationPoint() == DataValidator.DataValidationPoint.VALIDATION_POINT_ALL
                    && information.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){

                    binding.nextButton.setEnabled(false);

                }
            }

        });
    }
}
