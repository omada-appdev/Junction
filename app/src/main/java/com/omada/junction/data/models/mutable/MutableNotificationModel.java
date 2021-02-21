package com.omada.junction.data.models.mutable;

import com.omada.junction.data.models.external.NotificationModel;

import java.util.Map;

public class MutableNotificationModel extends NotificationModel {

    public MutableNotificationModel() {
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
