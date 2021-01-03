package com.omada.junction.ui.organization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayoutMediator;
import com.omada.junction.R;
import com.omada.junction.data.models.OrganizationModel;
import com.omada.junction.databinding.OrganizationProfileFragmentLayoutBinding;
import com.omada.junction.ui.uicomponents.CustomBindings;
import com.omada.junction.utils.taskhandler.LiveEvent;
import com.omada.junction.viewmodels.OrganizationProfileViewModel;

public class OrganizationProfileFragment extends Fragment {

    private OrganizationProfileFragmentLayoutBinding binding;
    private OrganizationProfileViewModel organizationProfileViewModel;


    public static OrganizationProfileFragment newInstance(OrganizationModel organizationModel) {

        Bundle args = new Bundle();
        args.putString("organizationID", organizationModel.getOrganizationID());
        args.putSerializable("organizationModel", organizationModel);

        OrganizationProfileFragment fragment = new OrganizationProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OrganizationProfileFragment newInstance(String organizationID) {

        Bundle args = new Bundle();
        args.putString("organizationID", organizationID);
        args.putParcelable("organizationModel", null);

        OrganizationProfileFragment fragment = new OrganizationProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        organizationProfileViewModel = new ViewModelProvider(this).get(OrganizationProfileViewModel.class);

        if(savedInstanceState == null) {
            // created first time

            organizationProfileViewModel.setOrganizationModel((OrganizationModel) getArguments().get("organizationModel"));
            organizationProfileViewModel.setOrganizationID(getArguments().getString("organizationID"));

            organizationProfileViewModel.loadOrganizationHighlights();
            organizationProfileViewModel.loadOrganizationShowcases();
        }
        else{

            if(organizationProfileViewModel.getOrganizationID() == null){
                if(organizationProfileViewModel.getOrganizationModel() != null){
                    organizationProfileViewModel.setOrganizationID(
                            organizationProfileViewModel.getOrganizationModel().getOrganizationID()
                    );
                }
                else {
                    organizationProfileViewModel.setOrganizationID(savedInstanceState.getString("organizationID"));
                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.organization_profile_fragment_layout, container, false);
        binding.setViewModel(organizationProfileViewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        if(organizationProfileViewModel.getOrganizationModel() == null) {

            LiveData<LiveEvent<OrganizationModel>> orgLiveData = organizationProfileViewModel.getOrganizationDetails();

            orgLiveData.observe(getViewLifecycleOwner(), orgModelLiveEvent->{
                if (orgModelLiveEvent != null && orgModelLiveEvent.getData() != null){
                    organizationProfileViewModel.setOrganizationModel(orgModelLiveEvent.getDataOnceAndReset());
                    populateViews();
                }
            });
        }
        else{
            populateViews();
        }

        ViewPager2 pager = binding.organizationProfilePager;

        pager.setOffscreenPageLimit(2);
        pager.setAdapter(new OrganizationProfilePagerAdapter(getChildFragmentManager(), getLifecycle()));
        pager.setCurrentItem(0);

        pager.setUserInputEnabled(false);

        new TabLayoutMediator(binding.organizationProfileTabs, pager, (tab, position) -> {
            switch(position){
                case 0:
                    tab.setText("Content");
                    break;
                case 1:
                    tab.setText("About");
                    break;
            }
        }).attach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("organizationID", organizationProfileViewModel.getOrganizationID());
    }

    private void populateViews(){
        binding.organizationNameText.setText(organizationProfileViewModel.getOrganizationModel().getName());
        CustomBindings.loadImage(
                binding.organizationProfilePictureImage,
                organizationProfileViewModel.getOrganizationModel().getProfilePhoto()
        );
    }

    public static class OrganizationProfilePagerAdapter extends FragmentStateAdapter{

        public OrganizationProfilePagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position){
                case 0:
                    return new OrganizationContentFragment();
                case 1:
                    return new OrganizationAboutFragment();
            }
            return null;
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
