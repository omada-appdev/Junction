package com.omada.junction.utils.taskhandler;

import com.omada.junction.utils.TransformUtilities;

import java.util.Date;

public class DataValidator {

    private static final String EMAIL_VERIFICATION_REGEX =
            "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";

    private static final String NAME_VERIFICATION_REGEX =
            "[a-zA-Z ]*";



    public void validateName(String name, OnValidationCompleteListener listener){
        listener.onValidationComplete(validateName(name));
    }
    
    public DataValidationInformation validateName(String name){

        if(name == null){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_NAME,
                    DataValidationResult.VALIDATION_RESULT_BLANK_VALUE
            );
        }

        name = name.trim();

        if(name.equals("")) {
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_NAME,
                    DataValidationResult.VALIDATION_RESULT_BLANK_VALUE
            );
        }
        else if(!name.matches(NAME_VERIFICATION_REGEX)){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_NAME,
                    DataValidationResult.VALIDATION_RESULT_ILLEGAL_FORMAT
            );
        }
        else{
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_NAME,
                    DataValidationResult.VALIDATION_RESULT_VALID
            );
        }

    }

    public void validateEmail(String email, OnValidationCompleteListener listener){
        listener.onValidationComplete(validateEmail(email));
    }

    public DataValidationInformation validateEmail(String email){

        if(email == null){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_EMAIL,
                    DataValidationResult.VALIDATION_RESULT_BLANK_VALUE
            );
        }

        email = email.trim();

        if(email.equals("")){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_EMAIL,
                    DataValidationResult.VALIDATION_RESULT_BLANK_VALUE
            );
        }
        else if(!email.matches(EMAIL_VERIFICATION_REGEX)){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_EMAIL,
                    DataValidationResult.VALIDATION_RESULT_ILLEGAL_FORMAT
            );
        }
        else{
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_EMAIL,
                    DataValidationResult.VALIDATION_RESULT_VALID
            );
        }
    }

    public void validateGender(String gender, OnValidationCompleteListener listener){
        listener.onValidationComplete(validateGender(gender));
    }

    public DataValidationInformation validateGender(String gender){

        if(gender == null){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_GENDER,
                    DataValidationResult.VALIDATION_RESULT_BLANK_VALUE
            );
        }

        gender = gender.trim().toUpperCase();

        if(!gender.equals("MALE") && !gender.equals("FEMALE") && !gender.equals("OTHER")){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_GENDER,
                    DataValidationResult.VALIDATION_RESULT_ILLEGAL_FORMAT
            );
        }
        else{
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_GENDER,
                    DataValidationResult.VALIDATION_RESULT_VALID
            );
        }
    }

    public void validateInstitute(String institute, OnValidationCompleteListener listener){
        listener.onValidationComplete(validateInstitute(institute));
    }

    public DataValidationInformation validateInstitute(String institute){
        return new DataValidationInformation(
                DataValidationPoint.VALIDATION_POINT_INSTITUTE,
                DataValidationResult.VALIDATION_RESULT_VALID
        );
    }

    public void validateDateOfBirth(String dateOfBirth, OnValidationCompleteListener listener){
        listener.onValidationComplete(validateDateOfBirth(dateOfBirth));
    }

    public DataValidationInformation validateDateOfBirth(String dateOfBirth){

        if(dateOfBirth == null){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_DATE_OF_BIRTH,
                    DataValidationResult.VALIDATION_RESULT_BLANK_VALUE
            );
        }

        dateOfBirth = dateOfBirth.trim();

        if(dateOfBirth.equals("")){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_DATE_OF_BIRTH,
                    DataValidationResult.VALIDATION_RESULT_BLANK_VALUE
            );
        }
        else{
            Date parsedDate = TransformUtilities.convertDDMMYYYYtoDate(dateOfBirth, "/");
            if(parsedDate != null) {
                return new DataValidationInformation(
                        DataValidationPoint.VALIDATION_POINT_DATE_OF_BIRTH,
                        DataValidationResult.VALIDATION_RESULT_VALID
                );
            }
            else{
                return new DataValidationInformation(
                        DataValidationPoint.VALIDATION_POINT_DATE_OF_BIRTH,
                        DataValidationResult.VALIDATION_RESULT_PARSE_ERROR
                );
            }
        }
    }

    public void validatePassword(String password, OnValidationCompleteListener listener){
        listener.onValidationComplete(validatePassword(password));
    }

    public DataValidationInformation validatePassword(String password){
        if(password == null || password.equals("")){
            return new DataValidationInformation(
                    DataValidationPoint.VALIDATION_POINT_PASSWORD,
                    DataValidationResult.VALIDATION_RESULT_BLANK_VALUE
            );
        }
        return new DataValidationInformation(
                DataValidationPoint.VALIDATION_POINT_PASSWORD,
                DataValidationResult.VALIDATION_RESULT_VALID
        );
    }

    public enum DataValidationPoint {

        VALIDATION_POINT_ALL,

        VALIDATION_POINT_NAME,
        VALIDATION_POINT_EMAIL,
        VALIDATION_POINT_GENDER,
        VALIDATION_POINT_DATE_OF_BIRTH,
        VALIDATION_POINT_INSTITUTE,
        VALIDATION_POINT_PASSWORD,
        VALIDATION_POINT_INTERESTS,

    }

    public enum DataValidationResult {

        VALIDATION_RESULT_VALID,
        VALIDATION_RESULT_INVALID,

        VALIDATION_RESULT_BLANK_VALUE,
        VALIDATION_RESULT_NO_INFO,

        VALIDATION_RESULT_UNEXPECTED_VALUE,
        VALIDATION_RESULT_ILLEGAL_FORMAT,
        VALIDATION_RESULT_PARSE_ERROR,
        VALIDATION_RESULT_DOES_NOT_EXIST,
        VALIDATION_RESULT_OVERFLOW,
        VALIDATION_RESULT_UNDERFLOW,

    }

    public static final class DataValidationInformation {

        private final DataValidationPoint dataValidationPoint;
        private final DataValidationResult dataValidationResult;

        public DataValidationInformation(DataValidationPoint dataValidationPoint, DataValidationResult dataValidationResult) {
            this.dataValidationPoint = dataValidationPoint;
            this.dataValidationResult = dataValidationResult;
        }

        public DataValidationPoint getValidationPoint(){
            return dataValidationPoint;
        }

        public DataValidationResult getDataValidationResult() {
            return dataValidationResult;
        }
    }

    public interface OnValidationCompleteListener {
        void onValidationComplete(DataValidationInformation dataValidationInformation);
    }

}
