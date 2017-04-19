package ru.mera.smamonov.retrofittest.controller;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.model.ApiResponse;
import ru.mera.smamonov.retrofittest.model.Lamp;
import ru.mera.smamonov.retrofittest.model.Scene;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sergeym on 31.03.2017.
 */

public class IotManager {

    public interface FailureListener {
        void OnFailure(Throwable t);

        void OnFailure(String error);
    }

    public interface GetListListener<DeviceType> extends FailureListener {
        void OnSuccess(final List<DeviceType> devices);
    }

    public interface SetListener<DeviceType> extends FailureListener {
        void OnSuccess(DeviceType device);
    }

    public interface GetListener<DeviceType> extends FailureListener {
        void OnSuccess(DeviceType device);
    }

    public interface CreateListener<DeviceType> extends FailureListener {
        void OnSuccess(DeviceType device);
    }

    public interface DeleteListener<DeviceType> extends FailureListener {
        void OnSuccess(DeviceType device);
    }

    private IotInterface m_interface = null;
    private final String LOG_TAG = "IotManager";
    private static Retrofit m_retrofit = null;

    public IotManager() {
        String url = AppContext.getAppContext().getSharedPreferences(AppContext.getAppContext().getResources().getString(R.string.settings_file_name),
                MODE_PRIVATE).getString("url", AppContext.getAppContext().getString(R.string.default_url));

        configureRetrofit(url);
    }

    public void configureRetrofit(String url) {
        m_retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        m_interface = m_retrofit.create(IotInterface.class);
    }

    public void getLamps(final GetListListener<Lamp> getLampsListener) {

        Call<List<Lamp>> call = m_interface.getLamps();

        call.enqueue(new Callback<List<Lamp>>() {

            private final GetListListener<Lamp> m_listener = getLampsListener;

            @Override
            public void onResponse(Call<List<Lamp>> call,
                                   Response<List<Lamp>> response) {

                if (m_listener != null) {

                    if (response.isSuccessful()) {
                        List<Lamp> result = response.body();
                        m_listener.OnSuccess(result);
                    } else {
                        Log.e(LOG_TAG, "onResponse" + response.message());
                        m_listener.OnFailure(response.message());
                    }
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            @Override
            public void onFailure(Call<List<Lamp>> call, Throwable t) {
                Log.e(LOG_TAG, "onFailure: " + t.getMessage());
                if (m_listener != null) {
                    m_listener.OnFailure(t);
                }
            }
        });
    }

    public void getLamp(String device_id,
                        final GetListener<Lamp> getLampListener) {

        Call<Lamp> call = m_interface.getLamp(device_id);
        call.enqueue(new Callback<Lamp>() {

            private final GetListener<Lamp> listener = getLampListener;

            @Override
            public void onResponse(Call<Lamp> call,
                                   Response<Lamp> response) {

                if (listener != null) {
                    if (response.isSuccessful()) {
                        Lamp result = response.body();
                        listener.OnSuccess(result);
                    } else {
                        Log.e(LOG_TAG, "onResponse" + response.message());
                        listener.OnFailure(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<Lamp> call, Throwable t) {
                Log.e(LOG_TAG, "onFailure: " + t.getMessage());
                if (listener != null) {
                    listener.OnFailure(t);
                }
            }
        });
    }

    public void setLamp(final Lamp lamp,
                        final SetListener<Lamp> updateListener) {

        Call<ApiResponse> response = m_interface.setLamp(lamp.getUuid(), lamp.getSwitched());

        response.enqueue(new Callback<ApiResponse>() {
            final SetListener<Lamp> listener = updateListener;

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.e("Error", "onResponse: " + String.valueOf(response.isSuccessful()));

                if (listener != null) {
                    ApiResponse apiResponse = response.body();

                    Log.d(LOG_TAG, "apiResponse code: " + apiResponse.getCode());
                    Log.d(LOG_TAG, "apiResponse message: " + apiResponse.getMessage());

                    if (response.isSuccessful() &&
                            apiResponse.getCode() == ApiResponse.RESULT_OK) {
                        listener.OnSuccess(lamp);
                    } else {
                        listener.OnFailure(apiResponse.getMessage());
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

    /*************************************************************************************
     * Scenes
     *************************************************************************************/
    public void getScenes(final GetListListener<Scene> getListListener) {

        Call<List<Scene>> call = m_interface.getSceneList();

        call.enqueue(new Callback<List<Scene>>() {

            private final GetListListener<Scene> listener = getListListener;

            @Override
            public void onResponse(Call<List<Scene>> call,
                                   Response<List<Scene>> response) {

                if (listener != null) {

                    if (response.isSuccessful()) {
                        List<Scene> result = response.body();
                        listener.OnSuccess(result);
                    } else {
                        Log.e(LOG_TAG, "onResponse" + response.message());
                        listener.OnFailure(response.message());
                    }
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            @Override
            public void onFailure(Call<List<Scene>> call, Throwable t) {
                Log.e(LOG_TAG, "onFailure: " + t.getMessage());
                if (listener != null) {
                    listener.OnFailure(t);
                }
            }
        });
    }

    public void getScene(String device_id,
                         final GetListener<Scene> getListener) {

        Call<Scene> call = m_interface.getScene(device_id);
        call.enqueue(new Callback<Scene>() {

            private final GetListener<Scene> m_listener = getListener;

            @Override
            public void onResponse(Call<Scene> call,
                                   Response<Scene> response) {

                if (m_listener != null) {
                    if (response.isSuccessful()) {
                        Scene result = response.body();
                        m_listener.OnSuccess(result);
                    } else {
                        Log.e(LOG_TAG, "onResponse" + response.message());
                        m_listener.OnFailure(response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<Scene> call, Throwable t) {
                Log.e(LOG_TAG, "onFailure: " + t.getMessage());
                if (m_listener != null) {
                    m_listener.OnFailure(t);
                }
            }
        });
    }

    public void setScene(final Scene device,
                         final SetListener<Scene> updateListener) {

        Call<ApiResponse> response = m_interface.setScene(device.getUuid(), device);

        response.enqueue(new Callback<ApiResponse>() {
            final IotManager.SetListener<Scene> m_listener = updateListener;

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.e("Error", "onResponse: " + String.valueOf(response.isSuccessful()));

                if (m_listener != null) {

                    ApiResponse apiResponse = response.body();

                    if (apiResponse != null &&
                            response.isSuccessful() &&
                            apiResponse.getCode() == ApiResponse.RESULT_OK) {
                        Log.d(LOG_TAG, "apiResponse code: " + apiResponse.getCode());
                        Log.d(LOG_TAG, "apiResponse message: " + apiResponse.getMessage());
                        m_listener.OnSuccess(device);
                    } else {
                        m_listener.OnFailure("Empty response");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (m_listener != null) {
                    m_listener.OnFailure(t);
                }
            }
        });
    }

    public void createScene(final Scene device,
                            final IotManager.CreateListener<Scene> createListener) {

        Call<ApiResponse> response = m_interface.createScene(device);

        response.enqueue(new Callback<ApiResponse>() {
            final IotManager.CreateListener<Scene> m_listener = createListener;

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.e("Error", "onResponse: " + String.valueOf(response.isSuccessful()));

                if (m_listener != null) {
                    ApiResponse apiResponse = response.body();

                    Log.d(LOG_TAG, "apiResponse code: " + apiResponse.getCode());
                    Log.d(LOG_TAG, "apiResponse message: " + apiResponse.getMessage());

                    if (response.isSuccessful() &&
                            apiResponse.getCode() == ApiResponse.RESULT_OK) {
                        m_listener.OnSuccess(device);
                    } else {
                        m_listener.OnFailure(apiResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (m_listener != null) {
                    m_listener.OnFailure(t);
                }
            }
        });
    }

    public void deleteScene(final Scene device,
                            final IotManager.DeleteListener<Scene> deleteListener) {

        Call<ApiResponse> response = m_interface.deleteScene(device.getUuid());

        response.enqueue(new Callback<ApiResponse>() {
            final IotManager.DeleteListener<Scene> m_listener = deleteListener;

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.e("Error", "onResponse: " + String.valueOf(response.isSuccessful()));

                if (m_listener != null) {
                    ApiResponse apiResponse = response.body();

                    Log.d(LOG_TAG, "apiResponse code: " + apiResponse.getCode());
                    Log.d(LOG_TAG, "apiResponse message: " + apiResponse.getMessage());

                    if (response.isSuccessful() &&
                            apiResponse.getCode() == ApiResponse.RESULT_OK) {
                        m_listener.OnSuccess(device);
                    } else {
                        m_listener.OnFailure(apiResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (m_listener != null) {
                    m_listener.OnFailure(t);
                }
            }
        });
    }


}
