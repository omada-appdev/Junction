package com.omada.junction.data.handler;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.data.models.InterestModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AuthDataHandler {

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
    private UserModelInternal signedInUser = new UserModelInternal();
    private String prevUserUID = "";

    //output fields to viewmodel or mid level layers
    MutableLiveData<LiveEvent<AuthStatus>> authResponseNotifier = new MutableLiveData<>();
    MutableLiveData<LiveEvent<UserModel>> signedInUserNotifier = new MutableLiveData<>();

    //state fields go here


    public AuthDataHandler(){

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
    call this in auth state listener to get extra user details from firestore with the UID
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
            signedInUser.setUserDisplayName(newUser.getDisplayName());
            signedInUser.setUserEmail(newUser.getEmail());
            signedInUser.setUserPhoneNumber(newUser.getPhoneNumber());

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(signedInUser.getUID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        signedInUser.setUserDateOfBirth(documentSnapshot.getTimestamp("userDateOfBirth"));
                        signedInUser.setUserInstitute(documentSnapshot.getString("userInstitute"));

                        signedInUser.setUserDisplayName(documentSnapshot.getString("userDisplayName"));
                        signedInUser.setUserGender(documentSnapshot.getString("userGender"));

                        try {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> interestsTempRaw = (List<Map<String, Object>>)documentSnapshot.get("userInterests");
                            if(interestsTempRaw != null && interestsTempRaw.size()>0) {

                                List<InterestModel> userInterests = new ArrayList<>(interestsTempRaw.size());

                                for (Map<String, Object> elem : interestsTempRaw) {

                                    InterestModel interestModel = new InterestModel((String) elem.get("interestString"));
                                    userInterests.add(interestModel);
                                }
                                signedInUser.setUserInterests(userInterests);
                            }

                        }
                        catch (Exception e){
                            signedInUser.setUserInterests(null);
                        }

                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.LOGIN_SUCCESS));
                        signedInUserNotifier.setValue(new LiveEvent<>(signedInUser));

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
    public void getCurrentUserFromDatabase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null && user.getUid() != null){

            prevUserUID = signedInUser.getUID();

            signedInUser.setUID(user.getUid());
            signedInUser.setUserDisplayName(user.getDisplayName());
            signedInUser.setUserEmail(user.getEmail());

            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_SUCCESS));
            getCurrentUserDetailsFromDatabase();
        }
        else {
            authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_FAILURE));
        }
    }

    /*
    This method is called after login and getting details is done. ie, when firebase already has a current user.
    It sets details into the signed in user notifier live data
     */
    private void getCurrentUserDetailsFromDatabase(){

        if(!signedInUser.getUID().equals("") && !(signedInUser == null)){
            //TODO get data from local and then remote if that fails

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(signedInUser.getUID())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {

                        signedInUser.setUserDateOfBirth(documentSnapshot.getTimestamp("userDateOfBirth"));
                        signedInUser.setUserInstitute(documentSnapshot.getString("userInstitute"));

                        signedInUser.setUserDisplayName(documentSnapshot.getString("userDisplayName"));
                        signedInUser.setUserGender(documentSnapshot.getString("userGender"));

                        try {
                            @SuppressWarnings("unchecked")
                            List<Map<String, Object>> interestsTempRaw = (List<Map<String, Object>>)documentSnapshot.get("userInterests");
                            if(interestsTempRaw != null && interestsTempRaw.size()>0) {

                                List<InterestModel> userInterests = new ArrayList<>(interestsTempRaw.size());

                                for (Map<String, Object> elem : interestsTempRaw) {

                                    InterestModel interestModel = new InterestModel((String) elem.get("interestString"));
                                    userInterests.add(interestModel);
                                }
                                signedInUser.setUserInterests(userInterests);
                            }

                        }
                        catch (Exception e){
                            signedInUser.setUserInterests(null);
                        }

                        authResponseNotifier.setValue(new LiveEvent<>(AuthStatus.CURRENT_USER_LOGIN_SUCCESS));
                        signedInUserNotifier.setValue(new LiveEvent<>(signedInUser));

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
                        "userDisplayName", updatedUserModel.userDisplayName,
                        "userGender", updatedUserModel.userGender,
                        "userDateOfBirth", updatedUserModel.userDateOfBirth
                )
                .addOnSuccessListener(aVoid -> {
                    Log.e("Update", "success");
                    signedInUser.userDateOfBirth = updatedUserModel.userDateOfBirth;
                    signedInUser.userGender = updatedUserModel.userGender;
                    signedInUser.userDisplayName = updatedUserModel.userDisplayName;
                    signedInUser.userInstitute = updatedUserModel.userInstitute;
                })
                .addOnFailureListener(e -> Log.e("Update", e.getMessage()));

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
        String userEmail;
        String userDisplayName;
        String userPhoneNumber;

        List<InterestModel> userInterests;
        List<String> userInterestsString;
        Timestamp userDateOfBirth;
        String userGender;
        String userInstitute;

        private UserModel(){}

        @NonNull
        public String getUID() {
            return UID;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public String getUserDisplayName() {
            return userDisplayName;
        }

        public String getUserPhoneNumber() {
            return userPhoneNumber;
        }

        public List<InterestModel> getUserInterests() {
            return userInterests;
        }

        public List<String> getUserInterestsString(){
            return userInterestsString;
        }

        public Timestamp getUserDateOfBirth() {
            return userDateOfBirth;
        }

        public String getUserGender() {
            return userGender;
        }

        public String getUserInstitute() {
            return userInstitute;
        }

        public Map<String, Object> toMapObject(){

            Map<String, Object> mapUserModel = new HashMap<>();
            mapUserModel.put("userInstitute", userInstitute);
            mapUserModel.put("userGender", userGender);
            mapUserModel.put("userDateOfBirth", userDateOfBirth);
            mapUserModel.put("userInterests", userInterests);
            mapUserModel.put("userDisplayName", userDisplayName);
            mapUserModel.put("userInterestsString", userInterestsString);

            return mapUserModel;
        }

    }

    /*
    this is for outside world to be able to make a user object and send it
    to auth handler but without being able to set a UID
    */
    public static class MutableUserModel extends UserModel{


        public void setUserEmail(String emailID) {
            this.userEmail = emailID;
        }

        public void setUserDisplayName(String displayName) {
            this.userDisplayName = displayName;
        }

        public void setUserPhoneNumber(String phoneNumber) {
            this.userPhoneNumber = phoneNumber;
        }

        public void setUserInterests(List<InterestModel> userInterestsModel) {

            this.userInterests = userInterestsModel;

            if(userInterests != null) {
                this.userInterestsString = new ArrayList<>(userInterests.size());
                for (InterestModel i : userInterests) {
                    userInterestsString.add(i.interestString);
                }
            }
        }

        public void setUserDateOfBirth(Timestamp userDOB) {
            this.userDateOfBirth = userDOB;
        }

        public void setUserGender(String userGender) {
            this.userGender = userGender;
        }

        public void setUserInstitute(String userInstitute) {
            this.userInstitute = userInstitute;
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
            userEmail = null;
            userDisplayName = null;
            userPhoneNumber = null;
            userInterests = null;
            userInterestsString = null;
            userDateOfBirth = null;
            userGender = null;
            userInstitute = null;
        }
    }
}
