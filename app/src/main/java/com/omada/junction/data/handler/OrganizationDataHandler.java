package com.omada.junction.data.handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.OrganizationModel;
import com.omada.junction.data.models.OrganizationModelRemoteDB;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;

public class OrganizationDataHandler {

    /*
   #############################
   # INPUT FIELDS FROM SOURCES # (if required)
   #############################
    */


    /*
    ##############################
    # OUTPUT FIELDS TO VIEWMODEL #
    ##############################
     */
    private MutableLiveData<LiveEvent<List<OrganizationModel>>> loadedInstituteOrganizationsNotifier = new MutableLiveData<>();

    /*
    ###########################
    # FIELDS FOR INTERNAL USE #
    ###########################
    */


    public LiveData<LiveEvent<OrganizationModel>> getOrganizationDetails(String organizationID) {

        MutableLiveData<LiveEvent<OrganizationModel>> detailsLiveEvent = new MutableLiveData<>();

        //TODO first search in local and if it fails search in remote
        getOrganizationFromRemote(organizationID, detailsLiveEvent);

        return detailsLiveEvent;
    }

    public void getInstituteOrganizations(){

        String instituteID = DataRepository
                .getInstance()
                .getUserDataHandler()
                .getCurrentUserModel()
                .getInstitute();

        FirebaseFirestore.getInstance()
                .collection("organizations")
                .whereEqualTo("institute", instituteID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<OrganizationModel> models = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                        OrganizationModelRemoteDB modelRemoteDB = documentSnapshot.toObject(OrganizationModelRemoteDB.class);
                        modelRemoteDB.setOrganizationID(documentSnapshot.getId());
                        models.add(new OrganizationModel(modelRemoteDB));
                    }
                    loadedInstituteOrganizationsNotifier.setValue(new LiveEvent<>(models));

                });
    }

    private void getOrganizationFromRemote(String organizationID, MutableLiveData<LiveEvent<OrganizationModel>> remoteData){
        FirebaseFirestore.getInstance()
                .collection("organizations")
                .document(organizationID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    OrganizationModel model = new OrganizationModel(documentSnapshot.toObject(OrganizationModelRemoteDB.class));
                    remoteData.setValue(new LiveEvent<>(model));
                });
    }

    private void getOrganizationFromLocal(String organizationID){
    }

    public LiveData<LiveEvent<List<OrganizationModel>>> getLoadedInstituteOrganizationsNotifier(){
        return loadedInstituteOrganizationsNotifier;
    }

    public void resetLastInstituteOrganization() {
        loadedInstituteOrganizationsNotifier = new MutableLiveData<>();
    }


    private static class PaginationHelper{
        public static DocumentSnapshot lastInstituteOrganization;
    }

}
