package com.omada.junction.ui.uicomponents.binders;

import android.view.View;
import android.view.ViewGroup;

import com.omada.junction.R;
import com.omada.junction.data.models.external.AchievementModel;


import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;

public class AchievementThumbnailBinder extends ItemBinder<AchievementModel, AchievementThumbnailBinder.AchievementItemViewHolder> {

    @Override
    public AchievementItemViewHolder createViewHolder(ViewGroup parent) {

        View view = inflate(parent, R.layout.achievement_thumbnail_layout);
        return new AchievementItemViewHolder(view);
    }

    @Override
    public void bindViewHolder(AchievementItemViewHolder holder, AchievementModel item) {
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof AchievementModel;
    }

    public static class AchievementItemViewHolder extends ItemViewHolder<AchievementModel>{

        public AchievementItemViewHolder (View contents) {
            super(contents);
        }
    }
}
