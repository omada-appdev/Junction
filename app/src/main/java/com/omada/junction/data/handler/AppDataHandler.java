package com.omada.junction.data.handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omada.junction.R;
import com.omada.junction.data.models.InterestModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppDataHandler {

    public List<InterestModel> getInterestsList(){

        InterestModel[] arrayList = new InterestModel[]{
                new InterestModel("Technology", R.drawable.technology),
                new InterestModel("Music", R.drawable.music),
                new InterestModel("Movies", R.drawable.movies),
                new InterestModel("Entrepreneurship", R.drawable.entrepreneurship),
                new InterestModel("Web development", R.drawable.web_development),
                new InterestModel("Machine Learning", R.drawable.machine_learning),
                new InterestModel("Space", R.drawable.space),
                new InterestModel("Debating", R.drawable.debate),
                new InterestModel("Gaming", R.drawable.gaming),
                new InterestModel("Quizzing", R.drawable.quiz),
                new InterestModel("Business", R.drawable.business),
                new InterestModel("Writing", R.drawable.writing),
                new InterestModel("Dance", R.drawable.dance),
                new InterestModel("Electronics", R.drawable.electronics),
                new InterestModel("Dramatics", R.drawable.acting),
                new InterestModel("Science", R.drawable.science),
                new InterestModel("Painting", R.drawable.painting),
                new InterestModel("Nature", R.drawable.nature),
                new InterestModel("Social work", R.drawable.social_work)
        };

        return Arrays.asList(arrayList);
    }

    public static class CountedAccessLiveEvent<T> extends LiveEvent<T>{

        int numAccesses = 0;
        int maxAccesses = 1;

        private CountedAccessLiveEvent(T data){
        }

        public CountedAccessLiveEvent(T data, int numAccesses){
            super(data);
            this.numAccesses = numAccesses;
        }

        @Override
        public T getData() {

            if(numAccesses == maxAccesses){
                return null;
            }

            numAccesses ++;
            return super.getData();
        }
    }

}
