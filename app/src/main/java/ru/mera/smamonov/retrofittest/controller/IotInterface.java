package ru.mera.smamonov.retrofittest.controller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.mera.smamonov.retrofittest.com.tilgin.model.ApiResponse;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Scene;

/**
 * Created by sergeym on 29.03.2017.
 */

public interface IotInterface {
    /*************************************************************************************
     * LAMPS
     *************************************************************************************/
    @GET("lamps")
    Call<List<Lamp>> getLamps();

    @GET("lamps/{device_id}/")
    Call<Lamp> getLamp(@Path("device_id") String device_id);

    @PUT("lamps/{device_id}/")
    Call<ApiResponse> setLamp(@Path("device_id") String device_id,
                              @Query("switched") Boolean swithced);

    /*************************************************************************************
     * Scenes
     *************************************************************************************/
    @GET("scences/")
    Call<List<Scene>> getSceneList();

    @GET("scences/{scence_id}")
    Call<Scene> getScene(@Path("scence_id") String scence_id);

    @PUT("scences/{scence_id}")
    Call<ApiResponse> setScene(@Path("scence_id") String scence_id,
                               @Body Scene scence);

    @POST("scences/")
    Call<ApiResponse> createScene(@Body Scene device);

    @DELETE("scences/{scence_id}")
    Call<ApiResponse> deleteScene(@Path("scence_id") String device_id);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.56.1:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}


