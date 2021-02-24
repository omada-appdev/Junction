package com.omada.junction.data.source;

import com.google.android.gms.tasks.Task;
import com.omada.junction.data.models.external.ArticleModel;
import com.omada.junction.data.models.external.EventModel;
import com.omada.junction.data.models.external.PostModel;

import java.util.List;

public interface PostDataSource extends DataSource {
    Task<List<PostModel>> getShowcasePosts(String showcaseId);
    Task<List<PostModel>> getOrganizationHighlights(String organizationId);
    Task<List<PostModel>> getInstituteHighlights(String instituteId);
    Task<EventModel> getEventDetails(String postId);
    Task<ArticleModel> getArticleDetails(String postId);
}
