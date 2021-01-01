package com.omada.junction.ui.uicomponents.binders.articlecard;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.omada.junction.R;
import com.omada.junction.data.models.ArticleModel;
import com.omada.junction.databinding.ArticleCardLargeLayoutBinding;
import com.omada.junction.viewmodels.FeedContentViewModel;

import mva3.adapter.ItemBinder;
import mva3.adapter.ItemViewHolder;


public class ArticleCardBinder extends ItemBinder<ArticleModel, ArticleCardBinder.ArticleCardViewHolder> {


    private final FeedContentViewModel viewModel;

    public ArticleCardBinder(FeedContentViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public ArticleCardViewHolder createViewHolder(ViewGroup parent) {
        ArticleCardLargeLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.article_card_large_layout, parent, false);
        return new ArticleCardViewHolder(binding);
    }

    @Override
    public void bindViewHolder(final ArticleCardViewHolder holder, ArticleModel item) {
        holder.binding.setArticleModel(item);
        holder.binding.setViewModel(viewModel);
    }

    @Override
    public boolean canBindData(Object item) {
        return item instanceof ArticleModel;
    }

    public static class ArticleCardViewHolder extends ItemViewHolder<ArticleModel>{

        ArticleCardLargeLayoutBinding binding;

        public ArticleCardViewHolder (ArticleCardLargeLayoutBinding containerBinding) {
            super(containerBinding.getRoot());
            binding = containerBinding;
        }

        public ArticleCardLargeLayoutBinding getBinding() {
            return binding;
        }
    }
}
