package com.omada.junction.data.models.mutable;

import com.omada.junction.data.models.external.VenueModel;

public class MutableVenueModel extends VenueModel {

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setInstitute(String institute) {
        this.institute = institute;
    }
}
