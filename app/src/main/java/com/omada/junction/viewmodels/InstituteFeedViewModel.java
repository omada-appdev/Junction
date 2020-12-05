package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.data.models.OrganizationModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;

public class InstituteFeedViewModel extends ViewModel {

    private final MediatorLiveData<List<BaseModel>> loadedHighlights = new MediatorLiveData<>();
    private LiveData<List<OrganizationModel>> loadedInstituteOrganizations;

    private LiveData<List<EventModel>> loadedHighlightEvents;

    public InstituteFeedViewModel(){
        initializeDataLoaders();
        distributeLoadedData();
    }

    private void initializeDataLoaders() {
        loadedHighlightEvents = Transformations.map(
                DataRepository.getInstance().getEventDataHandler().getLoadedInstituteHighlightEventsNotifier(),
                eventList-> eventList
        );

        loadedInstituteOrganizations = Transformations.map(
                DataRepository.getInstance().getOrganizationDataHandler().getLoadedInstituteOrganizationsNotifier(),
                LiveEvent::getDataOnceAndReset);
    }

    private void distributeLoadedData() {
        loadedHighlights.addSource(loadedHighlightEvents, eventModels -> {
            List<BaseModel> modelList = new ArrayList<>(eventModels);
            loadedHighlights.setValue(modelList);
        });
    }

    public void loadInstituteOrganizations(){
        DataRepository.getInstance()
                .getOrganizationDataHandler()
                .getInstituteOrganizations();
    }

    public void loadInstituteHighlights(){
        DataRepository.getInstance()
                .getEventDataHandler()
                .getInstituteHighlightEvents();
    }

    public void searchInstitute(String query){

    }

    public LiveData<List<BaseModel>> getLoadedHighlights(){
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

}
