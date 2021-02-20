package com.omada.junction.data.handler;


import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.omada.junction.BuildConfig;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.external.InterestModel;
import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserDataHandler {

    public enum AuthStatus{

        CURRENT_USER_SUCCESS,
        CURRENT_USER_FAILURE,

        AUTHENTICATION_SUCCESS,
        AUTHENTICATION_FAILURE,
        LOGIN_SUCCESS,
        LOGIN_FAILURE,

        SIGNUP_SUCCESS,
        SIGNUP_FAILURE,
        ADD_EXTRA_DETAILS_SUCCESS,
        ADD_EXTRA_DETAILS_FAILURE,

        UPDATE_USER_DETAILS_SUCCESS,
        UPDATE_USER_DETAILS_FAILURE,

        USER_TOKEN_EXPIRED,
        USER_SIGNED_OUT
    }

    //to be used as a cache to check for changes
    private final UserModelInternal signedInUser = new UserModelInternal();
    private String prevUserUID = "";

    //output fields to view model or mid level layers
    MutableLiveData<LiveEvent<AuthStatus>> authResponseNotifier = new MutableLiveData<>();
    MutableLiveData<LiveEvent<UserModel>> signedInUserNotifier = new MutableLiveData<>();

    //state fields go here


    public UserDataHandler(){

        /*
        this is only to check for sign outs and token expiration (if needed)
         */
        FirebaseAuth.getInstance()
                .addAuthStateListener(firebaseAuth -> {

                    if(firebaseAuth.getCurrentUser() == null){
                        prevUserUID = signedInUser.getUID();
                        signedInUser.resetUser();
                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.USER_SIGNED_OUT));

                    }

                });
    }

    public void createNewUserWithEmailAndPassword(String email, String password, MutableUserModel details){

        FirebaseAuth auth = FirebaseAuth.getInstance();
        /*
        signing out so that all tasks related to sign out can be handled by the auth state listeners
        TODO register auth state listeners in a suitable place
        */
        auth.signOut();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Log.d("AUTH", "create new user successful");
                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.SIGNUP_SUCCESS));
                        addCreatedUserDetails(details);
                    }
                    else{
                        Log.d("AUTH", "create new user unsuccessful");
                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.SIGNUP_FAILURE));
                    }
                });

    }

    //to be called in createNewUser when it is successful and not anywhere else
    private void addCreatedUserDetails(UserModel details){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            /*
            do not use the local cached variable named signed in user because it might not yet be updated by the
            auth state listener and do not update it from here because the auth state listener will take care of it
            */
            DataRepository
                    .getInstance()
                    .getImageUploadHandler()
                    .uploadProfilePictureWithTask(details.profilePicturePath, user.getUid())
                    .addOnCompleteListener(uri -> {
                        details.profilePicture = uri.getResult().toString();
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(user.getUid())
                                .set(details.toMapObject())
                                .addOnSuccessListener(task -> {
                                    authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.ADD_EXTRA_DETAILS_SUCCESS));
                                })
                                .addOnFailureListener(task -> authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.ADD_EXTRA_DETAILS_FAILURE)));
                    });
        }
    }

    /*
    use it through authResponse LiveData by attaching a transformation or an observer and use the
    sign in data in auth state listener
    */
    public void authenticateUser(final String email, final String password){

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.AUTHENTICATION_SUCCESS));
                        getCurrentUserDetails();
                    } else {
                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.AUTHENTICATION_FAILURE));
                    }
                });
    }

    public UserModel getCurrentUserModel(){
        return signedInUser;
    }

    /*
    this method triggers a get user through firebase auth that changes the value in signed in user
    notifier live data through the auth state listener callback
     */
    public void getCurrentUserDetails(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.getUid().equals("")) {
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_SUCCESS));
            signedInUser.setUID(user.getUid());
            LiveData<Boolean> localResultLiveData = getUserDetailsFromLocal(user.getUid());
            localResultLiveData.observeForever(new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean result) {
                    if (result != null) {
                        if (!result) {
                            getUserDetailsFromRemote(user.getUid());
                        }
                        localResultLiveData.removeObserver(this);
                    }
                }
            });
        } else {
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_FAILURE));
        }
    }

    /*
       To be called when the user data is to be reset completely, ie, the cache is stale
    */
    public void getCurrentUserDetailsFromRemote() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !user.getUid().equals("")) {
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_SUCCESS));
            getUserDetailsFromRemote(user.getUid());
        } else {
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_FAILURE));
        }
    }

    /*
    This method is called after login and getting details is done. ie, when firebase already has a current user.
    It sets details into the signed in user notifier live data
     */
    private LiveData<Boolean> getUserDetailsFromRemote(String uid){

        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();

        if(!signedInUser.getUID().equals("")){
            //TODO get data from local and then remote if that fails

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .get(Source.SERVER)
                    .addOnSuccessListener(documentSnapshot -> {

                        if(!documentSnapshot.exists()) {
                            resultLiveData.setValue(false);
                            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.USER_TOKEN_EXPIRED));
                            return;
                        }

                        signedInUser.setUID(uid);
                        signedInUser.setName(documentSnapshot.getString("name"));
                        signedInUser.setEmail(documentSnapshot.getString("email"));
                        signedInUser.setPhone(documentSnapshot.getString("phone"));

                        signedInUser.setGender(documentSnapshot.getString("gender"));
                        signedInUser.setDateOfBirth(documentSnapshot.getTimestamp("dateOfBirth"));
                        signedInUser.setInstitute(documentSnapshot.getString("institute"));
                        signedInUser.setProfilePicture(documentSnapshot.getString("profilePicture"));

                        try {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> interestsTempRaw = (List<Map<String, Object>>)documentSnapshot.get("interestsRating");
                            if(interestsTempRaw != null && interestsTempRaw.size()>0) {

                                List<InterestModel> userInterests = new ArrayList<>(interestsTempRaw.size());

                                for (Map<String, Object> elem : interestsTempRaw) {

                                    InterestModel interestModel = new InterestModel((String) elem.get("interests"));
                                    userInterests.add(interestModel);
                                }
                                signedInUser.setInterests(userInterests);
                            }

                        }
                        catch (Exception e){
                            signedInUser.setInterests(null);
                        }

                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("follows")
                                .child(signedInUser.getUID())
                                .addValueEventListener(new ValueEventListener(){

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        HashMap<String, Object> dataMap = new HashMap<>();
                                        for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                            dataMap.put(childSnapshot.getKey(), childSnapshot.getValue());
                                        }
                                        signedInUser.following = dataMap;

                                        Log.e("User", "Retrieved details from remote");

                                        resultLiveData.setValue(true);
                                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_SUCCESS));
                                        signedInUserNotifier.setValue(new LiveEvent<>(signedInUser));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        resultLiveData.setValue(false);
                                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_FAILURE));
                                    }
                                });

                    })
                    .addOnFailureListener(e -> {
                        resultLiveData.setValue(false);
                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_FAILURE));
                    });

        }
        else{
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_FAILURE));
        }

        return resultLiveData;
    }

    private LiveData<Boolean> getUserDetailsFromLocal(String uid){

        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();

        if(!signedInUser.getUID().equals("")){

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .get(Source.CACHE)
                    .addOnSuccessListener(documentSnapshot -> {

                        if(!documentSnapshot.exists()) {
                            resultLiveData.setValue(false);
                            return;
                        }

                        signedInUser.setUID(uid);
                        signedInUser.setName(documentSnapshot.getString("name"));
                        signedInUser.setEmail(documentSnapshot.getString("email"));
                        signedInUser.setPhone(documentSnapshot.getString("phone"));

                        signedInUser.setGender(documentSnapshot.getString("gender"));
                        signedInUser.setDateOfBirth(documentSnapshot.getTimestamp("dateOfBirth"));
                        signedInUser.setInstitute(documentSnapshot.getString("institute"));
                        signedInUser.setProfilePicture(documentSnapshot.getString("profilePicture"));

                        try {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> interestsTempRaw = (List<Map<String, Object>>)documentSnapshot.get("interestsRating");
                            if(interestsTempRaw != null && interestsTempRaw.size()>0) {

                                List<InterestModel> userInterests = new ArrayList<>(interestsTempRaw.size());

                                for (Map<String, Object> elem : interestsTempRaw) {

                                    InterestModel interestModel = new InterestModel((String) elem.get("interests"));
                                    userInterests.add(interestModel);
                                }
                                signedInUser.setInterests(userInterests);
                            }

                        }
                        catch (Exception e){
                            signedInUser.setInterests(null);
                        }

                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("follows")
                                .child(signedInUser.getUID())
                                .addValueEventListener(new ValueEventListener(){

                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        HashMap<String, Object> dataMap = new HashMap<>();
                                        for (DataSnapshot childSnapshot: snapshot.getChildren()) {
                                            dataMap.put(childSnapshot.getKey(), childSnapshot.getValue());
                                        }
                                        signedInUser.following = dataMap;

                                        Log.e("User", "Retrieved details from local");
                                        resultLiveData.setValue(true);

                                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_SUCCESS));
                                        signedInUserNotifier.setValue(new LiveEvent<>(signedInUser));
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });

                    })
                    .addOnFailureListener(e -> {
                        resultLiveData.setValue(false);
                    });

        }
        else{
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_FAILURE));
        }

        return resultLiveData;

    }

    public void signOutCurrentUser(){
        FirebaseAuth.getInstance().signOut();
        prevUserUID = signedInUser.getUID();
        signedInUser.resetUser();
    }

    public void updateCurrentUserDetails(UserModel updatedUserModel){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (BuildConfig.DEBUG && !(user != null && user.getUid() != null && !user.getUid().equals(""))) {
            throw new AssertionError("Assertion failed");
        }

        // Filesystem path
        Uri newProfilePicture = updatedUserModel.getProfilePicturePath();
        if (newProfilePicture != null) {
            DataRepository.getInstance()
                    .getImageUploadHandler()
                    .uploadProfilePictureWithTask(newProfilePicture, user.getUid())
                    .addOnCompleteListener(uri -> {
                        String httpUrl = uri.getResult().toString();
                        updatedUserModel.profilePicture = httpUrl;
                        updateDatabaseDetails(updatedUserModel, httpUrl);
                    });
        }
        else {
            updateDatabaseDetails(updatedUserModel, null);
        }

    }

    private void updateDatabaseDetails(UserModel updatedUserModel, @Nullable String httpUrl) {

        Map<String, Object> updates = new HashMap<>();

        updates.put("name", updatedUserModel.name);
        updates.put("gender", updatedUserModel.gender);
        updates.put("dateOfBirth", updatedUserModel.dateOfBirth);
        updates.put("institute", updatedUserModel.institute);

        if(httpUrl != null) {
            updates.put("profilePicture", httpUrl);
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(getCurrentUserModel().UID)
                .update(updates)
                .addOnSuccessListener(aVoid -> {
                    Log.e("Update", "success");
                    signedInUser.dateOfBirth = updatedUserModel.dateOfBirth;
                    signedInUser.gender = updatedUserModel.gender;
                    signedInUser.name = updatedUserModel.name;
                    signedInUser.institute = updatedUserModel.institute;
                    if(updatedUserModel.profilePicture != null) {
                        signedInUser.profilePicture = updatedUserModel.profilePicture;
                    }

                    signedInUserNotifier.setValue(new LiveEvent<>(signedInUser));
                    authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.UPDATE_USER_DETAILS_SUCCESS));
                })
                .addOnFailureListener(e -> {
                    Log.e("Update", e.getMessage());
                    authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.UPDATE_USER_DETAILS_FAILURE));
                });
    }

    public LiveData<LiveEvent<Boolean>> sendPasswordResetLink(String email) {

        MutableLiveData<LiveEvent<Boolean>> result = new MutableLiveData<>();

        // Just an extra layer of protection
        DataValidator dataValidator = new DataValidator();
        dataValidator.validateEmail(email, dataValidationInformation -> {
            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                result.setValue(new LiveEvent<>(false));
                return;
            }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnSuccessListener(aVoid -> {
                        result.setValue(new LiveEvent<>(true));
                    })
                    .addOnFailureListener(e -> {
                        Log.e("User", "Password reset link failure");
                        e.getMessage();
                        result.setValue(new LiveEvent<>(false));
                    });
        });
        return result;
    }

    public void updateFollow(String organizationID, boolean following) {

        if(following) {
            getCurrentUserModel().getFollowing().put(organizationID, true);

            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("follows")
                    .child(getCurrentUserModel().getUID())
                    .child(organizationID)
                    .setValue(true);
        }
        else {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("follows")
                    .child(getCurrentUserModel().getUID())
                    .child(organizationID)
                    .removeValue();
            getCurrentUserModel().getFollowing().remove(organizationID);
        }
    }

    /*
    shares if auth request was success or failure
    */
    public LiveData<LiveEvent<AuthStatus>> getAuthResponseNotifier(){
        return authResponseNotifier;
    }

    /*
        to be used when the signed in user changes
        this is distinct to auth response as that only shares status of request
        this shares result of request ie, current user or null
        */
    public LiveData<LiveEvent<UserModel>> getSignedInUserNotifier(){
        return signedInUserNotifier;
    }

    /*
    to be used to handle the case when signed in user changed or if you just want access to previous
    user
     */
    public String getPrevUserUID() {
        return prevUserUID;
    }

    /*
    this class is shown to outside world there is only one instance of this
    class and the derived class they contain all the data needed
    */
    public static class UserModel implements Serializable {

        // Variables are package-private to prevent subclasses of MutableUserModel
        // gaining access to fields
        @NonNull String UID = "";
        String email;
        String name;
        String phone;
        String profilePicture;

        protected Uri profilePicturePath = null;

        List<InterestModel> interestsRating;
        List<String> interests;

        Timestamp dateOfBirth;
        String gender;
        String institute;

        // get this from realtime database
        Map<String, Object> following;

        private UserModel(){}

        @NonNull
        public String getUID() {
            return UID;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public List<InterestModel> getInterestsRating() {
            return interestsRating;
        }

        public Map<String, Object> getFollowing() {
            return following;
        }

        public List<String> getInterests(){
            return interests;
        }

        public Timestamp getDateOfBirth() {
            return dateOfBirth;
        }

        public String getGender() {
            return gender;
        }

        public String getInstitute() {
            return institute;
        }

        public String getProfilePicture() {
            return profilePicture;
        }

        public Uri getProfilePicturePath() {
            return profilePicturePath;
        }

        public Map<String, Object> toMapObject(){

            Map<String, Object> mapUserModel = new HashMap<>();
            mapUserModel.put("institute", institute);
            mapUserModel.put("gender", gender);
            mapUserModel.put("dateOfBirth", dateOfBirth);
            mapUserModel.put("interestsRating", interestsRating);
            mapUserModel.put("name", name);
            mapUserModel.put("interests", interests);
            mapUserModel.put("email", email);
            mapUserModel.put("profilePicture", profilePicture);

            return mapUserModel;
        }

    }

    /*
    this is for outside world to be able to make a user object and send it
    to auth handler but without being able to set a UID
    */
    public static class MutableUserModel extends UserModel{

        public void setEmail(String email) {
            this.email = email;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setInterests(List<InterestModel> interestsRating) {

            this.interestsRating = interestsRating;

            if(this.interestsRating != null) {
                this.interests = new ArrayList<>(this.interestsRating.size());
                for (InterestModel i : this.interestsRating) {
                    interests.add(i.interestString);
                }
            }
        }

        public void setDateOfBirth(Timestamp dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public void setInstitute(String institute) {
            this.institute = institute;
        }

        public void setProfilePicture(String profilePicture) {
            this.profilePicture = profilePicture;
        }

        public void setProfilePicturePath(Uri profilePicturePath) {
            this.profilePicturePath = profilePicturePath;
        }

    }

    /*
    This is for internal use and under no instance must it be used outside this class
     */
    private static class UserModelInternal extends MutableUserModel{

        public void setUID(@NonNull String UID) {
            this.UID = UID;
        }

        public void resetUser(){
            UID = "";
            email = null;
            name = null;
            phone = null;
            interestsRating = null;
            interests = null;
            dateOfBirth = null;
            gender = null;
            institute = null;
            profilePicture = null;
        }
    }
}
