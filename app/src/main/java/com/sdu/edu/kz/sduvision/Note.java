package com.sdu.edu.kz.sduvision;

public class Note {
    private String content;
    private long timestamp;
    private String title;

    public Note() {}
    public Note(String title, String content, long timestamp) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
