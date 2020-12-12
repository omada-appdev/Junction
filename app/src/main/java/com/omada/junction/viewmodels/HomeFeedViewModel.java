package com.omada.junction.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.ArticleModel;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.utils.taskhandler.LiveDataAggregator;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedViewModel extends ViewModel {

    /*
    ################################
    # INPUT FIELDS FROM REPOSITORY #
    ################################
     */
    private LiveData<List<EventModel>> loadedForYouEvents;
    private LiveData<List<EventModel>> loadedLearnEvents;
    private LiveData<List<EventModel>> loadedCompeteEvents;

    private LiveData<List<ArticleModel>> loadedForYouArticles;
    private LiveData<List<EventModel>> loadedLearnArticles;

    /*
    #########################
    # OUTPUT FIELDS TO VIEW #
    #########################
     */
    private MediatorLiveData<List<BaseModel>> loadedForYou = new MediatorLiveData<>();
    private MediatorLiveData<List<BaseModel>> loadedLearn = new MediatorLiveData<>();
    private MediatorLiveData<List<BaseModel>> loadedCompete = new MediatorLiveData<>();

    private final MutableLiveData<LiveEvent<Boolean>> forYouCompleteNotifier = new MutableLiveData<>();

    /*
    ###########################
    # FIELDS FOR INTERNAL USE #
    ###########################
     */
    private ForYouAggregator forYouAggregator = new ForYouAggregator(loadedForYou, forYouCompleteNotifier);


    public HomeFeedViewModel(){
        initializeDataLoaders();
        distributeLoadedData();
    }

    /*
    * Initializes all the points of entry for content that goes from databases to the feed through
    * transformations. These transformations are then distributed via the next method to points of
    * output to the ui components
    */
    private void initializeDataLoaders(){

        loadedForYouEvents = Transformations.map(
                DataRepository.getInstance().getEventDataHandler().getLoadedForYouEventsNotifier(), allEvents-> allEvents);
        loadedForYouArticles = Transformations.map(
                DataRepository.getInstance().getArticleDataHandler().getLoadedAllArticlesNotifier(), allArticles -> allArticles);
    }

    /*
    * initialize live data here through transformations to create wirings or associations
    * to propagate repository data downstream
    */
    private void distributeLoadedData(){

        loadedForYou.addSource(loadedForYouArticles, articleModels->{
            forYouAggregator.holdData(ContentTypeIdentifier.CONTENT_TYPE_ARTICLE, articleModels);
        });
        loadedForYou.addSource(loadedForYouEvents, eventModels->{
            forYouAggregator.holdData(ContentTypeIdentifier.CONTENT_TYPE_EVENT, eventModels);
        });
    }

    public LiveData<List<BaseModel>> getLoadedForYou(){
        return loadedForYou;
    }

    public LiveData<List<BaseModel>> getLoadedLearn(){
        return loadedLearn;
    }

    public LiveData<List<BaseModel>> getLoadedCompete(){
        return loadedCompete;
    }

    public void getForYouFeedContent(){

        Log.e("For you", "called feed content");

        DataRepository
                .getInstance()
                .getEventDataHandler()
                .getForYouEvents();

        DataRepository
                .getInstance()
                .getArticleDataHandler()
                .getAllArticles();
    }

    public void resetForYouFeedContent(){

        loadedForYou = new MediatorLiveData<>();
        forYouAggregator = new ForYouAggregator(loadedForYou, forYouCompleteNotifier);
    }

    public void resetLearnFeedContent(){

        loadedLearn = new MediatorLiveData<>();
        //forYouAggregator = new ForYouAggregator(loadedForYou);

    }

    public void resetCompeteFeedContent(){

        loadedCompete = new MediatorLiveData<>();
        //forYouAggregator = new ForYouAggregator(loadedForYou);

    }

    @Override
    protected void onCleared() {
        super.onCleared();

        DataRepository.getInstance()
                .resetHomeFeedContent();

        initializeDataLoaders();

        resetForYouFeedContent();
        resetLearnFeedContent();
        resetCompeteFeedContent();

        distributeLoadedData();
    }

    public MutableLiveData<LiveEvent<Boolean>> getForYouCompleteNotifier() {
        return forYouCompleteNotifier;
    }

    private enum ContentTypeIdentifier {
        CONTENT_TYPE_EVENT,
        CONTENT_TYPE_ARTICLE,
        CONTENT_TYPE_ANNOUNCEMENT,
        CONTENT_TYPE_ADVERTISEMENT
    }

    /*
    ############################
    # STATIC CLASSES AND ENUMS #
    ############################
     */

    // Apply things like sorting, etc. in aggregator
    private static class ForYouAggregator extends LiveDataAggregator<ContentTypeIdentifier, List<? extends BaseModel>, List<BaseModel>>{

        private final MutableLiveData<LiveEvent<Boolean>> completeNotifier;

        public ForYouAggregator(MediatorLiveData<List<BaseModel>> destination, MutableLiveData<LiveEvent<Boolean>> completeNotifier) {
            super(destination);
            this.completeNotifier = completeNotifier;
        }

        @Override
        public void holdData(ContentTypeIdentifier typeOfData, List<? extends BaseModel> data) {

            if(data == null){
                return;
            }
            super.holdData(typeOfData, data);
        }

        @Override
        protected List<BaseModel> mergeWithExistingData(ContentTypeIdentifier typeOfData, List<? extends BaseModel> oldData, List<? extends BaseModel> newData) {
            return (List<BaseModel>) newData;
        }

        @Override
        protected boolean checkDataForAggregability() {
            return dataOnHold.get(ContentTypeIdentifier.CONTENT_TYPE_ARTICLE) != null &&
                    dataOnHold.get(ContentTypeIdentifier.CONTENT_TYPE_EVENT) != null;
        }

        @SuppressWarnings({"ConstantConditions"})
        @Override
        protected void aggregateData() {

            Log.e("Pagination", "aggregated for you");

            List<BaseModel> aggregatedList = new ArrayList<>();
            aggregatedList.addAll(dataOnHold.get(ContentTypeIdentifier.CONTENT_TYPE_EVENT));
            aggregatedList.addAll(dataOnHold.get(ContentTypeIdentifier.CONTENT_TYPE_ARTICLE));

            if(aggregatedList.size() == 0){
                completeNotifier.setValue(new LiveEvent<>(true));
            }

            dataOnHold.put(ContentTypeIdentifier.CONTENT_TYPE_EVENT, null);
            dataOnHold.put(ContentTypeIdentifier.CONTENT_TYPE_ARTICLE, null);

            //to retain existing data in destination live data
            if(destinationLiveData.getValue() != null){
                aggregatedList.addAll(0, destinationLiveData.getValue());
            }
            destinationLiveData.setValue(aggregatedList);

        }

    }

    private static class LearnAggregator extends LiveDataAggregator<ContentTypeIdentifier, List<? extends BaseModel>, List<BaseModel>> {

        public LearnAggregator(MediatorLiveData<List<BaseModel>> destination) {
            super(destination);
        }

        @Override
        protected List<? extends BaseModel> mergeWithExistingData(ContentTypeIdentifier typeofData, List<? extends BaseModel> oldData, List<? extends BaseModel> newData) {
            return null;
        }

        @Override
        protected boolean checkDataForAggregability() {
            return false;
        }

        @Override
        protected void aggregateData() {

        }
    }

    private static class CompeteAggregator extends LiveDataAggregator<ContentTypeIdentifier, List<? extends BaseModel>, List<BaseModel>> {

        public CompeteAggregator(MediatorLiveData<List<BaseModel>> destination) {
            super(destination);
        }

        @Override
        protected List<? extends BaseModel> mergeWithExistingData(ContentTypeIdentifier typeofData, List<? extends BaseModel> oldData, List<? extends BaseModel> newData) {
            return null;
        }

        @Override
        protected boolean checkDataForAggregability() {
            return false;
        }

        @Override
        protected void aggregateData() {

        }
    }

}
