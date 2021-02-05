package com.omada.junction.ui.more;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.omada.junction.R;
import com.omada.junction.data.models.external.AchievementModel;
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.databinding.UserProfileFragmentLayoutBinding;
import com.omada.junction.viewmodels.UserProfileViewModel;

import mva3.adapter.ListSection;
import mva3.adapter.MultiViewAdapter;

public class UserProfileFragment extends Fragment implements AppBarLayout.OnOffsetChangedListener {

    private UserProfileFragmentLayoutBinding binding;

    private final ListSection<EventModel> eventModelListSection = new ListSection<>();
    private final ListSection<AchievementModel> achievementModelListSection = new ListSection<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserProfileFragmentLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment_layout, container, false);
        this.binding = binding;

        binding.setViewModel(
                new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class)
        );

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MultiViewAdapter upcomingEventsAdapter = new MultiViewAdapter();
        MultiViewAdapter achievementsAdapter = new MultiViewAdapter();

        binding.userProfileUpcomingEventsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
        );

        binding.userProfileAchievementsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false)
        );

        binding.appbar.addOnOffsetChangedListener(this);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (-verticalOffset >= binding.profileDetails.getHeight()) {
            binding.userProfileToolbar.setTitle("Your Profile");
            binding.userProfileToolbar.setAlpha(1);
        }
        else {
            binding.userProfileToolbar.setTitle("");
            binding.userProfileToolbar.setAlpha(0);
        }
    }
}
