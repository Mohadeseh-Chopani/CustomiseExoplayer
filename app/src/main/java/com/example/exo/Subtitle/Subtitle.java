package com.example.exo.Subtitle;

import com.example.exo.Model.DataPlayer;

public class Subtitle {
    private String url;
    private String language;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Subtitle(String url, String id, String language) {
        this.url = url;
        this.language = language;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }
}
