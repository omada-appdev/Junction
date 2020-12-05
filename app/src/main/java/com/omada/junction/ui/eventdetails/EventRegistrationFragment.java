package com.omada.junction.ui.eventdetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.omada.junction.data.models.EventModel;
import com.omada.junction.ui.uicomponents.FormView;
import com.omada.junction.viewmodels.FeedContentViewModel;
import com.omada.junction.viewmodels.content.EventViewHandler;

import java.util.Map;

public class EventRegistrationFragment extends Fragment {

    private EventViewHandler eventViewHandler;

    //use form from here and inflate all the required views
    private EventModel eventModel;

    public static EventRegistrationFragment newInstance(EventModel eventModel) {

        Bundle args = new Bundle();
        args.putSerializable("eventModel", eventModel);

        EventRegistrationFragment fragment = new EventRegistrationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to call register for event method and save responses
        eventViewHandler = new ViewModelProvider(requireActivity())
                .get(FeedContentViewModel.class)
                .getEventViewHandler();

        if(savedInstanceState == null) {
            if(getArguments() == null) return;
            eventModel = (EventModel) getArguments().getSerializable("eventModel");
        }
        else{
            eventModel = (EventModel) savedInstanceState.getSerializable("eventModel");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FormView formView = new FormView(getContext());
        formView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        formView.setViewModel(eventViewHandler);
        formView.setForm(eventModel);

        return formView;
    }

    /*
     *   Event Form is of the following structure
     *
     *   Section:
     *   ID --
     *       |--- typeOfElement (section)
     *       |--- position
     *       |--- title
     *       |--- description
     *       |
     *       |--- action type (nextSection, submit, goToSection)
     *       |--- action value
     *
     *   Question:
     *   ID --
     *       |--- typeOfElement (question)
     *       |--- sectionID
     *       |--- position
     *       |--- description
     *       |--- title
     *       |
     *       |--- responseType (date, time, checkBox, radioButton, dropDown, longAnswer, shortAnswer, file)
     *       |--- required
     *       |
     *       |--- validationType
     *       |--- validationCondition
     *       |--- validationCompareOperands
     *       |--- validationErrorText
     *       |
     *       |--- isDuration     - for time
     *       |
     *       |--- includeTime    |- for date
     *       |--- includeYear    |
     *       |
     *       |--- allowedFileTypes   |
     *       |--- maxFileSize        |- for file
     *       |--- maxNumFiles        |
     *
     *   Option:
     *   ID --
     *       |-- typeOfElement (option)
     *       |-- questionId    [type of option is implicit in the question given by ID]
     *       |-- position
     *       |-- title
     *       |
     *       |-- goToSectionBasedOnInput (sectionID)     - only for dropdown and radio button
     *
     *
     */



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.e("Parse", "reached here");
        outState.putSerializable("eventModel", eventModel);
    }

}
