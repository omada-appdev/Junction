package com.omada.junction.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.external.ArticleModel;
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.utils.taskhandler.LiveDataAggregator;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedViewModel extends BaseViewModel {

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
    private MediatorLiveData<List<PostModel>> loadedForYou = new MediatorLiveData<>();
    private MediatorLiveData<List<PostModel>> loadedLearn = new MediatorLiveData<>();
    private MediatorLiveData<List<PostModel>> loadedCompete = new MediatorLiveData<>();

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

    public LiveData<List<PostModel>> getLoadedForYou(){
        return loadedForYou;
    }

    public LiveData<List<PostModel>> getLoadedLearn(){
        return loadedLearn;
    }

    public LiveData<List<PostModel>> getLoadedCompete(){
        return loadedCompete;
    }

    public void getForYouFeedContent(){

        DataRepository
                .getInstance()
                .getEventDataHandler()
                .getForYouEvents();

        DataRepository
                .getInstance()
                .getArticleDataHandler()
                .getForYouArticles();
    }

    private void resetFeed(){
        resetForYouFeedContent();
        resetLearnFeedContent();
        resetCompeteFeedContent();
    }

    private void resetForYouFeedContent(){

        loadedForYou = new MediatorLiveData<>();
        forYouAggregator = new ForYouAggregator(loadedForYou, forYouCompleteNotifier);
    }

    private void resetLearnFeedContent(){

        loadedLearn = new MediatorLiveData<>();
        //forYouAggregator = new ForYouAggregator(loadedForYou);

    }

    private void resetCompeteFeedContent(){

        loadedCompete = new MediatorLiveData<>();
        //forYouAggregator = new ForYouAggregator(loadedForYou);

    }

    public void reinitializeFeed(){

        DataRepository.getInstance()
                .resetHomeFeedContent();

        initializeDataLoaders();
        resetFeed();
        distributeLoadedData();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        reinitializeFeed();
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
    private static class ForYouAggregator extends LiveDataAggregator<ContentTypeIdentifier, List<? extends PostModel>, List<PostModel>>{

        private final MutableLiveData<LiveEvent<Boolean>> completeNotifier;

        public ForYouAggregator(MediatorLiveData<List<PostModel>> destination, MutableLiveData<LiveEvent<Boolean>> completeNotifier) {
            super(destination);
            this.completeNotifier = completeNotifier;
        }

        @Override
        public void holdData(ContentTypeIdentifier typeOfData, List<? extends PostModel> data) {

            if(data == null){
                return;
            }
            super.holdData(typeOfData, data);
        }

        @Override
        protected List<PostModel> mergeWithExistingData(ContentTypeIdentifier typeOfData, List<? extends PostModel> oldData, List<? extends PostModel> newData) {
            return (List<PostModel>) newData;
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

            List<PostModel> aggregatedList = new ArrayList<>();
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

    private static class LearnAggregator extends LiveDataAggregator<ContentTypeIdentifier, List<? extends PostModel>, List<PostModel>> {

        public LearnAggregator(MediatorLiveData<List<PostModel>> destination) {
            super(destination);
        }

        @Override
        protected List<? extends PostModel> mergeWithExistingData(ContentTypeIdentifier typeofData, List<? extends PostModel> oldData, List<? extends PostModel> newData) {
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

    private static class CompeteAggregator extends LiveDataAggregator<ContentTypeIdentifier, List<? extends PostModel>, List<PostModel>> {

        public CompeteAggregator(MediatorLiveData<List<PostModel>> destination) {
            super(destination);
        }

        @Override
        protected List<? extends PostModel> mergeWithExistingData(ContentTypeIdentifier typeofData, List<? extends PostModel> oldData, List<? extends PostModel> newData) {
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
