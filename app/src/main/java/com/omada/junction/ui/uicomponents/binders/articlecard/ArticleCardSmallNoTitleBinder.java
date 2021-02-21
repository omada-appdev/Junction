package com.omada.junction.ui.uicomponents.binders.articlecard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.omada.junction.R;
import com.omada.junction.data.models.external.ArticleModel;
import com.omada.junction.databinding.ArticleCardSmallNoTitleLayoutBinding;
import com.omada.junction.viewmodels.FeedContentViewModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;

public class ArticleCardSmallNoTitleBinder extends ItemBinder<ArticleModel, ArticleCardSmallNoTitleBinder.ArticleCardViewHolder> {

    private FeedContentViewModel viewModel;

    public ArticleCardSmallNoTitleBinder(FeedContentViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public ArticleCardViewHolder createViewHolder(ViewGroup parent) {
        ArticleCardSmallNoTitleLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.article_card_small_no_title_layout, parent, false);
        return new ArticleCardViewHolder(binding);
    }

    @Override
    public void bindViewHolder(ArticleCardViewHolder holder, ArticleModel item) {
        holder.binding.setArticleModel(item);
        holder.binding.setViewModel(viewModel);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof ArticleModel;
    }

    public static class ArticleCardViewHolder extends ItemViewHolder<ArticleModel>{

        private ArticleCardSmallNoTitleLayoutBinding binding;

        public ArticleCardViewHolder(ArticleCardSmallNoTitleLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ArticleCardSmallNoTitleLayoutBinding getBinding() {
            return binding;
        }
    }
}
