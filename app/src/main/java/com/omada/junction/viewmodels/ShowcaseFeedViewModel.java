package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.data.models.external.ShowcaseModel;

import java.util.List;


public class ShowcaseFeedViewModel extends BaseViewModel {

    private LiveData<List<PostModel>> loadedShowcaseItems;

    private final ShowcaseModel showcaseModel;

    public ShowcaseFeedViewModel(ShowcaseModel model){
        this.showcaseModel = model;
        initializeDataLoaders();
    }

    private void initializeDataLoaders(){
        loadedShowcaseItems = Transformations.map(
                DataRepository.getInstance().getShowcaseDataHandler().getOrganizationShowcaseItems(showcaseModel.getId()),
                postModels -> postModels
        );
    }

    public ShowcaseModel getShowcaseModel() {
        return showcaseModel;
    }

    public LiveData<List<PostModel>> getLoadedShowcaseItems(){
        return loadedShowcaseItems;
    }

}
