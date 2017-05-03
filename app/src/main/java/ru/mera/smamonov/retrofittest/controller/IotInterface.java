package ru.mera.smamonov.retrofittest.controller;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.mera.smamonov.retrofittest.model.ApiResponse;
import ru.mera.smamonov.retrofittest.model.Lamp;
import ru.mera.smamonov.retrofittest.model.Scene;

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
                              @Body Lamp lamp);

    /*************************************************************************************
     * Scenes
     *************************************************************************************/
    @GET("scenes/")
    Call<List<Scene>> getSceneList();

    @GET("scenes/{scene_id}")
    Call<Scene> getScene(@Path("scene_id") String device_id);

    @PUT("scenes/{scene_id}")
    Call<ApiResponse> setScene(@Path("scene_id") String device_id,
                               @Body Scene scence);

    @POST("scenes/")
    Call<ApiResponse> createScene(@Body Scene device);

    @DELETE("scenes/{scene_id}")
    Call<ApiResponse> deleteScene(@Path("scene_id") String device_id);
}


