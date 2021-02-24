package com.omada.junction.ui.uicomponents.binders.misc;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;
import com.omada.junction.R;
import com.omada.junction.ui.uicomponents.models.LargeBoldHeaderModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;

public class LargeBoldHeaderBinder extends ItemBinder<LargeBoldHeaderModel, LargeBoldHeaderBinder.HeaderViewHolder> {

    @Override
    public HeaderViewHolder createViewHolder(ViewGroup parent) {
        return new HeaderViewHolder(inflate(parent, R.layout.large_bold_header_layout));
    }

    @Override
    public void bindViewHolder(HeaderViewHolder holder, LargeBoldHeaderModel item) {
        holder.headerTextView.setText(item.getHeaderText());
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof LargeBoldHeaderModel;
    }

    public static class HeaderViewHolder extends ItemViewHolder<LargeBoldHeaderModel>{

        MaterialTextView headerTextView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerTextView = itemView.findViewById(R.id.header_text);
        }
    }
}
