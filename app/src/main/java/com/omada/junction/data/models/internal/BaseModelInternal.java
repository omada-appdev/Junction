package com.omada.junction.data.models.internal;

import com.google.firebase.firestore.Exclude;
import com.omada.junction.data.models.BaseModelCommon;

public class BaseModelInternal extends BaseModelCommon {

    public BaseModelInternal(){
    }

    public BaseModelInternal(String id) {
        super(id);
    }

    @Exclude
    @Override
    public String getId(){
        return super.getId();
    }

    @Exclude
    @Override
    public void setId(String id){
        super.setId(id);
    }
}
