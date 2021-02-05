package com.omada.junction.data;

import androidx.annotation.Nullable;

import com.omada.junction.data.handler.AppDataHandler;
import com.omada.junction.data.handler.ArticleDataHandler;
import com.omada.junction.data.handler.ImageUploadHandler;
import com.omada.junction.data.handler.UserDataHandler;
import com.omada.junction.data.handler.EventDataHandler;
import com.omada.junction.data.handler.OrganizationDataHandler;
import com.omada.junction.data.handler.PostDataHandler;
import com.omada.junction.data.handler.ShowcaseDataHandler;
import com.omada.junction.utils.StringUtilities;

import java.util.HashMap;
import java.util.Map;

public class DataRepository {

    // All the data related to state is to be stored here so that it can be read from handlers
    // private because the get and set operations need to be performed atomically through synchronized
    // methods which are package-private
    private static final Map<DataRepositoryAccessIdentifier, DataRepositoryAccessorData> accessTracker = new HashMap<>();

    private static DataRepository dataRepository;

    private final AppDataHandler appDataHandler = new AppDataHandler();
    private final UserDataHandler userDataHandler = new UserDataHandler();
    private final OrganizationDataHandler organizationDataHandler = new OrganizationDataHandler();
    private final ArticleDataHandler articleDataHandler = new ArticleDataHandler();
    private final ShowcaseDataHandler showcaseDataHandler = new ShowcaseDataHandler();
    private final PostDataHandler postDataHandler = new PostDataHandler();
    private final ImageUploadHandler imageUploadHandler = new ImageUploadHandler();
    //this is only for events
    private final EventDataHandler eventDataHandler = new EventDataHandler();


    private DataRepository(){
        //class constructor init firestore and local db etc here if required
    }

    public static synchronized DataRepository getInstance() {
        if (dataRepository == null) {
            dataRepository = new DataRepository();
        }
        return dataRepository;
    }

    public AppDataHandler getAppDataHandler() {
        return appDataHandler;
    }

    public UserDataHandler getUserDataHandler() {
        return userDataHandler;
    }

    public EventDataHandler getEventDataHandler() {
        return eventDataHandler;
    }

    public ArticleDataHandler getArticleDataHandler() {
        return articleDataHandler;
    }

    public OrganizationDataHandler getOrganizationDataHandler() {
        return organizationDataHandler;
    }

    public ShowcaseDataHandler getShowcaseDataHandler() {
        return showcaseDataHandler;
    }

    public PostDataHandler getPostDataHandler() {
        return postDataHandler;
    }

    // Only for external accessors

    public synchronized static DataRepositoryAccessIdentifier registerForDataRepositoryAccess() {

        DataRepositoryAccessIdentifier identifier = new DataRepositoryAccessIdentifier(
                StringUtilities.randomAlphabetGenerator(6)
        );
        accessTracker.put(identifier, new DataRepositoryAccessorData());
        return identifier;
    }

    public synchronized static void removeDataRepositoryAccessRegistration(DataRepositoryAccessIdentifier accessIdentifier) {
        accessTracker.remove(accessIdentifier);
    }

    static DataRepositoryHandlerIdentifier registerDataHandler() {
        return new DataRepositoryHandlerIdentifier(
                StringUtilities.randomAlphabetGenerator(6)
        );

    }

    public ImageUploadHandler getImageUploadHandler() {
        return imageUploadHandler;
    }

    // This class identifies the data repository accessor so that state can be tracked without
    // creating new instances for each accessor
    public static class DataRepositoryAccessIdentifier {


        private final String id;

        private DataRepositoryAccessIdentifier(String id){
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            DataRepositoryAccessIdentifier other = (DataRepositoryAccessIdentifier) obj;
            return this.id.equals(other.id);
        }

    }

    // Identifies each handler so that the data related to it can be stored in the data
    // repository accessor data
    public static class DataRepositoryHandlerIdentifier {


        private final String id;

        private DataRepositoryHandlerIdentifier(String id){
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (this.getClass() != obj.getClass()) {
                return false;
            }
            DataRepositoryHandlerIdentifier other = (DataRepositoryHandlerIdentifier) obj;
            return this.id.equals(other.id);
        }

    }

    // contains all the data that an accessor needs for each handler
    private static class DataRepositoryAccessorData {

        private final Map<DataRepositoryHandlerIdentifier, Map<String, Object>>  accessorData = new HashMap<>();

        public synchronized void putHandlerData(
                DataRepositoryHandlerIdentifier handlerIdentifier, String key, Object value) {

            Map<String, Object> handlerData = accessorData.get(handlerIdentifier);

            if(handlerData == null) {
                handlerData = new HashMap<>();
                accessorData.put(handlerIdentifier, handlerData);
            }

            handlerData.put(key, value);
        }

        public synchronized Object getHandlerData(DataRepositoryHandlerIdentifier handlerIdentifier, String key) {

            Map<String, Object> handlerData = accessorData.get(handlerIdentifier);

            if(handlerData == null) {
                handlerData = new HashMap<>();
                accessorData.put(handlerIdentifier, handlerData);
                return null;
            }

            return handlerData.get(key);
        }
    }
    public void resetHomeFeedContent() {
        resetForYouFeedContent();
        resetLearnFeedContent();
        resetCompeteFeedContent();
    }

    private void resetForYouFeedContent() {
        eventDataHandler.resetLastForYouEvent();
        articleDataHandler.resetLastForYouArticle();
    }

    private void resetLearnFeedContent() {
    }

    private void resetCompeteFeedContent() {
    }

    public void resetInstituteFeedContent() {
        postDataHandler.resetLastInstituteHighlight();
        organizationDataHandler.resetLastInstituteOrganization();
    }
}
