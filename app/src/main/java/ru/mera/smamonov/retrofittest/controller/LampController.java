package ru.mera.smamonov.retrofittest.controller;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mera.smamonov.retrofittest.com.tilgin.model.ApiResponse;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;

/**
 * Created by sergeym on 31.03.2017.
 */

public class LampController {

    public interface FailureListener {
        void OnFailure(Throwable t);
    }

    public interface GetLampsListener extends FailureListener {
        void OnSuccess(List<Lamp> lamps, String error);
    }

    public interface UpdateListener extends FailureListener {
        void OnSuccess(Lamp lamp, String error);
    }

    public interface GetLampListener extends FailureListener {
        void OnSuccess(Lamp lamp, String error);
    }

    private LampInterface m_gitHubService = null;

    public LampController() {
        m_gitHubService = LampInterface.retrofit.create(LampInterface.class);
    }

    public void getLamps(final GetLampsListener getLampsListener) {

        Call<List<Lamp>> call = m_gitHubService.getLamps();

        call.enqueue(new Callback<List<Lamp>>() {

            private final GetLampsListener listener = getLampsListener;

            @Override
            public void onResponse(Call<List<Lamp>> call, Response<List<Lamp>> response) {

                List<Lamp> result = null;

                if (response.isSuccessful()) {
                    result = response.body();
                } else {
                    Log.d("Error:", response.message());
                }

                if (listener != null) {
                    listener.OnSuccess(response.body(), response.message());
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            @Override
            public void onFailure(Call<List<Lamp>> call, Throwable t) {
                Log.e("Error", t.getMessage());
                if (listener != null) {
                    listener.OnFailure(t);
                }
            }
        });
    }

    public void getLamp(String device_id,
                        final GetLampListener getLampListener) {


        Call<Lamp> call = m_gitHubService.getLamp(device_id);
        call.enqueue(new Callback<Lamp>() {

            private final GetLampListener listener = getLampListener;

            @Override
            public void onResponse(Call<Lamp> call, Response<Lamp> response) {

                Lamp result = null;

                if (response.isSuccessful()) {
                    // tasks available
                    result = response.body();
                } else {
                    Log.d("Error:", response.message());
                }

                if (listener != null) {
                    listener.OnSuccess(result, response.message());
                }
            }

            @Override
            public void onFailure(Call<Lamp> call, Throwable t) {

                Log.e("Error", t.getMessage());
                if (listener != null) {
                    listener.OnFailure(t);
                }
            }
        });
    }

    public void setLamp(final Lamp lamp, final UpdateListener updateListener) {

        Call<ApiResponse> response = m_gitHubService.setLamp(lamp.getUuid(), lamp.getSwitched());

        response.enqueue(new Callback<ApiResponse>() {
            final UpdateListener listener = updateListener;

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.e("Error", "onResponse: " + String.valueOf(response.isSuccessful()));

                if (listener != null) {
                    ApiResponse apiResponse = response.body();

                    Log.e("Error", "apiResponse code: " + apiResponse.getCode());
                    Log.e("Error", "apiResponse message: " + apiResponse.getMessage());

                    if (response.isSuccessful() &&
                            apiResponse != null &&
                            apiResponse.getCode() == ApiResponse.RESULT_OK) {
                        listener.OnSuccess(lamp, apiResponse.getMessage());
                    } else {
                        listener.OnSuccess(null, apiResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (listener != null) {
                    listener.OnFailure(t);
                }
            }
        });

    }

}
