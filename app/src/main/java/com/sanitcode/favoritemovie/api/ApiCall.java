package com.sanitcode.favoritemovie.api;

import com.sanitcode.favoritemovie.data.model.ResultItems;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCall {
    @GET("movie/{id}")
    Call<ResultItems> getMovieById(@Path("id") String id, @Query("api_key") String apiKey);
}
