package com.omada.junction.data.handler;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.omada.junction.data.BaseDataHandler;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.converter.InstituteModelConverter;
import com.omada.junction.data.models.external.InstituteModel;
import com.omada.junction.data.models.internal.remote.InstituteModelRemoteDB;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.HashMap;
import java.util.Map;


public class InstituteDataHandler extends BaseDataHandler {

    private static final Map<String, String> instituteHandleToIdCache = new HashMap<>();
    private static final Map<String, String> instituteIdToHandleCache = new HashMap<>();
    private final InstituteModelConverter instituteModelConverter = new InstituteModelConverter();

    public LiveData<LiveEvent<InstituteModel>> getInstituteDetails(String instituteID) {

        MutableLiveData<LiveEvent<InstituteModel>> instituteModelLiveData = new MutableLiveData<>();
        FirebaseFirestore
                .getInstance()
                .collection("institutes")
                .document(instituteID)
                .get()
                .addOnSuccessListener(snapshot -> {

                    InstituteModelRemoteDB modelRemoteDB = snapshot.toObject(InstituteModelRemoteDB.class);

                    if (modelRemoteDB == null) {
                        instituteModelLiveData.setValue(null);
                    } else {
                        modelRemoteDB.setId(snapshot.getId());
                        instituteModelLiveData.setValue(new LiveEvent<>(
                                instituteModelConverter.convertRemoteDBToExternalModel(modelRemoteDB)
                        ));
                    }

                })
                .addOnFailureListener(e -> {
                    Log.e("Institute", "Error retrieving institute details");
                    instituteModelLiveData.setValue(null);
                });
        return instituteModelLiveData;
    }

    public LiveData<LiveEvent<Boolean>> updateInstituteDetails(InstituteModel changedInstituteModel) {

        MutableLiveData<LiveEvent<Boolean>> resultLiveData = new MutableLiveData<>();
        String instituteId = DataRepository
                .getInstance()
                .getUserDataHandler()
                .getCurrentUserModel()
                .getInstitute();

        FirebaseFirestore
                .getInstance()
                .collection("institutes")
                .document(instituteId)
                .set(instituteModelConverter.convertExternalToRemoteDBModel(changedInstituteModel), SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    resultLiveData.setValue(new LiveEvent<>(true));
                })
                .addOnFailureListener(e -> {
                    resultLiveData.setValue(new LiveEvent<>(false));
                    Log.e("Institute", "Error updating institute details");
                });

        return resultLiveData;
    }

    public LiveData<LiveEvent<Boolean>> checkInstituteCodeValidity(String code) {

        MutableLiveData<LiveEvent<Boolean>> resultLiveData = new MutableLiveData<>();

        if (code != null) {

            if (instituteHandleToIdCache.get(code) != null) {
                resultLiveData.setValue(new LiveEvent<>(true));
                return resultLiveData;
            }

            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("instituteHandles")
                    .child(code)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.getValue() instanceof String) {
                                instituteHandleToIdCache.put(snapshot.getKey(), (String) snapshot.getValue());
                                resultLiveData.setValue(new LiveEvent<>(true));
                            } else {
                                resultLiveData.setValue(new LiveEvent<>(false));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            resultLiveData.setValue(new LiveEvent<>(false));
                            Log.e("Institute", "Error checking institute code validity");
                        }
                    });
        } else {
            resultLiveData.setValue(new LiveEvent<>(false));
        }

        return resultLiveData;
    }

    public LiveData<LiveEvent<String>> getInstituteId(String handle) {

        MutableLiveData<LiveEvent<String>> resultLiveData = new MutableLiveData<>();

        if (handle == null) {
            resultLiveData.setValue(new LiveEvent<>("notFound"));
        } else if (instituteHandleToIdCache.get(handle) != null) {
            resultLiveData.setValue(new LiveEvent<>(instituteHandleToIdCache.get(handle)));
        } else {
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("instituteHandles")
                    .child(handle)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.getValue() instanceof String) {
                                instituteHandleToIdCache.put(snapshot.getKey(), (String) snapshot.getValue());
                                instituteIdToHandleCache.put((String) snapshot.getValue(), snapshot.getKey());
                                resultLiveData.setValue(new LiveEvent<>((String) snapshot.getValue()));
                            } else {
                                resultLiveData.setValue(new LiveEvent<>("notFound"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            resultLiveData.setValue(new LiveEvent<>("notFound"));
                            Log.e("Institute", "Error checking institute code validity");
                        }
                    });
        }

        return resultLiveData;
    }

    public LiveData<LiveEvent<String>> getInstituteHandle(String id) {

        MutableLiveData<LiveEvent<String>> resultLiveData = new MutableLiveData<>();

        if (id == null) {
            resultLiveData.setValue(new LiveEvent<>("notFound"));
        } else if (instituteIdToHandleCache.get(id) != null) {
            resultLiveData.setValue(new LiveEvent<>(instituteIdToHandleCache.get(id)));
        } else {
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("instituteIds")
                    .child(id)
                    .addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.getValue() instanceof String) {
                                instituteHandleToIdCache.put(snapshot.getKey(), (String) snapshot.getValue());
                                instituteIdToHandleCache.put((String) snapshot.getValue(), snapshot.getKey());
                                resultLiveData.setValue(new LiveEvent<>((String) snapshot.getValue()));
                            } else {
                                resultLiveData.setValue(new LiveEvent<>("notFound"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            resultLiveData.setValue(new LiveEvent<>("notFound"));
                            Log.e("Institute", "Error checking institute code validity");
                        }
                    });
        }
        return resultLiveData;
    }

    public static String getCachedInstituteId(String handle) {
        return instituteHandleToIdCache.get(handle);
    }

    public static String getCachedInstituteHandle(String id) {
        return instituteIdToHandleCache.get(id);
    }

}
