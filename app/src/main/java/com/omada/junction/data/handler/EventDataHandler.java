package com.omada.junction.data.handler;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.omada.junction.data.BaseDataHandler;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.converter.EventModelConverter;
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.data.models.internal.remote.EventModelRemoteDB;
import com.omada.junction.utils.taskhandler.LiveDataAggregator;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class EventDataHandler extends BaseDataHandler {

    private enum EventType{
        EVENT_TYPE_LOCAL,
        EVENT_TYPE_REMOTE;
    }

     /*
    #############################
    # INPUT FIELDS FROM SOURCES # (if required)
    #############################
     */
    /*
    ##############################
    # OUTPUT FIELDS TO VIEWMODEL #
    ##############################
     */

    private MediatorLiveData<List<EventModel>> loadedForYouEventsNotifier = new MediatorLiveData<>();
    private final MediatorLiveData<List<EventModel>> loadedLearnEventsNotifier = new MediatorLiveData<>();
    private final MediatorLiveData<List<EventModel>> loadedCompeteEventsNotifier = new MediatorLiveData<>();

    /*
    ###########################
    # FIELDS FOR INTERNAL USE #
    ###########################
    */
    private EventsAggregator ForYouEventsAggregator = new EventsAggregator(loadedForYouEventsNotifier);
    private EventModelConverter eventModelConverter = new EventModelConverter();

    public EventDataHandler(){
    }

    public LiveData<LiveEvent<EventModel>> getEventDetails(String eventID){
        MutableLiveData<LiveEvent<EventModel>> detailsLiveEvent = new MutableLiveData<>();

        getEventDetailsFromRemote(eventID, detailsLiveEvent);
        return detailsLiveEvent;
    }

    private void getEventDetailsFromRemote(String eventID, MutableLiveData<LiveEvent<EventModel>> destinationLiveData) {

        FirebaseFirestore.getInstance()
                .collection("events")
                .document(eventID)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if(documentSnapshot == null) return;
                    EventModelRemoteDB modelRemote = documentSnapshot.toObject(EventModelRemoteDB.class);
                    if(modelRemote == null) return;
                    modelRemote.setId(documentSnapshot.getId());
                    destinationLiveData.setValue(new LiveEvent<>(
                            eventModelConverter.convertRemoteDBToExternalModel(modelRemote)
                    ));
                })
                .addOnFailureListener(e -> {
                    Log.e("EventModel", "error retrieving event");
                    e.printStackTrace();
                });

    }

    /*
    This function gets all events
     */

    public void getForYouEvents(){

        MutableLiveData<List<EventModel>> localEvents = new MutableLiveData<>();
        MutableLiveData<List<EventModel>> remoteEvents = new MutableLiveData<>();

        loadedForYouEventsNotifier.addSource(localEvents, eventModels -> {
            ForYouEventsAggregator.holdData(EventType.EVENT_TYPE_LOCAL, eventModels);
        });
        loadedForYouEventsNotifier.addSource(remoteEvents, eventModels -> {
            ForYouEventsAggregator.holdData(EventType.EVENT_TYPE_REMOTE, eventModels);
        });

        getForYouEventsFromRemote(remoteEvents);
        //getAllEventsFromLocal(localEvents)
    }
    private void getForYouEventsFromRemote(final MutableLiveData<List<EventModel>> destinationLiveData){

        //TODO add query to check what the latest timestamp in local events is and get all after that

        List<String> following = new ArrayList<>(
                DataRepository.getInstance()
                .getUserDataHandler()
                .getCurrentUserModel()
                .getFollowing()
                .keySet()
        );

        following.add("null");

        FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
        Query query = dbInstance
                .collection("posts")
                .whereEqualTo("type", "event")
                .whereIn("creator", following)
                .orderBy("timeCreated", Query.Direction.DESCENDING)
                .limit(5);

        if(PaginationHelper.lastForYouEvent != null){
            query = query.startAfter(PaginationHelper.lastForYouEvent);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    ArrayList<EventModel> loadedEvents = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        EventModelRemoteDB item = snapshot.toObject(EventModelRemoteDB.class);
                        if(item == null) {
                            return;
                        }
                        item.setId(snapshot.getId());
                        loadedEvents.add(eventModelConverter.convertRemoteDBToExternalModel(item));
                    }
                    if(queryDocumentSnapshots.size() > 0) {
                        PaginationHelper.lastForYouEvent = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }

                    destinationLiveData.setValue(loadedEvents);
                })
                .addOnFailureListener(e -> Log.d("TAG", Objects.requireNonNull(e.getMessage())));
    }

    private void getAllEventsFromLocal(final MutableLiveData<ArrayList<EventModel>> destinationLiveData){
        //TODO get all events from local db and remove all the events that are expired and set live data as above
    }

    public void populateEventResponses(EventModel eventModel, Map <String, Map <String, String>> responses) {

        String UID = null;
        try {
             UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.e("EventRegistration" ,"No current user");
            return;
        }

        FirebaseFirestore dbInstance = FirebaseFirestore.getInstance();
        dbInstance
                .collection("events")
                .document(eventModel.getId())
                .collection("registrations")
                .document(UID)
                .set(responses)
                .addOnSuccessListener(aVoid -> {
                    Log.e("EventRegistration" ,"Event registration successful");
                });

    }

    public LiveData<List<EventModel>> getLoadedForYouEventsNotifier(){
        return loadedForYouEventsNotifier;
    }

    // This class will be used to get cursors for pagination
    private static class PaginationHelper{

        public static DocumentSnapshot lastForYouEvent = null;
        public static DocumentSnapshot lastLearnEvent = null;
        public static DocumentSnapshot lastCompeteEvent = null;
        public static DocumentSnapshot lastInstituteEvent = null;
    }
    public void resetLastForYouEvent(){
        PaginationHelper.lastForYouEvent = null;
        loadedForYouEventsNotifier = new MediatorLiveData<>();
        ForYouEventsAggregator = new EventsAggregator(loadedForYouEventsNotifier);
    }

    private static class EventsAggregator extends LiveDataAggregator<EventType, List<EventModel>, List<EventModel>>{

        public EventsAggregator(MediatorLiveData<List<EventModel>> destination) {
            super(destination);
        }

        @Override
        public List<EventModel> mergeWithExistingData(EventType typeOfData, List<EventModel> oldData, List<EventModel> newData) {
            return newData;
        }

        @Override
        protected boolean checkDataForAggregability() {
            try {
                List<? extends PostModel> remoteEvents = dataOnHold.get(EventType.EVENT_TYPE_REMOTE);
                //List<? extends BaseModel> localEvents = dataOnHold.get("remoteEvents");

                /*
                if(remoteEvents == null || localEvents == null){
                    throw new Exception("Null values given to aggregator");
                }
                else if(localEvents.size()>0 || remoteEvents.size()>0){
                    throw new Exception("One of the Arrays is empty");
                }
                else{
                    return true;
                }
                 */

                Log.e("Pagination", "aggregable events " + (remoteEvents != null));

                return remoteEvents != null;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        protected void aggregateData() {

            Log.e("Pagination", "aggregated events");
            //TODO result.addAll(dataOnHold.get("localEvents"));
            List<EventModel> result = new ArrayList<>(dataOnHold.get(EventType.EVENT_TYPE_REMOTE));

            dataOnHold.put(EventType.EVENT_TYPE_REMOTE, null);
            destinationLiveData.setValue(result);
        }

    }
}

