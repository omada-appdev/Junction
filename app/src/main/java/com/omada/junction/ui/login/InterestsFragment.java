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
import androidx.recyclerview.widget.GridLayoutManager;

import com.omada.junction.R;
import com.omada.junction.data.models.InterestModel;
import com.omada.junction.databinding.LoginInterestsFragmentLayoutBinding;
import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.viewmodels.LoginViewModel;
import com.omada.junction.ui.uicomponents.binders.InterestThumbnailBinder;

import mva3.adapter.ListSection;
import mva3.adapter.MultiViewAdapter;

public class InterestsFragment extends Fragment {

    private MultiViewAdapter adapter;

    private ListSection<InterestModel> interestListSection;
    private LoginViewModel loginViewModel;
    private LoginInterestsFragmentLayoutBinding binding;

    public static InterestsFragment newInstance() {

        Bundle args = new Bundle();

        InterestsFragment fragment = new InterestsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

        interestListSection = new ListSection<>();
        interestListSection.set(loginViewModel.getInterestsListSection());
        interestListSection.clearSelections();

        adapter = new MultiViewAdapter();
        adapter.setSpanCount(2);
        adapter.registerItemBinders(new InterestThumbnailBinder());
        adapter.addSection(interestListSection);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(
                inflater, R.layout.login_interests_fragment_layout, container, false);

        binding.setViewModel(loginViewModel);
        binding.setListSection(interestListSection);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        layoutManager.setSpanSizeLookup(adapter.getSpanSizeLookup());

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(layoutManager);

        binding.interestErrorText.setVisibility(View.INVISIBLE);

        loginViewModel.getDataValidationAction().observe(getViewLifecycleOwner(),
                dataValidationInformationLiveEvent -> {

                    if(dataValidationInformationLiveEvent.getData() == null){
                        return;
                    }

                    DataValidator.DataValidationInformation dataValidationInformation = dataValidationInformationLiveEvent.getDataOnceAndReset();

                    if(dataValidationInformation.getValidationPoint() != DataValidator.DataValidationPoint.VALIDATION_POINT_INTERESTS){
                        return;
                    }
                    switch (dataValidationInformation.getDataValidationResult()){
                        case VALIDATION_RESULT_OVERFLOW:
                            binding.interestErrorText.setText("Please select at most 5 interests");
                            binding.interestErrorText.setVisibility(View.VISIBLE);
                            break;
                        case VALIDATION_RESULT_UNDERFLOW:
                            binding.interestErrorText.setText("Please select up to 5 interests");
                            binding.interestErrorText.setVisibility(View.VISIBLE);
                            break;
                        default:
                            break;
                    }
                });
    }
}
