package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.data.models.external.OrganizationModel;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.data.models.external.ShowcaseModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;


// NOTE this class is not supposed to be used at the activity scope because it is stateful

public class OrganizationProfileViewModel extends BaseViewModel {

    private String organizationID;
    private OrganizationModel organizationModel;

    private final MediatorLiveData<List<PostModel>> loadedOrganizationHighlights = new MediatorLiveData<>();
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

        organizationID = organizationID == null ? (organizationModel == null ? null : organizationModel.getId()) : organizationID;

        return DataRepository
                .getInstance()
                .getOrganizationDataHandler()
                .getOrganizationDetails(organizationID);
    }

    public void loadOrganizationHighlights(){

        LiveData<LiveEvent<List<PostModel>>> eventSource = DataRepository.getInstance()
                .getPostDataHandler()
                .getOrganizationHighlights(getDataRepositoryAccessIdentifier(), organizationID);

        loadedOrganizationHighlights.addSource(eventSource,
                listLiveEvent -> {
                    if(listLiveEvent != null) {
                        List<PostModel> highlights = listLiveEvent.getDataOnceAndReset();
                        if(highlights != null) {
                            List<PostModel> modelList = new ArrayList<>(highlights);
                            loadedOrganizationHighlights.setValue(modelList);
                        }
                    }
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

    public LiveData<List<PostModel>> getLoadedOrganizationHighlights(){
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
