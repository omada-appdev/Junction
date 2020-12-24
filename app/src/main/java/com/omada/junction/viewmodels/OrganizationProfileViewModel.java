package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.data.models.OrganizationModel;
import com.omada.junction.data.models.ShowcaseModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;


// NOTE this class is not supposed to be used at the activity scope because it is stateful

public class OrganizationProfileViewModel extends ViewModel {

    private String organizationID;
    private OrganizationModel organizationModel;

    private final MediatorLiveData<List<BaseModel>> loadedOrganizationHighlights = new MediatorLiveData<>();
    private final MediatorLiveData<List<ShowcaseModel>> loadedOrganizationShowcases = new MediatorLiveData<>();

    public void setOrganizationID(String orgID){
        organizationID = orgID;
    }

    public void setOrganizationModel(OrganizationModel organizationModel) {
        this.organizationModel = organizationModel;
    }

    public OrganizationModel getOrganizationModel(){
        return organizationModel;
    }

    public String getOrganizationID(){
        return organizationID;
    }

    public LiveData<LiveEvent<OrganizationModel>> getOrganizationDetails(){

        organizationID = organizationID == null ? (organizationModel == null ? null : organizationModel.getOrganizationID()) : organizationID;

        return DataRepository
                .getInstance()
                .getOrganizationDataHandler()
                .getOrganizationDetails(organizationID);
    }

    public void loadOrganizationHighlights(){

        LiveData<List<EventModel>> eventSource = DataRepository.getInstance()
                .getEventDataHandler()
                .getOrganizationHighlightEvents(organizationID);

        loadedOrganizationHighlights.addSource(eventSource,
                eventModels -> {
                    List<BaseModel> modelList = new ArrayList<>(eventModels);
                    loadedOrganizationHighlights.setValue(modelList);
                    loadedOrganizationHighlights.removeSource(eventSource);
                }
        );
    }

    public void loadOrganizationShowcases(){

        LiveData<List<ShowcaseModel>> showcaseSource = DataRepository.getInstance()
                .getShowcaseDataHandler()
                .getOrganizationShowcases(organizationID);

        loadedOrganizationShowcases.addSource(showcaseSource,
                showcaseModels -> {
                    loadedOrganizationShowcases.setValue(showcaseModels);
                    loadedOrganizationShowcases.removeSource(showcaseSource);
                }
        );
    }

    public LiveData<List<BaseModel>> getLoadedOrganizationHighlights(){
        return loadedOrganizationHighlights;
    }

    public LiveData<List<ShowcaseModel>> getLoadedOrganizationShowcases(){
        return loadedOrganizationShowcases;
    }

    public boolean getFollowingStatus(){

        UserDataHandler.UserModel currentUserModel = DataRepository.getInstance()
                .getUserDataHandler()
                .getCurrentUserModel();

        Object val = currentUserModel.getFollowing().get(organizationID);

        return val != null;

    }

    public void updateFollowingStatus(boolean following){
        DataRepository.getInstance()
                .getUserDataHandler()
                .updateFollow(organizationID, following);
    }

}
