package com.omada.junction.ui.organization;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.omada.junction.R;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.ui.uicomponents.binders.articlecard.ArticleCardSmallNoTitleBinder;
import com.omada.junction.ui.uicomponents.binders.eventcard.EventCardSmallNoTitleBinder;
import com.omada.junction.utils.factory.ShowcaseFeedViewModelFactory;
import com.omada.junction.viewmodels.FeedContentViewModel;
import com.omada.junction.viewmodels.ShowcaseFeedViewModel;

import java.util.List;

import mva3.adapter.ListSection;
import mva3.adapter.MultiViewAdapter;

public class OrganizationShowcaseFragment extends Fragment {

    private String organizationID;
    private String showcaseID;

    private ShowcaseFeedViewModel showcaseFeedViewModel;

    private final MultiViewAdapter adapter = new MultiViewAdapter();
    private final ListSection<PostModel> showcaseItemsListSection = new ListSection<>();
    private boolean refreshContents = true;


    public static OrganizationShowcaseFragment newInstance(String organizationID, String showcaseID) {

        Bundle args = new Bundle();
        args.putString("organizationID", organizationID);
        args.putString("showcaseID", showcaseID);

        OrganizationShowcaseFragment fragment = new OrganizationShowcaseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showcaseID = getArguments().getString("showcaseID");
        organizationID = getArguments().getString("organizationID");

        adapter.addSection(showcaseItemsListSection);

        showcaseFeedViewModel = new ViewModelProvider(
                requireActivity(),
                new ShowcaseFeedViewModelFactory(organizationID, showcaseID))
                .get(ShowcaseFeedViewModel.class);

        FeedContentViewModel feedContentViewModel = new ViewModelProvider(requireActivity()).get(FeedContentViewModel.class);

        adapter.registerItemBinders(
                new EventCardSmallNoTitleBinder(feedContentViewModel),
                new ArticleCardSmallNoTitleBinder(feedContentViewModel)
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.organization_showcase_fragment_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getView() == null) return;
        RecyclerView recyclerView = getView().findViewById(R.id.recycler_view);

        showcaseFeedViewModel.getLoadedShowcaseItems()
                .observe(getViewLifecycleOwner(), this::onShowcaseItemsLoaded);

        recyclerView.setLayoutManager(
                new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false)
        );

        recyclerView.setAdapter(adapter);
    }

    private void onShowcaseItemsLoaded(List<PostModel> items){
        if(refreshContents || showcaseItemsListSection.size() == 0){
            showcaseItemsListSection.addAll(items);
            refreshContents = false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
