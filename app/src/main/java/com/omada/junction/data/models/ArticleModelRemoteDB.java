package com.omada.junction.data.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.util.Map;

public class ArticleModelRemoteDB extends BaseModel{

    private String id;
    private String title;
    private String creator;
    private String text;
    private String author;

    private Map<String, String> creatorCache;

    private String image;

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("creator")
    public String getCreator() {
        return creator;
    }

    @PropertyName("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @PropertyName("text")
    public String getText() {
        return text;
    }

    @PropertyName("text")
    public void setText(String text) {
        this.text = text;
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("author")
    public String getAuthor() {
        return author;
    }

    @PropertyName("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    @PropertyName("image")
    public String getImage() {
        return image;
    }

    @PropertyName("image")
    public void setImage(String image) {
        this.image = image;
    }

    @PropertyName("creatorCache")
    public Map<String, String> getCreatorCache() {
        return creatorCache;
    }

    @PropertyName("creatorCache")
    public void setCreatorCache(Map<String, String> creatorCache) {
        this.creatorCache = creatorCache;
    }
}
