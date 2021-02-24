package com.omada.junction.data.source;

import com.google.android.gms.tasks.Task;
import com.omada.junction.data.models.external.NotificationModel;

import java.util.List;

public interface NotificationDataSource extends DataSource {

    Task<List<NotificationModel>> getPendingOrganizationNotifications();
    Task<List<NotificationModel>> getPendingInstituteNotifications();
}
