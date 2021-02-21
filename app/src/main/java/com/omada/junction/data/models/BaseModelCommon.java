package com.omada.junction.data.models;

public class BaseModelCommon {

    protected String id;

    public BaseModelCommon(){
    }

    public BaseModelCommon(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
