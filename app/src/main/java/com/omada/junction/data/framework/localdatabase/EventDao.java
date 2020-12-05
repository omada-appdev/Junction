package com.omada.junction.data.framework.localdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import com.omada.junction.data.models.EventModelLocalDB;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void insertEvents(List<EventModelLocalDB> events);

    @Delete
    void deleteEvents(List<EventModelLocalDB> events);

    //TODO insert query to get all events that are active
    //TODO insert query to remove events that are expired
    //TODO insert query to check latest event timestamp

}
