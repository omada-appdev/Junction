package com.omada.junction.ui.eventdetails;

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
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.databinding.EventDetailsFragmentLayoutBinding;
import com.omada.junction.viewmodels.FeedContentViewModel;


public class EventDetailsFragment extends Fragment {

    private EventModel eventModel;
    private EventDetailsFragmentLayoutBinding binding;

    public static EventDetailsFragment newInstance(EventModel eventModel) {

        Bundle args = new Bundle();
        args.putParcelable("eventModel", eventModel);

        EventDetailsFragment fragment = new EventDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            if(getArguments() == null) return;
            eventModel = getArguments().getParcelable("eventModel");
        }
        else{
            eventModel = savedInstanceState.getParcelable("eventModel");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.event_details_fragment_layout, container, false);
        binding.setViewModel(new ViewModelProvider(requireActivity()).get(FeedContentViewModel.class));
        binding.setEventDetails(eventModel);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable("eventModel", eventModel);
    }
}
