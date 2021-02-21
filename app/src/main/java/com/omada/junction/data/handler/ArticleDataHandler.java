package com.omada.junction.data.handler;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.omada.junction.data.BaseDataHandler;
import com.omada.junction.data.DataRepository;
import com.omada.junction.data.models.converter.ArticleModelConverter;
import com.omada.junction.data.models.external.ArticleModel;
import com.omada.junction.data.models.external.PostModel;
import com.omada.junction.data.models.internal.remote.ArticleModelRemoteDB;
import com.omada.junction.utils.taskhandler.LiveDataAggregator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArticleDataHandler extends BaseDataHandler {


    private enum ArticleType{
        ARTICLE_TYPE_LOCAL,
        ARTICLE_TYPE_REMOTE
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
    private MediatorLiveData<List<ArticleModel>> loadedAllArticlesNotifier = new MediatorLiveData<>();
    private final MediatorLiveData<List<ArticleModel>> loadedForYouArticlesNotifier = new MediatorLiveData<>();
    private final MediatorLiveData<List<ArticleModel>> loadedLearnArticlesNotifier = new MediatorLiveData<>();
    private final MediatorLiveData<List<ArticleModel>> loadedCompeteArticlesNotifier = new MediatorLiveData<>();

    /*
    ###########################
    # FIELDS FOR INTERNAL USE #
    ###########################
    */
    private ArticlesAggregator allArticlesAggregator = new ArticlesAggregator(loadedAllArticlesNotifier);
    private ArticleModelConverter articleModelConverter = new ArticleModelConverter();

    public ArticleDataHandler(){
    }

    /*
    This function gets all articles
     */
    public void getAllArticles(){

        MutableLiveData<List<ArticleModel>> localArticles = new MutableLiveData<>();
        MutableLiveData<List<ArticleModel>> remoteArticles = new MutableLiveData<>();

        loadedAllArticlesNotifier.addSource(localArticles, articleModels -> {
            allArticlesAggregator.holdData(ArticleType.ARTICLE_TYPE_LOCAL, articleModels);
        });
        loadedAllArticlesNotifier.addSource(remoteArticles, articleModels -> {
            allArticlesAggregator.holdData(ArticleType.ARTICLE_TYPE_REMOTE, articleModels);
        });

        getAllArticlesFromRemote(remoteArticles);
        //getArticlesFromLocal(localArticles)
    }

    private void getAllArticlesFromRemote(final MutableLiveData<List<ArticleModel>> destinationLiveData){

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
                .whereEqualTo("type", "article")
                .whereIn("creator", following)
                .limit(1);

        if(PaginationHelper.lastAllArticle != null){
            query = query.startAfter(PaginationHelper.lastAllArticle);
        }

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    ArrayList<ArticleModel> loadedArticles = new ArrayList<>();
                    for(QueryDocumentSnapshot snapshot : queryDocumentSnapshots){
                        ArticleModelRemoteDB item = snapshot.toObject(ArticleModelRemoteDB.class);
                        item.setId(snapshot.getId());
                        loadedArticles.add(articleModelConverter.convertRemoteDBToExternalModel(item));
                    }
                    if(queryDocumentSnapshots.size() > 0) {
                        PaginationHelper.lastAllArticle = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size() - 1);
                    }
                    destinationLiveData.setValue(loadedArticles);
                })
                .addOnFailureListener(e -> Log.d("TAG", Objects.requireNonNull(e.getMessage())));
    }

    private void getArticlesFromLocal(final MutableLiveData<ArrayList<ArticleModel>> destinationLiveData){
        //TODO get all articles from local db and remove all the articles that are expired and set live data as above
    }

    public LiveData<List<ArticleModel>> getLoadedAllArticlesNotifier(){
        return loadedAllArticlesNotifier;
    }

    // This class will be used to get cursors for pagination
    private static class PaginationHelper{
        public static DocumentSnapshot lastAllArticle = null;
        public static DocumentSnapshot lastForYouArticle = null;
        public static DocumentSnapshot lastLearnArticle = null;
        public static DocumentSnapshot lastCompeteArticle = null;
        public static DocumentSnapshot lastInstituteArticle = null;
    }
    public void resetLastForYouArticle(){
        PaginationHelper.lastAllArticle = null;
        loadedAllArticlesNotifier = new MediatorLiveData<>();
        allArticlesAggregator = new ArticlesAggregator(loadedAllArticlesNotifier);
    }

    private static class ArticlesAggregator extends LiveDataAggregator<ArticleType, List<ArticleModel>, List<ArticleModel>>{

        public ArticlesAggregator(MediatorLiveData<List<ArticleModel>> destination) {
            super(destination);
        }

        @Override
        public List<ArticleModel> mergeWithExistingData(ArticleType typeOfData, List<ArticleModel> oldData, List<ArticleModel> newData) {
            return newData;
        }

        @Override
        protected boolean checkDataForAggregability() {
            try {
                List<? extends PostModel> remoteArticles = dataOnHold.get(ArticleType.ARTICLE_TYPE_REMOTE);
                //List<? extends BaseModel> localArticles = dataOnHold.get("remoteArticles");

                /*
                if(remoteArticles == null || localArticles == null){
                    throw new Exception("Null values given to aggregator");
                }
                else if(localArticles.size()>0 || remoteArticles.size()>0){
                    throw new Exception("One of the Arrays is empty");
                }
                else{
                    return true;
                }

                 */

                Log.e("Pagination", "aggregable articles " + (remoteArticles != null));

                return remoteArticles != null;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        protected void aggregateData() {

            Log.e("Pagination", "aggregated articles");
            //TODO result.addAll(dataOnHold.get("localArticles"));
            List<ArticleModel> result = new ArrayList<>(dataOnHold.get(ArticleType.ARTICLE_TYPE_REMOTE));

            dataOnHold.put(ArticleType.ARTICLE_TYPE_REMOTE, null);

            destinationLiveData.setValue(result);
        }
    }

}

