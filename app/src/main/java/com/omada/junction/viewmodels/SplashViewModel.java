package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.omada.junction.application.JunctionApplication;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.utils.FileUtilities;
import com.omada.junction.utils.taskhandler.LiveEvent;

import me.shouheng.utils.UtilsApp;

public class SplashViewModel extends BaseViewModel {

    private final LiveData<LiveEvent<UserDataHandler.AuthStatus>> authResultAction;
    private final LiveData<LiveEvent<UserDataHandler.UserModel>> signedInUserAction;

    public SplashViewModel() {

        // clear all files on startup
        UtilsApp.init(JunctionApplication.getInstance());
        FileUtilities.Companion.clearTemporaryFiles();

        authResultAction = Transformations.map(
                DataRepository.getInstance()
                    .getUserDataHandler()
                    .getAuthResponseNotifier(),

                authResponse->{
                    if(authResponse == null){
                        return null;
                    }

                    UserDataHandler.AuthStatus receivedAuthResponse = authResponse.getDataOnceAndReset();
                    if(receivedAuthResponse == null) {
                        return null;
                    }
                    switch (receivedAuthResponse){
                        case CURRENT_USER_SUCCESS:
                        case CURRENT_USER_FAILURE:
                        case CURRENT_USER_LOGIN_SUCCESS:
                        case CURRENT_USER_LOGIN_FAILURE:
                            break;
                    }
                    return new LiveEvent<>(receivedAuthResponse);
                });

        signedInUserAction = Transformations.map(
                DataRepository.getInstance()
                        .getUserDataHandler()
                        .getSignedInUserNotifier(),

                userModelLiveEvent->{
                    if(userModelLiveEvent == null){
                        return null;
                    }

                    UserDataHandler.UserModel signedInUser = userModelLiveEvent.getDataOnceAndReset();
                    if(signedInUser == null) return null;
                    else return new LiveEvent<>(signedInUser);
                });
    }

    public void getCurrentUser(){
        DataRepository.getInstance()
                .getUserDataHandler()
                .getCurrentUserDetails();
    }

    public LiveData<LiveEvent<UserDataHandler.AuthStatus>> getAuthResultAction() {
        return authResultAction;
    }

    public LiveData<LiveEvent<UserDataHandler.UserModel>> getSignedInUserAction() {
        return signedInUserAction;
    }
}
