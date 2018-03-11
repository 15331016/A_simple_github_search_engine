package com.example.yc.lab11.model;

/**
 * Created by yc on 2017/12/21.
 */


public class Repos {

    private String name;
    private String language;
    private String description;

    public Repos(String name, String language, String description) {
        this.name = name;
        this.language = language;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getLanguage() {
        return language;
    }

    public String getDescription() {
        return description;
    }

}