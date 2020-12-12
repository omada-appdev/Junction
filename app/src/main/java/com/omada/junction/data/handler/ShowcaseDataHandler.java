package com.omada.junction.data.handler;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omada.junction.data.models.ArticleModel;
import com.omada.junction.data.models.ArticleModelRemoteDB;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.data.models.EventModelRemoteDB;
import com.omada.junction.data.models.ShowcaseModel;
import com.omada.junction.data.models.ShowcaseModelRemoteDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ShowcaseDataHandler {

    public LiveData<List<ShowcaseModel>> getOrganizationShowcases(String organizationID){

        MutableLiveData<List<ShowcaseModel>> showcaseModelsLiveData = new MutableLiveData<>();

        FirebaseFirestore.getInstance()
                .collection("showcases")
                .whereEqualTo("creator", organizationID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<ShowcaseModel> showcaseModels = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        //TODO refactor ShowcaseModel into remote DB and local DB
                        ShowcaseModelRemoteDB modelRemoteDB = documentSnapshot.toObject(ShowcaseModelRemoteDB.class);
                        modelRemoteDB.setShowcaseID(documentSnapshot.getId());

                        showcaseModels.add(new ShowcaseModel(
                                modelRemoteDB
                        ));
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
                .collection("posts")
                .whereEqualTo("showcase", showcaseID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.e("showcases", "loaded " + queryDocumentSnapshots.size() + " showcases");

                    List<BaseModel> baseModels = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                        String type = documentSnapshot.getString("type");
                        if(type == null) continue;

                        switch (type){
                            case "event":
                                baseModels.add(new EventModel(
                                        documentSnapshot.toObject(EventModelRemoteDB.class)
                                ));
                                break;
                            case "article":
                                baseModels.add(new ArticleModel(
                                        documentSnapshot.toObject(ArticleModelRemoteDB.class)
                                ));
                                break;
                        }
                    }
                    showcaseItemsLiveData.setValue(baseModels);
                })
                .addOnFailureListener(e -> {

                });

        return showcaseItemsLiveData;

    }
}
