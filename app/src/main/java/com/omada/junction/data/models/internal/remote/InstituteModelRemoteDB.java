package com.omada.junction.data.models.internal.remote;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.omada.junction.data.models.internal.BaseModelInternal;

public class InstituteModelRemoteDB extends BaseModelInternal {

    private String handle;
    private String name;

    public InstituteModelRemoteDB(String id) {
        super(id);
    }

    public InstituteModelRemoteDB(){
    }

    @PropertyName("handle")
    public String getHandle(){
        return handle;
    }

    @PropertyName("name")
    public String getName(){
        return name;
    }



    @PropertyName("handle")
    public void setHandle(String handle){
        this.handle = handle;
    }

    @PropertyName("name")
    public void setName(String name){
        this.name = name;
    }
}
