package com.omada.junction.ui.uicomponents.binders.organizationfeed;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.omada.junction.R;
import com.omada.junction.data.models.external.ShowcaseModel;
import com.omada.junction.databinding.OrganizationProfileShowcaseThumbnailLayoutBinding;
import com.omada.junction.viewmodels.FeedContentViewModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;


public class OrganizationShowcaseThumbnailBinder extends ItemBinder<ShowcaseModel, OrganizationShowcaseThumbnailBinder.ShowcaseViewHolder> {

    private FeedContentViewModel viewModel;

    public OrganizationShowcaseThumbnailBinder(FeedContentViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public ShowcaseViewHolder createViewHolder(ViewGroup parent) {

        OrganizationProfileShowcaseThumbnailLayoutBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.organization_profile_showcase_thumbnail_layout,
                parent,
                false
        );
        return new ShowcaseViewHolder(binding);
    }

    @Override
    public void bindViewHolder(final ShowcaseViewHolder holder, ShowcaseModel item) {
        holder.getBinding().setViewModel(viewModel);
        holder.getBinding().setShowcaseModel(item);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof ShowcaseModel;
    }

    public static class ShowcaseViewHolder extends ItemViewHolder<ShowcaseModel>{

        private final OrganizationProfileShowcaseThumbnailLayoutBinding binding;

        public ShowcaseViewHolder (OrganizationProfileShowcaseThumbnailLayoutBinding containerBinding) {
            super(containerBinding.getRoot());
            binding = containerBinding;
        }

        public OrganizationProfileShowcaseThumbnailLayoutBinding getBinding() {
            return binding;
        }
    }

}
