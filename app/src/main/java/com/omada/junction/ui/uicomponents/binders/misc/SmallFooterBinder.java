package com.omada.junction.ui.uicomponents.binders.misc;

import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;
import com.omada.junction.R;
import com.omada.junction.ui.uicomponents.models.SmallFooterModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;

public class SmallFooterBinder extends ItemBinder<SmallFooterModel, SmallFooterBinder.FooterViewHolder> {

    @Override
    public FooterViewHolder createViewHolder(ViewGroup parent) {
        return new FooterViewHolder(inflate(parent, R.layout.small_footer_layout));
    }

    @Override
    public void bindViewHolder(FooterViewHolder holder, SmallFooterModel item) {
        holder.footerTextView.setText(item.getFooterText());
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof SmallFooterModel;
    }

    public static class FooterViewHolder extends ItemViewHolder<SmallFooterModel>{

        MaterialTextView footerTextView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            footerTextView = itemView.findViewById(R.id.footer_text);
        }
    }
}
