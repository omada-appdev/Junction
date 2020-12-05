package com.omada.junction.ui.home.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.omada.junction.R;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.ui.uicomponents.binders.articlecard.ArticleCardBinder;
import com.omada.junction.ui.uicomponents.binders.eventcard.EventCardLargeBinder;
import com.omada.junction.viewmodels.FeedContentViewModel;
import com.omada.junction.viewmodels.HomeFeedViewModel;

import java.util.List;

import mva3.adapter.ListSection;
import mva3.adapter.MultiViewAdapter;


public class ForYouFragment extends Fragment {

    private HomeFeedViewModel homeFeedViewModel;
    private final MultiViewAdapter adapter = new MultiViewAdapter();
    private final ListSection<BaseModel> contentListSection = new ListSection<>();

    private boolean refreshContents = true;

    public ForYouFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireParentFragment().requireActivity());

        homeFeedViewModel = viewModelProvider.get(HomeFeedViewModel.class);
        FeedContentViewModel feedContentViewModel = viewModelProvider.get(FeedContentViewModel.class);

        adapter.registerItemBinders(new EventCardLargeBinder(feedContentViewModel), new ArticleCardBinder(feedContentViewModel));
        adapter.addSection(contentListSection);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_foryou_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()) );

        homeFeedViewModel.getLoadedForYou()
                .observe(getViewLifecycleOwner(), this::onContentLoaded);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onContentLoaded(List<BaseModel> contents) {

        if(refreshContents || contentListSection.size() == 0) {

            Toast.makeText(requireContext(), refreshContents+"  "+contentListSection.size(), Toast.LENGTH_SHORT).show();

            contentListSection.addAll(contents);
            refreshContents = false;
        }
    }
}
