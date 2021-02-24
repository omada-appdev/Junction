package com.omada.junction.data.models.external;

import android.os.Parcel;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import me.shouheng.utils.data.StringUtils;

public class NotificationModel extends BaseModel {

    protected String notificationType;
    protected String sourceType;
    protected String source;
    protected String title;
    protected String text;
    protected String status;
    protected Map<String, Object> data;

    protected NotificationModel() {
    }

    protected NotificationModel(Parcel in) {
        id = in.readString();
        source = in.readString();
        sourceType = in.readString();
        notificationType = in.readString();
        title = in.readString();
        text = in.readString();
        status = in.readString();
        data = (Map<String, Object>) in.readSerializable();
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };

    public String getSource() {
        return source;
    }

    public String getSourceType() {
        return sourceType;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, Object> getData() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(source);
        dest.writeString(sourceType);
        dest.writeString(notificationType);
        dest.writeString(title);
        dest.writeString(text);
        dest.writeString(status);
        dest.writeSerializable((Serializable) data);
    }

    @NonNull
    @Override
    public final String toString() {
        return StringUtils.connect(Arrays.asList(notificationType, sourceType, source, title), " ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationModel model = (NotificationModel) o;
        return Objects.equals(notificationType, model.notificationType) &&
                Objects.equals(sourceType, model.sourceType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notificationType, sourceType);
    }
}
