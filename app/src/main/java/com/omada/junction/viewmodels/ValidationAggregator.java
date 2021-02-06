package com.omada.junction.viewmodels;

import androidx.lifecycle.MediatorLiveData;

import com.omada.junction.utils.taskhandler.DataValidator;
import com.omada.junction.utils.taskhandler.LiveDataAggregator;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;


public final class ValidationAggregator extends LiveDataAggregator<DataValidator.DataValidationPoint, DataValidator.DataValidationInformation, DataValidator.DataValidationInformation> {

    private final Set<DataValidator.DataValidationPoint> allValidationPoints = new HashSet<>();

    private ValidationAggregator(MediatorLiveData<DataValidator.DataValidationInformation> destination) {
        super(destination);
    }

    public static Builder build(@Nonnull MediatorLiveData<DataValidator.DataValidationInformation> destination) {
        return new Builder(destination);
    }

    @Override
    protected DataValidator.DataValidationInformation mergeWithExistingData(DataValidator.DataValidationPoint typeofData, DataValidator.DataValidationInformation oldData, DataValidator.DataValidationInformation newData) {
        throw new UnsupportedOperationException("Attempt to set same type of validation parameter twice");
    }

    @Override
    protected boolean checkDataForAggregability() {
        return dataOnHold.keySet().containsAll(allValidationPoints);
    }

    @Override
    protected void aggregateData() {

        for (DataValidator.DataValidationInformation dataValidationInformation : dataOnHold.values()) {
            if (dataValidationInformation.getDataValidationResult() != DataValidator.DataValidationResult.VALIDATION_RESULT_VALID) {
                destinationLiveData.postValue(new DataValidator.DataValidationInformation(
                        DataValidator.DataValidationPoint.VALIDATION_POINT_ALL,
                        DataValidator.DataValidationResult.VALIDATION_RESULT_INVALID
                ));
                return;
            }
        }
        destinationLiveData.postValue(new DataValidator.DataValidationInformation(
                DataValidator.DataValidationPoint.VALIDATION_POINT_ALL,
                DataValidator.DataValidationResult.VALIDATION_RESULT_VALID
        ));
    }

    public static final class Builder {

        private final ValidationAggregator validationAggregator;

        private Builder(@Nonnull MediatorLiveData<DataValidator.DataValidationInformation> destination){
            validationAggregator = new ValidationAggregator(destination);
        }

        public Builder add(@Nonnull DataValidator.DataValidationPoint dataValidationPoint) {
            validationAggregator.allValidationPoints.add(dataValidationPoint);
            return this;
        }

        public ValidationAggregator get() {
            return validationAggregator;
        }
    }
}