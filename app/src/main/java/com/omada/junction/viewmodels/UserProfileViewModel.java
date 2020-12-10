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

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> institute = new MutableLiveData<>();
    public MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();

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

        name.setValue(currentUserModel.getName());
        institute.setValue(currentUserModel.getInstitute());
        dateOfBirth.setValue(TransformUtilities.convertTimestampToDDMMYYYY(currentUserModel.getDateOfBirth()));
        gender.setValue(currentUserModel.getGender());
        email.setValue(currentUserModel.getEmail());
    }


    public void updateUserDetails(){

        UserDataHandler.MutableUserModel mutableUserModel = new UserDataHandler.MutableUserModel();

        DataValidator validator = new DataValidator();
        AtomicBoolean anyDetailsEntryInvalid = new AtomicBoolean(false);

        validator.validateName(name.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                mutableUserModel.setName(name.getValue());
            }
            else anyDetailsEntryInvalid.set(true);
        });

        validator.validateGender(gender.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                mutableUserModel.setName(name.getValue());
            }
            else anyDetailsEntryInvalid.set(true);
        });

        validator.validateDateOfBirth(dateOfBirth.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                mutableUserModel.setDateOfBirth(
                        new Timestamp(
                                TransformUtilities.convertDDMMYYYYtoDate(dateOfBirth.getValue(), "/")
                        )
                );
            }
            else anyDetailsEntryInvalid.set(true);
        });

        validator.validateInstitute(institute.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                mutableUserModel.setInstitute(institute.getValue());
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
