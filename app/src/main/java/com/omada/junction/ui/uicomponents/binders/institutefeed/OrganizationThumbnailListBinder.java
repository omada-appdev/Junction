package com.omada.junction.ui.uicomponents.binders.institutefeed;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.omada.junction.R;
import com.omada.junction.data.models.external.OrganizationModel;
import com.omada.junction.viewmodels.FeedContentViewModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;
import mva3.adapter.ListSection;
import mva3.adapter.MultiViewAdapter;

public class OrganizationThumbnailListBinder extends ItemBinder<ListSection<OrganizationModel>, OrganizationThumbnailListBinder.OrganizationThumbnailListViewHolder> {

    private final MultiViewAdapter adapter = new MultiViewAdapter();
    private boolean addedSection = false;

    public OrganizationThumbnailListBinder(FeedContentViewModel viewModel){
        adapter.registerItemBinders(new OrganizationThumbnailBinder(viewModel));
    }

    @Override
    public OrganizationThumbnailListViewHolder createViewHolder(ViewGroup parent) {

        View view = inflate(parent, R.layout.institute_feed_organizations_layout);
        return new OrganizationThumbnailListViewHolder(view);
    }

    @Override
    public void bindViewHolder(OrganizationThumbnailListViewHolder holder, ListSection<OrganizationModel> item) {

        holder.recyclerView.setLayoutManager(
                new GridLayoutManager(holder.recyclerView.getContext(), 2, RecyclerView.HORIZONTAL, false)
        );

        if(!addedSection) {
            adapter.addSection(item);
            addedSection = true;
        }
        holder.recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof ListSection && ((ListSection<?>) item).get(0) instanceof OrganizationModel;
    }

    public static class OrganizationThumbnailListViewHolder extends ItemViewHolder<ListSection<OrganizationModel>> {

        private final RecyclerView recyclerView;

        public OrganizationThumbnailListViewHolder(View view) {
            super(view);
            recyclerView = view.findViewById(R.id.recycler_view);
        }
    }
}