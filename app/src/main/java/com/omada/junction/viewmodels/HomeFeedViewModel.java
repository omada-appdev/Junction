package com.omada.junction.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.omada.junction.data.DataRepository;
import com.omada.junction.data.handler.AuthDataHandler;
import com.omada.junction.data.models.ArticleModel;
import com.omada.junction.data.models.BaseModel;
import com.omada.junction.data.models.EventModel;
import com.omada.junction.utils.taskhandler.LiveDataAggregator;
import com.omada.junction.utils.taskhandler.LiveEvent;

import java.util.ArrayList;
import java.util.List;

public class HomeFeedViewModel extends ViewModel {

    private enum ContentTypeIdentifier {
        CONTENT_TYPE_EVENT,
        CONTENT_TYPE_ARTICLE,
        CONTENT_TYPE_ANNOUNCEMENT,
        CONTENT_TYPE_ADVERTISEMENT
    }


    /*
    ########################
    #   REMOVE THIS FIELD  #
    ########################
     */
    public final MutableLiveData<String> test = new MutableLiveData<>();

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

    private final MediatorLiveData<List<BaseModel>> loadedForYou = new MediatorLiveData<>();
    private final MediatorLiveData<List<BaseModel>> loadedLearn = new MediatorLiveData<>();
    private final MediatorLiveData<List<BaseModel>> loadedCompete = new MediatorLiveData<>();

    // TODO remove if not needed
    private final LiveData<LiveEvent<Boolean>> forYouRefreshNotifier = new MutableLiveData<>();
    private final LiveData<LiveEvent<Boolean>> learnRefreshNotifier = new MutableLiveData<>();
    private final LiveData<LiveEvent<Boolean>> competeRefreshNotifier = new MutableLiveData<>();

    /*
    ###########################
    # FIELDS FOR INTERNAL USE #
    ###########################
     */
    private final ForYouAggregator forYouAggregator = new ForYouAggregator(loadedForYou);


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
                DataRepository.getInstance().getEventDataHandler().getLoadedAllEventsNotifier(), allEvents-> allEvents);
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

    //get all required content and store internally or in live data
    public void getHomeFeed(){

        DataRepository
                .getInstance()
                .getEventDataHandler()
                .getAllEvents();

        DataRepository
                .getInstance()
                .getArticleDataHandler()
                .getAllArticles();
    }


    // Apply things like sorting, etc. in aggregator
    private static class ForYouAggregator extends LiveDataAggregator<ContentTypeIdentifier, List<? extends BaseModel>, List<BaseModel>>{

        public ForYouAggregator(MediatorLiveData<List<BaseModel>> destination) {
            super(destination);
        }

        @Override
        public void holdData(ContentTypeIdentifier typeOfData, List<? extends BaseModel> data) {

            if(data == null || data.size() == 0){
                return;
            }
            
            switch (typeOfData) {
                case CONTENT_TYPE_EVENT:
                    if (!(data.get(0) instanceof EventModel)) return;
                    break;
                case CONTENT_TYPE_ARTICLE:
                    if (!(data.get(0) instanceof ArticleModel)) return;
                    break;
                case CONTENT_TYPE_ANNOUNCEMENT:
                    break;
                case CONTENT_TYPE_ADVERTISEMENT:
                    break;
            }

            super.holdData(typeOfData, data);
        }

        @Override
        protected List<BaseModel> mergeWithExistingData(ContentTypeIdentifier typeOfData, List<? extends BaseModel> oldData, List<? extends BaseModel> newData) {
            return null;
        }

        @Override
        protected boolean checkDataForAggregability() {
            return dataOnHold.get(ContentTypeIdentifier.CONTENT_TYPE_ARTICLE) != null &&
                    dataOnHold.get(ContentTypeIdentifier.CONTENT_TYPE_EVENT) != null;
        }

        @SuppressWarnings({"ConstantConditions"})
        @Override
        protected void aggregateData() {
            List<BaseModel> aggregatedList = new ArrayList<>();
            aggregatedList.addAll(dataOnHold.get(ContentTypeIdentifier.CONTENT_TYPE_EVENT));
            aggregatedList.addAll(dataOnHold.get(ContentTypeIdentifier.CONTENT_TYPE_ARTICLE));
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
