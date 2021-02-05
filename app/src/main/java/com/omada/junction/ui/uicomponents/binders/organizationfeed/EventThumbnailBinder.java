package com.omada.junction.ui.uicomponents.binders.organizationfeed;

import android.view.View;
import android.view.ViewGroup;

import com.omada.junction.data.models.external.EventModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;

public class EventThumbnailBinder extends ItemBinder<EventModel, EventThumbnailBinder.EventThumbnailViewHolder> {

    @Override
    public EventThumbnailViewHolder createViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void bindViewHolder(EventThumbnailViewHolder holder, EventModel item) {

    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof EventModel;
    }

    public static class EventThumbnailViewHolder extends ItemViewHolder<EventModel>{

        public EventThumbnailViewHolder(View itemView) {
            super(itemView);
        }
    }
}
