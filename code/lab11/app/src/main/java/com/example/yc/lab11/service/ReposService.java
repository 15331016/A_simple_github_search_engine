package com.example.yc.lab11.service;

import com.example.yc.lab11.model.Github;
import com.example.yc.lab11.model.Repos;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by yc on 2017/12/21.
 */

public interface ReposService {
    @GET("/users/{user}/repos")
    Call<List<Repos>> listRepos(@Path("user") String user);
}
