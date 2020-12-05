package com.omada.junction.data.framework;

public class LocalStorage {

    private static LocalStorage localStorage;

    private LocalStorage(){
        //constructor
    }

    public static LocalStorage getInstance() {
        if(localStorage == null){
            localStorage = new LocalStorage();
        }
        return localStorage;
    }

}
