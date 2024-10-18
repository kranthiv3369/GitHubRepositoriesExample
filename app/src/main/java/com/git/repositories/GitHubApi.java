package com.git.repositories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
public interface GitHubApi {

    // Request with username input
    @GET("users/{username}/repos")
    Call<List<Repository>> getRepositories(
            @Path("username") String username,
            @Query("page") int page,
            @Query("per_page") int perPage
    );

    // Request for example
    @GET("users/google/repos")
    Call<List<Repository>> getRepositories(
            @Query("page") int page,
            @Query("per_page") int perPage
    );
}

