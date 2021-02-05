package com.omada.junction.data.models.internal.remote;

import com.google.firebase.firestore.PropertyName;
import com.omada.junction.data.models.internal.BaseModelInternal;

public class VenueModelRemoteDB extends BaseModelInternal {

    private String name;
    private String address;
    private String institute;

    public VenueModelRemoteDB(String id) {
        super(id);
    }

    public VenueModelRemoteDB(){
    }

    @PropertyName("name")
    public String getName() {
        return name;
    }

    @PropertyName("address")
    public String getAddress() {
        return address;
    }

    @PropertyName("institute")
    public String getInstitute() {
        return institute;
    }

    @PropertyName("name")
    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("address")
    public void setAddress(String address) {
        this.address = address;
    }

    @PropertyName("institute")
    public void setInstitute(String institute) {
        this.institute = institute;
    }
}
