package com.omada.junction.ui.uicomponents.binders.eventcard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.omada.junction.R;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.databinding.EventCardMediumLayoutBinding;
import com.omada.junction.viewmodels.FeedContentViewModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;


public class EventCardMediumBinder extends ItemBinder<EventModel, EventCardMediumBinder.EventCardMediumViewHolder> {

    private final FeedContentViewModel viewModel;

    public EventCardMediumBinder(FeedContentViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public EventCardMediumViewHolder createViewHolder(ViewGroup parent) {
        EventCardMediumLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.event_card_medium_layout, parent, false);
        return new EventCardMediumViewHolder(binding);
    }

    @Override
    public void bindViewHolder(final EventCardMediumViewHolder holder, EventModel item) {

        holder.getBinding().setEventModel(item);
        holder.getBinding().setViewModel(viewModel);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof EventModel;
    }

    public static class EventCardMediumViewHolder extends ItemViewHolder<EventModel>{

        EventCardMediumLayoutBinding binding;

        public EventCardMediumViewHolder (EventCardMediumLayoutBinding containerBinding) {
            super(containerBinding.getRoot());
            binding = containerBinding;
        }

        public EventCardMediumLayoutBinding getBinding() {
            return binding;
        }
    }

}
