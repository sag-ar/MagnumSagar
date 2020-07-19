package com.sagar.magnumsagar;

import android.os.Build;

import androidx.annotation.RequiresApi;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {


    @GET("users")
    Call<StackApiResponse> getusers(
            @Query("q") String q,
            @Query("page") int page
    );
}
