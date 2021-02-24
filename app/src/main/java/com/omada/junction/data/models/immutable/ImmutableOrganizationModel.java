package com.omada.junction.data.models.immutable;

import com.omada.junction.data.models.external.OrganizationModel;


public final class ImmutableOrganizationModel extends OrganizationModel {

    private ImmutableOrganizationModel(OrganizationModel organizationModel){
        super(organizationModel);
    }

    public static ImmutableOrganizationModel from(OrganizationModel organizationModel) {
        return new ImmutableOrganizationModel(organizationModel);
    }

}
