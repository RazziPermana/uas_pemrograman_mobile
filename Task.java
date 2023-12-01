package com.example.uastest;

public class Task {
    private int id;
    private int userId;
    private String description;
    private String timestamp;
    private String title;
    private User user;

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    private String linkUrl;


    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    private String selectedType;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle(){return title;}
    public void setTitle(String title){this.title = title;}

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
