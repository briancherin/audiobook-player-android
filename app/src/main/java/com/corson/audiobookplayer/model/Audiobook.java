package com.corson.audiobookplayer.model;

public class Audiobook {
    private String id;
    private String title;
    private String fileExtension;

    public Audiobook() {}

    public Audiobook(String id, String title, String fileExtension) {
        this.id = id;
        this.title = title;
        this.fileExtension = fileExtension;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
