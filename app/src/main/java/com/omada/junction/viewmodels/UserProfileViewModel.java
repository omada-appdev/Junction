package com.omada.junction.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.handler.InstituteDataHandler;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.utils.taskhandler.LiveEvent;
import com.omada.junction.utils.TransformUtilities;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;

public class UserProfileViewModel extends BaseViewModel {

    private LiveData<LiveEvent<UserDataHandler.AuthStatus>> authStatusTrigger;

    // No live event because the changes should always be subscribed to by all observers
    private LiveData<UserDataHandler.UserModel> userUpdateAction;

    private final MutableLiveData<LiveEvent<Boolean>> editProfileTrigger = new MutableLiveData<>();
    private UserDataHandler.UserModel currentUserModel;

    public MutableLiveData<String> name = new MutableLiveData<>();
    public MutableLiveData<String> institute = new MutableLiveData<>();
    public MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    public MutableLiveData<String> gender = new MutableLiveData<>();
    public MutableLiveData<Uri> profilePicture = new MutableLiveData<>();

    public UserProfileViewModel(){

        initCalendar();

        // TODO since multiple possible observers, remember to make it thread safe just in case
        userUpdateAction = Transformations.map(
                DataRepository.getInstance()
                        .getUserDataHandler()
                        .getSignedInUserNotifier(),

                userModelLiveEvent -> {
                    if(userModelLiveEvent == null) {
                        return null;
                    }
                    UserDataHandler.UserModel temp = userModelLiveEvent.getDataOnceAndReset();
                    if(temp != null) {
                        currentUserModel = temp;
                        return currentUserModel;
                    }
                    return null;
                }
        );

        authStatusTrigger = Transformations.map(
                DataRepository.getInstance()
                .getUserDataHandler()
                .getAuthResponseNotifier(),

                authStatusLiveEvent -> authStatusLiveEvent
        );

        currentUserModel = DataRepository.getInstance()
                .getUserDataHandler()
                .getCurrentUserModel();

        name.setValue(currentUserModel.getName());
        institute.setValue(currentUserModel.getInstitute());
        dateOfBirth.setValue(TransformUtilities.convertTimestampToDDMMYYYY(currentUserModel.getDateOfBirth()));
        gender.setValue(currentUserModel.getGender());
    }


    public void updateUserDetails(){

        DataValidator dataValidator = getDataValidator();

        UserDataHandler.MutableUserModel updatedUserModel = new UserDataHandler.MutableUserModel();
        MediatorLiveData<DataValidator.DataValidationInformation> resultLiveData = new MediatorLiveData<>();

        ValidationAggregator validationAggregator = ValidationAggregator
                .build(resultLiveData)
                .add(DataValidator.DataValidationPoint.VALIDATION_POINT_INSTITUTE_HANDLE)
                .add(DataValidator.DataValidationPoint.VALIDATION_POINT_GENDER)
                .add(DataValidator.DataValidationPoint.VALIDATION_POINT_DATE_OF_BIRTH)
                .add(DataValidator.DataValidationPoint.VALIDATION_POINT_NAME)
                .get();

        dataValidator.validateDateOfBirth(dateOfBirth.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                updatedUserModel.setDateOfBirth(
                        new Timestamp(TransformUtilities.convertDDMMYYYYtoDate(dateOfBirth.getValue(), "/"))
                );
            }
            validationAggregator.holdData(DataValidator.DataValidationPoint.VALIDATION_POINT_DATE_OF_BIRTH, dataValidationInformation);
            notifyValidity(dataValidationInformation);
        });

        dataValidator.validateGender(gender.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                updatedUserModel.setGender(gender.getValue());
            }
            validationAggregator.holdData(DataValidator.DataValidationPoint.VALIDATION_POINT_GENDER, dataValidationInformation);
            notifyValidity(dataValidationInformation);
        });

        dataValidator.validateInstitute(institute.getValue(), dataValidationInformation -> {

            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                LiveData<LiveEvent<String>> instituteId = DataRepository
                        .getInstance()
                        .getInstituteDataHandler()
                        .getInstituteId(institute.getValue());

                instituteId.observeForever(new Observer<LiveEvent<String>>() {
                    @Override
                    public void onChanged(LiveEvent<String> stringLiveEvent) {
                        if (stringLiveEvent == null) {
                            return;
                        }
                        String result = stringLiveEvent.getDataOnceAndReset();
                        DataValidator.DataValidationInformation newValidationInformation;

                        if (result != null && !result.equals("notFound")) {
                            updatedUserModel.setInstitute(result);
                            newValidationInformation = new DataValidator.DataValidationInformation(
                                    DataValidator.DataValidationPoint.VALIDATION_POINT_INSTITUTE_HANDLE,
                                    DataValidator.DataValidationResult.VALIDATION_RESULT_VALID
                            );
                        } else {
                            newValidationInformation = new DataValidator.DataValidationInformation(
                                    DataValidator.DataValidationPoint.VALIDATION_POINT_INSTITUTE_HANDLE,
                                    DataValidator.DataValidationResult.VALIDATION_RESULT_INVALID
                            );
                        }

                        validationAggregator.holdData(
                                DataValidator.DataValidationPoint.VALIDATION_POINT_INSTITUTE_HANDLE,
                                newValidationInformation
                        );
                        notifyValidity(newValidationInformation);
                        instituteId.removeObserver(this);
                    }
                });
            }
            else notifyValidity(dataValidationInformation);
        });

        dataValidator.validateName(name.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                updatedUserModel.setName(
                        name.getValue()
                );
            }
            validationAggregator.holdData(DataValidator.DataValidationPoint.VALIDATION_POINT_NAME, dataValidationInformation);
            notifyValidity(dataValidationInformation);
        });

        resultLiveData.observeForever(new Observer<DataValidator.DataValidationInformation>() {
            @Override
            public void onChanged(DataValidator.DataValidationInformation dataValidationInformation) {

                if (dataValidationInformation.getValidationPoint() != DataValidator.DataValidationPoint.VALIDATION_POINT_ALL) {
                    return;
                }

                notifyValidity(dataValidationInformation);

                if (dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                    updatedUserModel.setProfilePicturePath(profilePicture.getValue());
                    Log.e("UpdateUser", "Added details to firebase");
                    DataRepository.getInstance()
                            .getUserDataHandler()
                            .updateCurrentUserDetails(updatedUserModel);
                }
                resultLiveData.removeObserver(this);
            }
        });

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

    public LiveData<UserDataHandler.UserModel> getUserUpdateAction() {
        return userUpdateAction;
    }

    public LiveData<LiveEvent<UserDataHandler.AuthStatus>> getAuthStatusTrigger() {
        return authStatusTrigger;
    }

    public UserDataHandler.UserModel getUserModel() {
        return currentUserModel;
    }

    private long endTime;
    private long startTime;

    private void initCalendar() {
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.setTimeInMillis(today);

        calendar.roll(Calendar.YEAR, -5);
        endTime = calendar.getTimeInMillis();

        calendar.roll(Calendar.YEAR, -70);
        startTime = calendar.getTimeInMillis();
    }

    public MaterialDatePicker.Builder<?> setupDateSelectorBuilder() {

        int inputMode = MaterialDatePicker.INPUT_MODE_CALENDAR;

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setSelection(endTime);
        builder.setInputMode(inputMode);

        return builder;
    }

    public CalendarConstraints.Builder setupConstraintsBuilder() {

        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();

        constraintsBuilder.setStart(startTime);
        constraintsBuilder.setEnd(endTime);
        constraintsBuilder.setOpenAt(endTime);

        return constraintsBuilder;
    }
}
