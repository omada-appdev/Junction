package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Transformations;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.external.OrganizationModel;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;

public class InstituteFeedViewModel extends BaseViewModel {

    private LiveData<List<PostModel>> loadedHighlights = new MediatorLiveData<>();
    private LiveData<List<OrganizationModel>> loadedInstituteOrganizations;

    public InstituteFeedViewModel(){
        initializeDataLoaders();
        distributeLoadedData();
    }

    private void initializeDataLoaders() {

        loadedHighlights = Transformations.map(
                DataRepository.getInstance().getPostDataHandler().getLoadedInstituteHighlightsNotifier(),
                listLiveEvent-> {
                    if(listLiveEvent == null) {
                        return null;
                    }
                    // returns null if nothing is there
                    return listLiveEvent.getDataOnceAndReset();
                }
        );

        loadedInstituteOrganizations = Transformations.map(
                DataRepository.getInstance().getOrganizationDataHandler().getLoadedInstituteOrganizationsNotifier(),
                LiveEvent::getDataOnceAndReset);
    }

    private void distributeLoadedData() {

        loadedHighlights = Transformations.map(
                DataRepository.getInstance().getPostDataHandler().getLoadedInstituteHighlightsNotifier(),
                listLiveEvent-> {
                    if(listLiveEvent == null) {
                        return null;
                    }
                    // returns null if nothing is there
                    return listLiveEvent.getDataOnceAndReset();
                }
        );

        loadedInstituteOrganizations = Transformations.map(
                DataRepository.getInstance().getOrganizationDataHandler().getLoadedInstituteOrganizationsNotifier(),
                LiveEvent::getDataOnceAndReset);
    }

    public void loadInstituteOrganizations(){
        DataRepository.getInstance()
                .getOrganizationDataHandler()
                .getInstituteOrganizations();
    }

    public void loadInstituteHighlights(){
        DataRepository.getInstance()
                .getPostDataHandler()
                .getInstituteHighlights(getDataRepositoryAccessIdentifier());
    }

    public void searchInstitute(String query){

    }

    public LiveData<List<PostModel>> getLoadedHighlights(){
        return loadedHighlights;
    }

    public LiveData<List<OrganizationModel>> getLoadedInstituteOrganizations(){
        return loadedInstituteOrganizations;
    }

    public boolean checkInstituteContentLoaded(){

        if(loadedInstituteOrganizations.getValue() == null || loadedHighlights.getValue() == null){
            return false;
        }
        else if(loadedInstituteOrganizations.getValue().size() == 0 || loadedHighlights.getValue().size() == 0){
            return false;
        }
        else {
            return true;
        }
    }

    // TODO define this function to nuke all the existing propagation variables
    public void reinitializeFeed() {

        DataRepository.getInstance().resetInstituteFeedContent();
        initializeDataLoaders();
        distributeLoadedData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        reinitializeFeed();
    }
}
