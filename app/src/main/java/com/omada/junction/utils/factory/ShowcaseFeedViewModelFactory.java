package com.omada.junction.utils.factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.omada.junction.viewmodels.ShowcaseFeedViewModel;

public class ShowcaseFeedViewModelFactory implements ViewModelProvider.Factory {

    private final String organizationID;
    private final String showcaseID;


    public ShowcaseFeedViewModelFactory(String organizationID, String showcaseID) {
        this.organizationID = organizationID;
        this.showcaseID = showcaseID;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //noinspection unchecked
        return (T) new ShowcaseFeedViewModel(organizationID, showcaseID);
    }
}