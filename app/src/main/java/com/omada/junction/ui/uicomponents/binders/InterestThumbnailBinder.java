package com.omada.junction.ui.uicomponents.binders;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.omada.junction.R;
import com.omada.junction.data.models.InterestModel;
import com.omada.junction.databinding.InterestsSelectionThumbnailLayoutBinding;

import java.util.Objects;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;


public class InterestThumbnailBinder extends ItemBinder<InterestModel, InterestThumbnailBinder.InterestSelectionViewHolder> {

    @Override public InterestThumbnailBinder.InterestSelectionViewHolder createViewHolder(ViewGroup parent) {
        View v = inflate(parent, R.layout.interests_selection_thumbnail_layout);
        InterestsSelectionThumbnailLayoutBinding binding = DataBindingUtil.bind(v);
        return new InterestSelectionViewHolder(Objects.requireNonNull(binding));
    }

    @Override public boolean canBindData(Object item) {
        return item instanceof InterestModel;
    }

    @Override public void bindViewHolder(InterestThumbnailBinder.InterestSelectionViewHolder holder, InterestModel item) {

        holder.interestText.setText(item.interestString);
        holder.interestImage.setImageResource(item.getDrawableResourceId());
        Drawable foregroundDrawableInactive = ContextCompat.getDrawable(holder.interestText.getContext(), R.drawable.interests_thumbnail_badge_deselected);
        Drawable foregroundDrawableActive = ContextCompat.getDrawable(holder.interestText.getContext(), R.drawable.interests_thumbnail_badge_selected);
        holder.interestImage.setForeground(holder.isItemSelected() ? foregroundDrawableActive : foregroundDrawableInactive);
    }

    public static class InterestSelectionViewHolder extends ItemViewHolder<InterestModel>{

        public MaterialTextView interestText;
        public ShapeableImageView interestImage;

        public  InterestSelectionViewHolder(InterestsSelectionThumbnailLayoutBinding containerBinding){
            super(containerBinding.getRoot());
            interestText = containerBinding.interestText;
            interestImage = containerBinding.interestImage;
            containerBinding.getRoot().setOnClickListener(v -> toggleItemSelection());
        }

    }
}
