package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.external.PostModel;

import java.util.List;


public class ShowcaseFeedViewModel extends ViewModel {

    private LiveData<List<PostModel>> loadedShowcaseItems;

    private final String organizationID;
    private final String showcaseID;

    public ShowcaseFeedViewModel(String organizationID, String showcaseID){

        this.organizationID = organizationID;
        this.showcaseID = showcaseID;

        initializeDataLoaders();
    }

    private void initializeDataLoaders(){
        loadedShowcaseItems = Transformations.map(
                DataRepository.getInstance().getShowcaseDataHandler().getOrganizationShowcaseItems(showcaseID),
                postModels -> postModels
        );
    }

    public LiveData<List<PostModel>> getLoadedShowcaseItems(){
        return loadedShowcaseItems;
    }

}
