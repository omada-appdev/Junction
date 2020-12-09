package com.omada.junction.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.firebase.Timestamp;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.utils.taskhandler.LiveEvent;
import com.omada.junction.utils.transform.TransformUtilities;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserProfileViewModel extends ViewModel {


    private final MutableLiveData<LiveEvent<Boolean>> editProfileTrigger = new MutableLiveData<>();
    private final LiveData<LiveEvent<UserDataHandler.AuthStatus>> signOutTrigger;
    private final MutableLiveData<LiveEvent<DataValidator.DataValidationInformation>> dataValidationAction = new MutableLiveData<>();

    public MutableLiveData<String> userDisplayName = new MutableLiveData<>();
    public MutableLiveData<String> userInstitute = new MutableLiveData<>();
    public MutableLiveData<String> userDateOfBirth = new MutableLiveData<>();
    public MutableLiveData<String> userGender = new MutableLiveData<>();
    public MutableLiveData<String> userEmail = new MutableLiveData<>();

    public UserProfileViewModel(){

        signOutTrigger = Transformations.map(
                DataRepository.getInstance()
                .getUserDataHandler()
                .getAuthResponseNotifier(),

                authStatusLiveEvent -> authStatusLiveEvent
        );

        UserDataHandler.UserModel currentUserModel = DataRepository.getInstance()
                .getUserDataHandler()
                .getCurrentUserModel();

        userDisplayName.setValue(currentUserModel.getUserDisplayName());
        userInstitute.setValue(currentUserModel.getUserInstitute());
        userDateOfBirth.setValue(TransformUtilities.convertTimestampToDDMMYYYY(currentUserModel.getUserDateOfBirth()));
        userGender.setValue(currentUserModel.getUserGender());
        userEmail.setValue(currentUserModel.getUserEmail());
    }


    public void updateUserDetails(){

        UserDataHandler.MutableUserModel mutableUserModel = new UserDataHandler.MutableUserModel();

        DataValidator validator = new DataValidator();
        AtomicBoolean anyDetailsEntryInvalid = new AtomicBoolean(false);

        validator.validateName(userDisplayName.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                mutableUserModel.setUserDisplayName(userDisplayName.getValue());
            }
            else anyDetailsEntryInvalid.set(true);
        });

        validator.validateGender(userGender.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                mutableUserModel.setUserDisplayName(userDisplayName.getValue());
            }
            else anyDetailsEntryInvalid.set(true);
        });

        validator.validateDateOfBirth(userDateOfBirth.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                mutableUserModel.setUserDateOfBirth(
                        new Timestamp(
                                TransformUtilities.convertDDMMYYYYtoDate(userDateOfBirth.getValue(), "/")
                        )
                );
            }
            else anyDetailsEntryInvalid.set(true);
        });

        validator.validateInstitute(userInstitute.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                mutableUserModel.setUserInstitute(userInstitute.getValue());
            }
            else anyDetailsEntryInvalid.set(true);
        });

        if(!anyDetailsEntryInvalid.get()) {
            DataRepository.getInstance()
                    .getUserDataHandler()
                    .updateCurrentUserDetails(mutableUserModel);

            dataValidationAction.setValue(new LiveEvent<>(
                    new DataValidator.DataValidationInformation(
                        DataValidator.DataValidationPoint.VALIDATION_POINT_ALL,
                        DataValidator.DataValidationResult.VALIDATION_RESULT_VALID
                    )
            ));
        }
    }

    public void goToEditProfile(){
        editProfileTrigger.setValue(new LiveEvent<>(true));
    }

    public void goToAutofillSettings(){

    }

    public void goToInterests(){

    }

    public void goToStatistics(){

    }

    public void signOutUser(){
        DataRepository.getInstance().getUserDataHandler().signOutCurrentUser();
    }

    public LiveData<LiveEvent<Boolean>> getEditProfileTrigger() {
        return editProfileTrigger;
    }

    public UserDataHandler.UserModel getCurrentUserModel() {
        return DataRepository.getInstance()
                .getUserDataHandler()
                .getCurrentUserModel();
    }

    public MutableLiveData<LiveEvent<DataValidator.DataValidationInformation>> getDataValidationAction() {
        return dataValidationAction;
    }

    public LiveData<LiveEvent<UserDataHandler.AuthStatus>> getSignOutTrigger() {
        return signOutTrigger;
    }


}
