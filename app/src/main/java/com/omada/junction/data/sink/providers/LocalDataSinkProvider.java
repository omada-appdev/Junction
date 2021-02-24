package com.omada.junction.data.sink.providers;

import com.omada.junction.data.sink.DataSinkProvider;
import com.omada.junction.data.sink.InstituteDataSink;
import com.omada.junction.data.sink.NotificationDataSink;
import com.omada.junction.data.sink.OrganizationDataSink;
import com.omada.junction.data.sink.PostDataSink;
import com.omada.junction.data.sink.ShowcaseDataSink;
import com.omada.junction.data.sink.UserDataSink;
import com.omada.junction.data.sink.VenueDataSink;

public class LocalDataSinkProvider implements DataSinkProvider {

    @Override
    public InstituteDataSink getInstituteDataSink() {
        return null;
    }

    @Override
    public NotificationDataSink getNotificationDataSink() {
        return null;
    }

    @Override
    public OrganizationDataSink getOrganizationDataSink() {
        return null;
    }

    @Override
    public PostDataSink getPostDataSink() {
        return null;
    }

    @Override
    public UserDataSink getUserDataSink() {
        return null;
    }

    @Override
    public ShowcaseDataSink getShowcaseDataSink() {
        return null;
    }

    @Override
    public VenueDataSink getVenueDataSink() {
        return null;
    }
}
