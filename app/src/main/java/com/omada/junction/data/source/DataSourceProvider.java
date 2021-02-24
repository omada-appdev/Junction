package com.omada.junction.data.source;

public interface DataSourceProvider {
    InstituteDataSource getInstituteDataSource();
    NotificationDataSource getNotificationDataSource();
    OrganizationDataSource getOrganizationDataSource();
    UserDataSource getUserDataSource();
    PostDataSource getPostDataSource();
    ShowcaseDataSource getShowcaseDataSource();
    VenueDataSource getVenueDataSource();
}