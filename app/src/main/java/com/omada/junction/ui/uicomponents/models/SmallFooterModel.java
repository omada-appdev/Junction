package com.omada.junction.ui.uicomponents.models;

public class SmallFooterModel {

    private final String footerText;

    public SmallFooterModel(String headerText){
        this.footerText = headerText;
    }

    public String getHeaderText(){
        return footerText;
    }
}
