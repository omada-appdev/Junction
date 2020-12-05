package com.omada.junction.viewmodels.content;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.Map;

public class EventViewHandler {

    private final MutableLiveData<LiveEvent<EventModel>> eventCardDetailsTrigger = new MutableLiveData<>();
    private final MutableLiveData<LiveEvent<EventModel>> eventFormTrigger = new MutableLiveData<>();
    private final MutableLiveData<LiveEvent<EventModel>> upcomingEventDetailsTrigger = new MutableLiveData<>();
    private final MutableLiveData<LiveEvent<EventModel>> attendedEventDetailsTrigger = new MutableLiveData<>();

    private final MutableLiveData<LiveEvent<String>> callOrganizerTrigger = new MutableLiveData<>();
    private final MutableLiveData<LiveEvent<String>> mailOrganizerTrigger = new MutableLiveData<>();


    public LiveData<LiveEvent<EventModel>> getEventCardDetailsTrigger() {
        return eventCardDetailsTrigger;
    }

    public LiveData<LiveEvent<EventModel>> getEventFormTrigger() {
        return eventFormTrigger;
    }

    public LiveData<LiveEvent<String>> getCallOrganizerTrigger() {
        return callOrganizerTrigger;
    }

    public LiveData<LiveEvent<String>> getMailOrganizerTrigger() {
        return mailOrganizerTrigger;
    }



    public void goToEventCardDetails(EventModel eventModel){
        eventCardDetailsTrigger.setValue(new LiveEvent<>(eventModel));
    }

    public void goToUpcomingEventDetails(EventModel eventModel){
        upcomingEventDetailsTrigger.setValue(new LiveEvent<>(eventModel));
    }

    public void goToAttendedEventDetails(EventModel eventModel){
        attendedEventDetailsTrigger.setValue(new LiveEvent<>(eventModel));
    }

    public void goToEventForm(EventModel eventModel){
        eventFormTrigger.setValue(new LiveEvent<>(eventModel));
    }

    public void callOrganizer(String organizerNumber){
        callOrganizerTrigger.setValue(new LiveEvent<>(organizerNumber));
    }

    public void mailOrganizer(String organizerEmail){
        mailOrganizerTrigger.setValue(new LiveEvent<>(organizerEmail));
    }

    public void registerForEvent(EventModel eventModel, Map <String, Map <String, Map <String, String>>> responses){
        DataRepository.getInstance().getEventDataHandler().populateEventResponses(eventModel, responses.get("questions"));
    }
}
