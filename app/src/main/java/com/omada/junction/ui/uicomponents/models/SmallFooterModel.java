package com.omada.junction.ui.uicomponents.models;

public class SmallFooterModel {

    private String footerText;

    public SmallFooterModel(String headerText){
        this.footerText = headerText;
    }

    public String getFooterText(){
        return footerText;
    }

    public void setFooterText(String text){
        footerText = text;
    }
}
