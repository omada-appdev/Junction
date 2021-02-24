package com.omada.junction.data.handler;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.omada.junction.data.BaseDataHandler;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.converter.ArticleModelConverter;
import com.omada.junction.data.models.converter.EventModelConverter;
import com.omada.junction.data.models.converter.RegistrationModelConverter;
import com.omada.junction.data.models.external.ArticleModel;
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.data.models.external.RegistrationModel;
import com.omada.junction.data.models.internal.remote.ArticleModelRemoteDB;
import com.omada.junction.data.models.internal.remote.EventModelRemoteDB;
import com.omada.junction.data.models.internal.remote.RegistrationModelRemoteDB;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;


public class PostDataHandler extends BaseDataHandler {

    private MutableLiveData<LiveEvent<List<PostModel>>> loadedInstituteHighlightsNotifier = new MutableLiveData<>();
    private MutableLiveData<LiveEvent<List<PostModel>>> loadedOrganizationHighlightsNotifier = new MutableLiveData<>();
    private MutableLiveData<LiveEvent<List<PostModel>>> loadedAllInstitutePostsNotifier = new MutableLiveData<>();
    private MutableLiveData<LiveEvent<List<PostModel>>> loadedAllOrganizationPostsNotifier = new MutableLiveData<>();


    public LiveData<LiveEvent<List<PostModel>>> getOrganizationHighlights(
            DataRepository.DataRepositoryAccessIdentifier accessIdentifier, String organizationID){

        MutableLiveData<LiveEvent<List<PostModel>>> loadedOrganizationHighlights = new MutableLiveData<>();

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .whereEqualTo("creator", organizationID)
                .whereNotEqualTo("organizationHighlight", false)
                .orderBy("organizationHighlight")
                .orderBy("timeCreated", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<PostModel> postModels = new ArrayList<>();

                    for(DocumentSnapshot snapshot: queryDocumentSnapshots){
                        PostModel postModel = convertSnapshotToPostModel(snapshot);
                        if(postModel != null){
                            postModels.add(postModel);
                        }
                    }

                    loadedOrganizationHighlights.setValue(new LiveEvent<>(postModels));

                })
                .addOnFailureListener(e -> {
                    Log.e("Posts", "Error retrieving organization highlights");
                    loadedOrganizationHighlights.setValue(null);
                });

        return loadedOrganizationHighlights;
    }

    public void getInstituteHighlights(
            DataRepository.DataRepositoryAccessIdentifier identifier){

        String instituteId = DataRepository
                .getInstance()
                .getUserDataHandler()
                .getCurrentUserModel()
                .getInstitute();

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .whereEqualTo("type", "event")
                .whereEqualTo("creatorCache.institute", instituteId)
                .whereGreaterThanOrEqualTo("startTime", Timestamp.now())
                .orderBy("startTime", Query.Direction.ASCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<PostModel> postModels = new ArrayList<>();

                    for(DocumentSnapshot snapshot: queryDocumentSnapshots){
                        PostModel postModel = convertSnapshotToPostModel(snapshot);
                        if(postModel != null){
                            postModels.add(postModel);
                        }
                    }
                    loadedInstituteHighlightsNotifier.setValue(new LiveEvent<>(postModels));
                })
                .addOnFailureListener(e -> {
                    Log.e("Posts", "Error retrieving organization highlights");
                    loadedInstituteHighlightsNotifier.setValue(null);
                });
    }

    public LiveData<LiveEvent<List<PostModel>>> getShowcasePosts(
            DataRepository.DataRepositoryAccessIdentifier identifier, String showcaseId) {

        MutableLiveData<LiveEvent<List<PostModel>>> loadedShowcasePostsLiveData = new MutableLiveData<>();

        FirebaseFirestore
                .getInstance()
                .collection("posts")
                .whereEqualTo("showcase", showcaseId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    List<PostModel> postModels = new ArrayList<>();

                    for(DocumentSnapshot snapshot: queryDocumentSnapshots){
                        PostModel postModel = convertSnapshotToPostModel(snapshot);
                        if(postModel != null){
                            postModels.add(postModel);
                        }
                    }

                    loadedShowcasePostsLiveData.setValue(new LiveEvent<>(postModels));

                })
                .addOnFailureListener(e -> {
                    Log.e("Posts", "Failed to retrieve showcase posts");
                    loadedShowcasePostsLiveData.setValue(null);
                });

        return loadedShowcasePostsLiveData;
    }

    public MutableLiveData<LiveEvent<List<PostModel>>> getLoadedInstituteHighlightsNotifier() {
        return loadedInstituteHighlightsNotifier;
    }

    public MutableLiveData<LiveEvent<List<PostModel>>> getLoadedAllInstitutePostsNotifier() {
        return loadedAllInstitutePostsNotifier;
    }

    public MutableLiveData<LiveEvent<List<PostModel>>> getLoadedOrganizationHighlightsNotifier() {
        return loadedOrganizationHighlightsNotifier;
    }

    public MutableLiveData<LiveEvent<List<PostModel>>> getLoadedAllOrganizationPostsNotifier() {
        return loadedAllOrganizationPostsNotifier;
    }

    /*
    Helper Fields
    If required, will be moved into a separate singleton class for use across handlers
     */

    private static final EventModelConverter eventModelConverter = new EventModelConverter();
    private static final ArticleModelConverter articleModelConverter = new ArticleModelConverter();
    private static final RegistrationModelConverter registrationModelConverter = new RegistrationModelConverter();

    private static PostModel convertSnapshotToPostModel(DocumentSnapshot snapshot){

        String type = snapshot.getString("type");

        if(type != null) {
            switch (type) {
                case "event":
                    return convertSnapshotToEventModel(snapshot);
                case "article":
                    return convertSnapshotToArticleModel(snapshot);
                default:
                    return null;
            }
        }
        return null;
    }

    private static EventModel convertSnapshotToEventModel(DocumentSnapshot snapshot){
        EventModelRemoteDB modelRemoteDB = snapshot.toObject(EventModelRemoteDB.class);
        if (modelRemoteDB == null){
            return null;
        }
        modelRemoteDB.setId(snapshot.getId());
        return eventModelConverter.convertRemoteDBToExternalModel(modelRemoteDB);
    }

    private static ArticleModel convertSnapshotToArticleModel(DocumentSnapshot snapshot){
        ArticleModelRemoteDB modelRemoteDB = snapshot.toObject(ArticleModelRemoteDB.class);
        if (modelRemoteDB == null){
            return null;
        }
        modelRemoteDB.setId(snapshot.getId());
        return articleModelConverter.convertRemoteDBToExternalModel(modelRemoteDB);
    }

    public static RegistrationModel convertSnapshotToRegistrationModel(DocumentSnapshot snapshot){
        RegistrationModelRemoteDB modelRemoteDB = snapshot.toObject(RegistrationModelRemoteDB.class);
        if (modelRemoteDB == null){
            return null;
        }
        modelRemoteDB.setId(snapshot.getId());
        return registrationModelConverter.convertRemoteDBToExternalModel(modelRemoteDB);
    }

    public void resetLastInstituteHighlight() {
    }
}
