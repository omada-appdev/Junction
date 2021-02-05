package com.omada.junction.data.models.mutable;

import com.omada.junction.data.models.external.InstituteModel;

public class MutableInstituteModel extends InstituteModel {

    public void setHandle(String handle){
        this.handle = handle;
    }

    public void setName(String name){
        this.name = name;
    }
}
