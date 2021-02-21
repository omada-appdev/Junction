package com.omada.junction.data.handler;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omada.junction.data.models.converter.ArticleModelConverter;
import com.omada.junction.data.models.converter.EventModelConverter;
import com.omada.junction.data.models.converter.ShowcaseModelConverter;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.data.models.external.ShowcaseModel;
import com.omada.junction.data.models.internal.remote.ArticleModelRemoteDB;
import com.omada.junction.data.models.internal.remote.EventModelRemoteDB;
import com.omada.junction.data.models.internal.remote.ShowcaseModelRemoteDB;

import java.util.ArrayList;
import java.util.List;


public class ShowcaseDataHandler {

    private ShowcaseModelConverter showcaseModelConverter = new ShowcaseModelConverter();
    private EventModelConverter eventModelConverter = new EventModelConverter();
    private ArticleModelConverter articleModelConverter = new ArticleModelConverter();

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
                        if(modelRemoteDB == null) {
                            return;
                        }
                        modelRemoteDB.setId(documentSnapshot.getId());
                        showcaseModels.add(showcaseModelConverter.convertRemoteDBToExternalModel(modelRemoteDB));
                    }
                    showcaseModelsLiveData.setValue(showcaseModels);
                })
                .addOnFailureListener(e -> {

                });

        return showcaseModelsLiveData;
    }

    public LiveData<List<PostModel>> getOrganizationShowcaseItems(String showcaseID){

        MutableLiveData<List<PostModel>> showcaseItemsLiveData = new MutableLiveData<>();

        FirebaseFirestore.getInstance()
                .collection("posts")
                .whereEqualTo("showcase", showcaseID)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.e("showcases", "loaded " + queryDocumentSnapshots.size() + " showcases");

                    List<PostModel> postModels = new ArrayList<>();
                    for(DocumentSnapshot documentSnapshot: queryDocumentSnapshots){

                        String type = documentSnapshot.getString("type");
                        if(type == null) continue;

                        switch (type){
                            case "event":
                                EventModelRemoteDB remoteEvent = documentSnapshot.toObject(EventModelRemoteDB.class);
                                if(remoteEvent == null) {
                                    continue;
                                }
                                remoteEvent.setId(documentSnapshot.getId());
                                for(int i = 0; i < 20; ++i) {
                                    postModels.add(eventModelConverter.convertRemoteDBToExternalModel(remoteEvent));
                                }
                                break;
                            case "article":
                                ArticleModelRemoteDB remoteArticle = documentSnapshot.toObject(ArticleModelRemoteDB.class);
                                if(remoteArticle == null) {
                                    continue;
                                }
                                remoteArticle.setId(documentSnapshot.getId());
                                postModels.add(articleModelConverter.convertRemoteDBToExternalModel(remoteArticle));
                                break;
                        }
                    }
                    showcaseItemsLiveData.setValue(postModels);
                })
                .addOnFailureListener(e -> {

                });

        return showcaseItemsLiveData;

    }
}
