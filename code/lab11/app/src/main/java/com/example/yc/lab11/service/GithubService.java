package com.example.yc.lab11.service;

import android.database.Observable;

import com.example.yc.lab11.model.Github;
import com.example.yc.lab11.model.Repos;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by yc on 2017/12/21.
 */

public interface GithubService {
    @GET("/users/{user}")
    Call<Github> getUser(@Path("user") String user);
}
