package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.handler.AuthDataHandler;
import com.omada.junction.utils.taskhandler.LiveEvent;

public class SplashViewModel extends ViewModel {

    private final LiveData<LiveEvent<AuthDataHandler.AuthStatus>> authResultAction;
    private final LiveData<LiveEvent<AuthDataHandler.UserModel>> signedInUserAction;

    public SplashViewModel() {
        authResultAction = Transformations.map(
                DataRepository.getInstance()
                    .getAuthDataHandler()
                    .getAuthResponseNotifier(),

                authResponse->{
                    if(authResponse == null){
                        return null;
                    }

                    AuthDataHandler.AuthStatus receivedAuthResponse = authResponse.getDataOnceAndReset();
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
                        .getAuthDataHandler()
                        .getSignedInUserNotifier(),

                userModelLiveEvent->{
                    if(userModelLiveEvent == null){
                        return null;
                    }

                    AuthDataHandler.UserModel signedInUser = userModelLiveEvent.getDataOnceAndReset();
                    if(signedInUser == null) return null;
                    else return new LiveEvent<>(signedInUser);
                });
    }

    public void getCurrentUser(){
        DataRepository.getInstance()
                .getAuthDataHandler()
                .getCurrentUserFromDatabase();
    }

    public LiveData<LiveEvent<AuthDataHandler.AuthStatus>> getAuthResultAction() {
        return authResultAction;
    }

    public LiveData<LiveEvent<AuthDataHandler.UserModel>> getSignedInUserAction() {
        return signedInUserAction;
    }
}
