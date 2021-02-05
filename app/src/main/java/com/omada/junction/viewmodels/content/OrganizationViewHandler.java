package com.omada.junction.viewmodels.content;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omada.junction.data.models.external.OrganizationModel;
import com.omada.junction.data.models.external.ShowcaseModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import javax.annotation.Nonnull;

public class OrganizationViewHandler {

    private final MutableLiveData<LiveEvent<OrganizationModel>> organizationModelDetailsTrigger = new MutableLiveData<>();
    private final MutableLiveData<LiveEvent<ShowcaseModel>> organizationShowcaseDetailsTrigger = new MutableLiveData<>();

    public void goToOrganization(@Nonnull OrganizationModel organizationModel){
        organizationModelDetailsTrigger.setValue(new LiveEvent<>(organizationModel));
    }

    public void goToOrganizationShowcase(ShowcaseModel showcaseModel){
        organizationShowcaseDetailsTrigger.setValue(new LiveEvent<>(showcaseModel));
    }

    public LiveData<LiveEvent<OrganizationModel>> getOrganizationModelDetailsTrigger() {
        return organizationModelDetailsTrigger;
    }

    public LiveData<LiveEvent<ShowcaseModel>> getOrganizationShowcaseDetailsTrigger() {
        return organizationShowcaseDetailsTrigger;
    }
}
