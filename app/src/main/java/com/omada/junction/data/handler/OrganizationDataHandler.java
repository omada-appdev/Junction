package com.omada.junction.data.handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omada.junction.data.BaseDataHandler;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.converter.OrganizationModelConverter;
import com.omada.junction.data.models.external.OrganizationModel;
import com.omada.junction.data.models.internal.remote.OrganizationModelRemoteDB;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;

public class OrganizationDataHandler extends BaseDataHandler {

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
    private OrganizationModelConverter organizationModelConverter = new OrganizationModelConverter();


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
                .whereEqualTo("instituteVerified", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<OrganizationModel> models = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                        OrganizationModelRemoteDB modelRemoteDB = documentSnapshot.toObject(OrganizationModelRemoteDB.class);
                        if(modelRemoteDB == null) {
                            return;
                        }
                        modelRemoteDB.setId(documentSnapshot.getId());
                        models.add(organizationModelConverter.convertRemoteDBToExternalModel(modelRemoteDB));
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
                    OrganizationModelRemoteDB modelRemoteDB = documentSnapshot.toObject(OrganizationModelRemoteDB.class);
                    if(modelRemoteDB == null) {
                        return;
                    }
                    modelRemoteDB.setId(documentSnapshot.getId());
                    remoteData.setValue(new LiveEvent<>(organizationModelConverter.convertRemoteDBToExternalModel(modelRemoteDB)));
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
