package com.omada.junction.data.handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.data.models.ShowcaseModel;
import com.omada.junction.data.models.ShowcaseModelRemoteDB;

import java.util.ArrayList;
import java.util.List;

public class ShowcaseDataHandler {

    public LiveData<List<ShowcaseModel>> getOrganizationShowcases(String organizationID){

        MutableLiveData<List<ShowcaseModel>> showcaseModelsLiveData = new MutableLiveData<>();

        FirebaseFirestore.getInstance()
                .collection("showcases")
                .whereEqualTo("showcaseCreator", organizationID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ShowcaseModel> showcaseModels = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        //TODO refactor ShowcaseModel into remote DB and local DB
                        ShowcaseModelRemoteDB modelRemoteDB = documentSnapshot.toObject(ShowcaseModelRemoteDB.class);
                        modelRemoteDB.setShowcaseID(documentSnapshot.getId());

                        if(modelRemoteDB != null) {
                            showcaseModels.add(new ShowcaseModel(
                                    modelRemoteDB
                            ));
                        }
                    }
                    showcaseModelsLiveData.setValue(showcaseModels);
                })
                .addOnFailureListener(e -> {

                });

        return showcaseModelsLiveData;

    }

    public LiveData<List<BaseModel>> getOrganizationShowcaseItems(String showcaseID){

        MutableLiveData<List<BaseModel>> showcaseItemsLiveData = new MutableLiveData<>();

        FirebaseFirestore.getInstance()
                .collection("showcases")
                .document(showcaseID)
                .collection("showcaseItems")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<BaseModel> baseModels = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                    }
                    showcaseItemsLiveData.setValue(baseModels);
                })
                .addOnFailureListener(e -> {

                });

        return showcaseItemsLiveData;

    }
}
