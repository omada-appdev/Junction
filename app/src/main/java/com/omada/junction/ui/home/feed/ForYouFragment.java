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

import com.github.rubensousa.gravitysnaphelper.GravitySnapRecyclerView;
import com.omada.junction.R;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.ui.uicomponents.binders.articlecard.ArticleCardBinder;
import com.omada.junction.ui.uicomponents.binders.eventcard.EventCardLargeBinder;
import com.omada.junction.ui.uicomponents.binders.misc.SmallFooterBinder;
import com.omada.junction.ui.uicomponents.models.SmallFooterModel;
import com.omada.junction.viewmodels.FeedContentViewModel;
import com.omada.junction.viewmodels.HomeFeedViewModel;

import java.util.List;

import mva3.adapter.ItemSection;
import mva3.adapter.ListSection;
import mva3.adapter.MultiViewAdapter;
import mva3.adapter.util.InfiniteLoadingHelper;


public class ForYouFragment extends Fragment {

    private HomeFeedViewModel homeFeedViewModel;
    private ListSection<BaseModel> contentListSection;

    private boolean refreshContents = true;
    private boolean allPagesLoaded = false;

    public ForYouFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ViewModelProvider viewModelProvider = new ViewModelProvider(requireParentFragment().requireActivity());

        homeFeedViewModel = viewModelProvider.get(HomeFeedViewModel.class);

        if(homeFeedViewModel.getLoadedForYou().getValue() == null || homeFeedViewModel.getLoadedForYou().getValue().size() == 0){
            homeFeedViewModel.getForYouFeedContent();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.feed_foryou_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        FeedContentViewModel feedContentViewModel = new ViewModelProvider(requireParentFragment().requireActivity()).get(FeedContentViewModel.class);

        MultiViewAdapter adapter = new MultiViewAdapter();
        contentListSection = new ListSection<>();

        if(homeFeedViewModel.getLoadedForYou().getValue() != null) {
            contentListSection.addAll(
                    homeFeedViewModel.getLoadedForYou().getValue()
            );
        }

        adapter.addSection(contentListSection);

        adapter.registerItemBinders(
                new EventCardLargeBinder(feedContentViewModel),
                new ArticleCardBinder(feedContentViewModel),
                new SmallFooterBinder()
        );

        ItemSection<SmallFooterModel> footerSection = new ItemSection<>(new SmallFooterModel("Nothing more to show"));
        adapter.addSection(footerSection);

        footerSection.hideSection();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()) );
        recyclerView.setAdapter(adapter);

        InfiniteLoadingHelper infiniteLoadingHelper = new InfiniteLoadingHelper(recyclerView, R.layout.loading_footer_layout) {

            private boolean added = false;

            @Override
            public void onLoadNextPage(int page) {
                if(allPagesLoaded) {
                    markCurrentPageLoaded();
                    markAllPagesLoaded();
                    return;
                }
                recyclerView.scrollToPosition(contentListSection.size());
                refreshContents = true;
                homeFeedViewModel.getForYouFeedContent();
            }

            @Override
            public void markCurrentPageLoaded() {
                if(added) {
                    super.markCurrentPageLoaded();
                }
                else{
                    adapter.setInfiniteLoadingHelper(this);
                    super.markCurrentPageLoaded();
                    added = true;
                }
            }

            @Override
            public void markAllPagesLoaded() {
                allPagesLoaded = true;
                if(added) {
                    super.markAllPagesLoaded();
                }
                else{
                    adapter.setInfiniteLoadingHelper(this);
                    super.markAllPagesLoaded();
                    added = true;
                }
            }

        };

        homeFeedViewModel.getLoadedForYou()
                .observe(getViewLifecycleOwner(), contents->{

                    int oldSize = contentListSection.size();

                    onContentLoaded(contents);

                    infiniteLoadingHelper.markCurrentPageLoaded();

                    if(contents.size() <= 1){
                        infiniteLoadingHelper.onLoadNextPage(2);
                    }

                    if(contents.size() != oldSize){
                        ((GravitySnapRecyclerView)recyclerView).scrollToPosition(oldSize - 1);
                    }
                });

        homeFeedViewModel.getForYouCompleteNotifier()
                .observe(getViewLifecycleOwner(), booleanLiveEvent -> {
                    if(booleanLiveEvent.getData() != null && booleanLiveEvent.getDataOnceAndReset()){

                        infiniteLoadingHelper.markAllPagesLoaded();

                        if(contentListSection.size() == 0 && footerSection.getItem() != null){
                            footerSection.getItem().setFooterText("Follow your favourite organizations to see them here");
                        }
                        footerSection.showSection();

                        ((GravitySnapRecyclerView)recyclerView).snapToNextPosition(true);
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onContentLoaded(List<BaseModel> contents) {

        if(refreshContents || contentListSection.size() == 0) {

            contentListSection.addAll(contents.subList(contentListSection.size(), contents.size()));
            refreshContents = false;
        }
    }
}
