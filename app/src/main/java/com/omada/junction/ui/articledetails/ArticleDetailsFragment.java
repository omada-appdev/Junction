package com.omada.junction.ui.articledetails;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.omada.junction.R;
import com.omada.junction.data.models.external.ArticleModel;


public class ArticleDetailsFragment extends Fragment {

    private ArticleModel articleModel;

    public static ArticleDetailsFragment newInstance(ArticleModel articleModel) {

        Bundle args = new Bundle();
        args.putParcelable("articleModel", articleModel);

        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() == null) {
            throw new RuntimeException("Attempt to initialize details fragment without providing id");
        }
        articleModel = getArguments().getParcelable("articleModel");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        com.omada.junction.databinding.ArticleDetailsFragmentLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.article_details_fragment_layout, container, false);
        binding.setArticleModel(articleModel);

        return binding.getRoot();
    }

}
