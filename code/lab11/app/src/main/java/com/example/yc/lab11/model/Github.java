package com.example.yc.lab11.model;

/**
 * Created by yc on 2017/12/21.
 */


public class Github {

    private String login;
    private String id;
    private String blog;

    public Github(String login, String id, String blog) {
        this.login = login;
        this.id = id;
        this.blog = blog;
    }

    public String getLogin() {
        return login;
    }

    public String getId() {
        return id;
    }

    public String getBlog() {
        return blog;
    }

}