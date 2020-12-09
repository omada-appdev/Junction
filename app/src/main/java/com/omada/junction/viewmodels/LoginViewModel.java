package com.omada.junction.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.Timestamp;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.data.models.InterestModel;
import com.omada.junction.ui.login.LoginActivity;
import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.utils.taskhandler.LiveEvent;
import com.omada.junction.utils.transform.TransformUtilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;


public class LoginViewModel extends ViewModel {

    //constant fields
    public enum DataValidationPoint{
        ALL_DETAILS_VALID,
        ALL_INTERESTS_VALID,
        ALL_SIGN_IN_INFO_VALID,
        NO_INFO,
        BLANK_VALUE,
        INVALID_EMAIL,
        INVALID_PASSWORD,
        INVALID_DATE_OF_BIRTH,
        INVALID_DATE_OF_BIRTH_PARSE_ERROR,
        INVALID_DATE_OF_BIRTH_OUT_OF_RANGE,
        INVALID_FULL_NAME,
        INVALID_FULL_NAME_HAS_NUMBERS,
        INVALID_INSTITUTE,
        INVALID_GENDER,
        INVALID_INTERESTS,
        INVALID_INTERESTS_NONE_SELECTED,
        INVALID_INTERESTS_MANY_SELECTED
    }

    private final static int MAX_INTERESTS = 5;

    //action fields (setting values triggers events)
    private final LiveData<LiveEvent<UserDataHandler.AuthStatus>> authResultAction;
    private final MutableLiveData<LiveEvent<DataValidator.DataValidationInformation>> dataValidationAction = new MutableLiveData<>();
    private final MutableLiveData<LiveEvent<LoginActivity.FragmentIdentifier>> fragmentChangeAction = new MutableLiveData<>();
    private final MutableLiveData<LiveEvent<Boolean>> goToFeedAction = new MutableLiveData<>();
    private final MutableLiveData<LiveEvent<String>> toastMessageAction = new MutableLiveData<>();

    //data fields from UI
    public final MutableLiveData<String> userDisplayName = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    public final MutableLiveData<String> email = new MutableLiveData<>();
    public final MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    public final MutableLiveData<String> userGender = new MutableLiveData<>();
    public final MutableLiveData<String> userInstitute = new MutableLiveData<>();

    private final List<InterestModel> selectedInterests = new ArrayList<>();
    private final List<InterestModel> allInterests = new ArrayList<>();

    private String profilePhotoUrl;

    //process fields (keep track of state)
    private final DataValidator dataValidator = new DataValidator();

    public LoginViewModel() {

        /*
        Observe this field in UI level to provide feedback to user
         */

        initCalendar();

        authResultAction = Transformations.map(
                DataRepository.getInstance().getUserDataHandler().getAuthResponseNotifier(),
                authResponse -> {

                    UserDataHandler.AuthStatus receivedAuthResponse = authResponse.getDataOnceAndReset();
                    if(receivedAuthResponse==null){
                        return authResponse;
                    }
                    switch (receivedAuthResponse){
                        case AUTHENTICATION_SUCCESS:
                            break;
                        case AUTHENTICATION_FAILURE:
                            break;
                        case LOGIN_SUCCESS:
                            goToFeedAction.setValue(new LiveEvent<>(true));
                            break;
                        case LOGIN_FAILURE:
                            email.setValue("");
                            password.setValue("");
                            break;
                        case SIGNUP_SUCCESS:
                            Log.e("SIGNUP", "success");
                            break;
                        case SIGNUP_FAILURE:
                            Log.e("SIGNUP", "failure");
                            email.setValue(null);
                            password.setValue(null);

                            break;
                        case ADD_EXTRA_DETAILS_SUCCESS:
                            Log.e("DETAILS", "success");
                            //add code to tell user to verify mail

                            DataRepository.getInstance()
                                    .getUserDataHandler()
                                    .authenticateUser(email.getValue(), password.getValue());
                            break;
                        case ADD_EXTRA_DETAILS_FAILURE:
                            Log.e("DETAILS", "failure");
                            break;
                    }
                    /*
                    done so that an old event is not triggered after attaching an observer later in another place
                    during the UI workflow
                    */
                    return new LiveEvent<>(receivedAuthResponse);
                }
        );
    }

    public void startSignIn(){
        fragmentChangeAction.setValue(new LiveEvent<>(LoginActivity.FragmentIdentifier.LOGIN_SIGNIN_FRAGMENT));
    }

    public void startSignUp(){
        fragmentChangeAction.setValue(new LiveEvent<>(LoginActivity.FragmentIdentifier.LOGIN_INTERESTS_FRAGMENT));
    }

    public void doUserLogin(){

        AtomicBoolean invalidData = new AtomicBoolean(false);

        dataValidator.validateEmail(email.getValue(), dataValidationInformation -> {
            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                invalidData.set(true);
            }
            notifyValidity(dataValidationInformation);
        });
        dataValidator.validatePassword(password.getValue(), dataValidationInformation -> {
            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                invalidData.set(true);
            }
            notifyValidity(dataValidationInformation);
        });

        if(!invalidData.get()) {

            notifyValidity(new DataValidator.DataValidationInformation(
                    DataValidator.DataValidationPoint.VALIDATION_POINT_ALL,
                    DataValidator.DataValidationResult.VALIDATION_RESULT_VALID
            ));
            DataRepository.getInstance()
                    .getUserDataHandler()
                    .authenticateUser(email.getValue(), password.getValue());
        }

    }

    public void forgotPassword(){
        password.setValue(null);
        dateOfBirth.setValue(null);
        email.setValue(null);
        userGender.setValue(null);
        fragmentChangeAction.setValue(new LiveEvent<>(LoginActivity.FragmentIdentifier.LOGIN_FORGOTPASSWORD_FRAGMENT));
    }

    /*
    Called after user clicks on button to reset password. The email ID of the user is reflected onto
    email live data. It is set to null when forgotPassword() is called
     */
    public void resetPassword(){

        //TODO add code to send password reset link
    }

    public void detailsEntryDone(){

        //TODO add code to verify email and go to home only if email is verified

        UserDataHandler.MutableUserModel userModel = new UserDataHandler.MutableUserModel();
        AtomicBoolean anyDetailsEntryInvalid = new AtomicBoolean(false);

        dataValidator.validateDateOfBirth(dateOfBirth.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                userModel.setUserDateOfBirth(
                        new Timestamp(TransformUtilities.convertDDMMYYYYtoDate(dateOfBirth.getValue(), "/"))
                );
            }
            else anyDetailsEntryInvalid.set(true);
            notifyValidity(dataValidationInformation);
        });

        dataValidator.validateGender(userGender.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                userModel.setUserGender(
                        Character.toString(userGender.getValue().charAt(0))
                );
            }
            else anyDetailsEntryInvalid.set(true);
            notifyValidity(dataValidationInformation);
        });

        dataValidator.validateInstitute(userInstitute.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                userModel.setUserInstitute(
                        userInstitute.getValue()
                );
            }
            else anyDetailsEntryInvalid.set(true);
            notifyValidity(dataValidationInformation);
        });

        dataValidator.validateName(userDisplayName.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                userModel.setUserDisplayName(
                        userDisplayName.getValue()
                );
            }
            else anyDetailsEntryInvalid.set(true);
            notifyValidity(dataValidationInformation);
        });

        dataValidator.validateEmail(email.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() == DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                userModel.setUserEmail(
                        email.getValue()
                );
            }
            else anyDetailsEntryInvalid.set(true);
            notifyValidity(dataValidationInformation);
        });

        dataValidator.validatePassword(password.getValue(), dataValidationInformation -> {
            if(dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID){
                anyDetailsEntryInvalid.set(true);
            }
            notifyValidity(dataValidationInformation);
        });



        if(selectedInterests.size()>0){
            userModel.setUserInterests(selectedInterests);
        }


        if(!anyDetailsEntryInvalid.get()) {
            notifyValidity(new DataValidator.DataValidationInformation(
                    DataValidator.DataValidationPoint.VALIDATION_POINT_ALL,
                    DataValidator.DataValidationResult.VALIDATION_RESULT_VALID
            ));
            DataRepository.getInstance()
                    .getUserDataHandler()
                    .createNewUserWithEmailAndPassword(email.getValue(), password.getValue(), userModel);
        }

    }

    private void notifyValidity(DataValidator.DataValidationInformation dataValidationInformation){
        dataValidationAction.setValue(new LiveEvent<>(dataValidationInformation));
    }

    public void interestsSelectionDone(List<InterestModel> interestListSection){

        if(interestListSection == null){
            return;
        }
        else if(interestListSection.size() == 0){
            notifyValidity(new DataValidator.DataValidationInformation(
                    DataValidator.DataValidationPoint.VALIDATION_POINT_INTERESTS,
                    DataValidator.DataValidationResult.VALIDATION_RESULT_UNDERFLOW
            ));
        }
        else if(interestListSection.size() > MAX_INTERESTS){
            notifyValidity(new DataValidator.DataValidationInformation(
                    DataValidator.DataValidationPoint.VALIDATION_POINT_INTERESTS,
                    DataValidator.DataValidationResult.VALIDATION_RESULT_OVERFLOW
            ));
        }
        else {
            selectedInterests.clear();
            selectedInterests.addAll(interestListSection);
            fragmentChangeAction.setValue(new LiveEvent<>(LoginActivity.FragmentIdentifier.LOGIN_DETAILS_FRAGMENT));
        }
    }

    public void exitDetailsScreen(){
    }

    public void exitInterestsScreen(){
        userDisplayName.setValue(null);
        userInstitute.setValue(null);
        userGender.setValue(null);
        password.setValue(null);
        email.setValue(null);
        dateOfBirth.setValue(null);
    }

    public void exitSignInScreen(){
        email.setValue(null);
        password.setValue(null);
    }

    public void goToProfilePictureChooser(){
        //TODO add code to select avatar photo
    }

    public LiveData<LiveEvent<UserDataHandler.AuthStatus>> getAuthResultAction(){
        return authResultAction;
    }

    public LiveData<LiveEvent<LoginActivity.FragmentIdentifier>> getFragmentChangeAction(){
        return fragmentChangeAction;
    }

    public LiveData<LiveEvent<Boolean>> getGoToFeedAction(){
        return goToFeedAction;
    }

    public LiveData<LiveEvent<String>> getToastMessageAction(){
        return toastMessageAction;
    }

    public LiveData<LiveEvent<DataValidator.DataValidationInformation>> getDataValidationAction() {
        return dataValidationAction;
    }

    public List<InterestModel> getInterestsListSection(){
        if(allInterests.size()>0){
            return allInterests;
        }
        return DataRepository.getInstance().getAppDataHandler().getInterestsList();
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
