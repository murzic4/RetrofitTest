package ru.mera.smamonov.retrofittest.controller;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.mera.smamonov.retrofittest.com.tilgin.model.ApiResponse;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;

/**
 * Created by sergeym on 29.03.2017.
 */

public interface LampInterface {
    @GET("lamps")
    Call<List<Lamp>> getLamps();

    @GET("lamps/{device_id}")
    Call<Lamp> getLamp(@Path("device_id") String device_id);

    @PUT("lamps/{device_id}/{swithced}")
    Call<ApiResponse> setLamp(@Path("device_id") String device_id,
                              @Path("swithced") Boolean swithced);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.0.2:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}


