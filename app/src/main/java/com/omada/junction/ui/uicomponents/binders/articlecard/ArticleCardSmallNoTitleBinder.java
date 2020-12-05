package com.omada.junction.ui.uicomponents.binders.articlecard;

import android.view.View;
import android.view.ViewGroup;

import com.omada.junction.data.models.ArticleModel;
import com.omada.junction.viewmodels.FeedContentViewModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;

public class ArticleCardSmallNoTitleBinder extends ItemBinder<ArticleModel, ArticleCardSmallNoTitleBinder.ArticleCardViewHolder> {

    private FeedContentViewModel feedContentViewModel;

    public ArticleCardSmallNoTitleBinder(FeedContentViewModel feedContentViewModel) {
        this.feedContentViewModel = feedContentViewModel;
    }

    @Override
    public ArticleCardViewHolder createViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void bindViewHolder(ArticleCardViewHolder holder, ArticleModel item) {

    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof ArticleModel;
    }

    public static class ArticleCardViewHolder extends ItemViewHolder<ArticleModel>{

        public ArticleCardViewHolder(View itemView) {
            super(itemView);
        }
    }
}
