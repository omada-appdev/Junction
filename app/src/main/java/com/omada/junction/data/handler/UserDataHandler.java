package com.omada.junction.data.handler;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.data.models.InterestModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserDataHandler {

    public enum AuthStatus{

        CURRENT_USER_SUCCESS,
        CURRENT_USER_LOGIN_SUCCESS,
        CURRENT_USER_FAILURE,
        CURRENT_USER_LOGIN_FAILURE,

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

                    if(firebaseAuth.getCurrentUser() == null || firebaseAuth.getCurrentUser().getUid() == null){
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
            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(user.getUid())
                    .set(details.toMapObject())
                    .addOnSuccessListener(task -> {
                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.ADD_EXTRA_DETAILS_SUCCESS));
                    })
                    .addOnFailureListener(task-> authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.ADD_EXTRA_DETAILS_FAILURE)));
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
                        getAuthenticatedUserDetails();
                    } else {
                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.AUTHENTICATION_FAILURE));
                    }
                });
    }

    /*
    This method is for getting details of newly authenticated user
     */
    private void getAuthenticatedUserDetails(){

        prevUserUID = signedInUser.getUID();
        signedInUser.resetUser();

        FirebaseUser newUser = FirebaseAuth.getInstance().getCurrentUser();

        if(newUser == null){
            signedInUserNotifier.setValue(new LiveEvent<>(null));
        }
        else{

            signedInUser.setUID(newUser.getUid());
            signedInUser.setName(newUser.getDisplayName());
            signedInUser.setEmail(newUser.getEmail());
            signedInUser.setPhone(newUser.getPhoneNumber());

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(signedInUser.getUID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        signedInUser.setDateOfBirth(documentSnapshot.getTimestamp("dateOfBirth"));
                        signedInUser.setInstitute(documentSnapshot.getString("institute"));

                        signedInUser.setName(documentSnapshot.getString("name"));
                        signedInUser.setGender(documentSnapshot.getString("gender"));

                        try {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> interestsTempRaw = (List<Map<String, Object>>)documentSnapshot.get("interestsRating");
                            if(interestsTempRaw != null && interestsTempRaw.size()>0) {

                                List<InterestModel> interestsRating = new ArrayList<>(interestsTempRaw.size());

                                for (Map<String, Object> elem : interestsTempRaw) {

                                    InterestModel interestModel = new InterestModel((String) elem.get("interestsRating"));
                                    interestsRating.add(interestModel);
                                }
                                signedInUser.setInterests(interestsRating);
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

                                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_SUCCESS));
                                        signedInUserNotifier.setValue(new LiveEvent<>(signedInUser));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    })
                    .addOnFailureListener(e -> authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_FAILURE)));
        }
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
        if(user != null){

            prevUserUID = signedInUser.getUID();

            signedInUser.setUID(user.getUid());
            signedInUser.setName(user.getDisplayName());
            signedInUser.setEmail(user.getEmail());

            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_SUCCESS));
            getUserDetailsFromRemote();
        }
        else {
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_FAILURE));
        }
    }

    /*
    This method is called after login and getting details is done. ie, when firebase already has a current user.
    It sets details into the signed in user notifier live data
     */
    private void getUserDetailsFromRemote(){

        if(!signedInUser.getUID().equals("")){
            //TODO get data from local and then remote if that fails

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(signedInUser.getUID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        signedInUser.setName(documentSnapshot.getString("name"));
                        signedInUser.setEmail(documentSnapshot.getString("email"));
                        signedInUser.setPhone(documentSnapshot.getString("phone"));

                        signedInUser.setGender(documentSnapshot.getString("gender"));
                        signedInUser.setDateOfBirth(documentSnapshot.getTimestamp("dateOfBirth"));
                        signedInUser.setInstitute(documentSnapshot.getString("institute"));

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

                                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_LOGIN_SUCCESS));
                                        signedInUserNotifier.setValue(new LiveEvent<>(signedInUser));

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                    })
                    .addOnFailureListener(e -> authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_FAILURE)));

        }
        else{
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_LOGIN_FAILURE));
        }

    }

    public void signOutCurrentUser(){
        FirebaseAuth.getInstance().signOut();
        prevUserUID = signedInUser.getUID();
        signedInUser.resetUser();
    }

    public void updateCurrentUserDetails(UserModel updatedUserModel){

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(getCurrentUserModel().UID)
                .update(
                        "name", updatedUserModel.name,
                        "gender", updatedUserModel.gender,
                        "dateOfBirth", updatedUserModel.dateOfBirth,
                        "institute", updatedUserModel.institute
                )
                .addOnSuccessListener(aVoid -> {
                    Log.e("Update", "success");
                    signedInUser.dateOfBirth = updatedUserModel.dateOfBirth;
                    signedInUser.gender = updatedUserModel.gender;
                    signedInUser.name = updatedUserModel.name;
                    signedInUser.institute = updatedUserModel.institute;
                })
                .addOnFailureListener(e -> Log.e("Update", e.getMessage()));

    }

    public void updateFollow(String organizationID, boolean following) {

        if(following) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child("follows")
                    .child(getCurrentUserModel().getUID())
                    .child(organizationID)
                    .setValue(true);

            getCurrentUserModel().getFollowing().put(organizationID, true);
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
    public static class UserModel extends BaseModel {

        // Variables are package-private to prevent subclasses of MutableUserModel
        // gaining access to fields
        @NonNull String UID = "";
        String email;
        String name;
        String phone;

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

        public Map<String, Object> toMapObject(){

            Map<String, Object> mapUserModel = new HashMap<>();
            mapUserModel.put("institute", institute);
            mapUserModel.put("gender", gender);
            mapUserModel.put("dateOfBirth", dateOfBirth);
            mapUserModel.put("interestsRating", interestsRating);
            mapUserModel.put("name", name);
            mapUserModel.put("interests", interests);
            mapUserModel.put("email", email);

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
        }
    }
}
