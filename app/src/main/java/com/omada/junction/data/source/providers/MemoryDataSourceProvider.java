package com.omada.junction.data.source.providers;

import com.omada.junction.data.source.DataSourceProvider;
import com.omada.junction.data.source.InstituteDataSource;
import com.omada.junction.data.source.NotificationDataSource;
import com.omada.junction.data.source.OrganizationDataSource;
import com.omada.junction.data.source.PostDataSource;
import com.omada.junction.data.source.ShowcaseDataSource;
import com.omada.junction.data.source.UserDataSource;
import com.omada.junction.data.source.VenueDataSource;

public class MemoryDataSourceProvider implements DataSourceProvider {

    @Override
    public InstituteDataSource getInstituteDataSource() {
        return null;
    }

    @Override
    public NotificationDataSource getNotificationDataSource() {
        return null;
    }

    @Override
    public OrganizationDataSource getOrganizationDataSource() {
        return null;
    }

    @Override
    public UserDataSource getUserDataSource() {
        return null;
    }

    @Override
    public PostDataSource getPostDataSource() {
        return null;
    }

    @Override
    public ShowcaseDataSource getShowcaseDataSource() {
        return null;
    }

    @Override
    public VenueDataSource getVenueDataSource() {
        return null;
    }
}
