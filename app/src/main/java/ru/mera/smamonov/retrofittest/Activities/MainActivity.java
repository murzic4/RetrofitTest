package ru.mera.smamonov.retrofittest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import ru.mera.smamonov.retrofittest.Adapters.RecycleViewAdapterLamp;
import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.com.tilgin.model.ApiResponse;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;
import ru.mera.smamonov.retrofittest.controller.LampController;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    RecyclerView m_recyRecyclerView = null;
    LampController m_lampController = null;

    /*
    private void getLamps() {
        Call<List<Lamp>> call = m_gitHubService.getLamps();

        call.enqueue(new Callback<List<Lamp>>() {
            @Override
            public void onResponse(Call<List<Lamp>> call, Response<List<Lamp>> response) {
                if (response.isSuccessful()) {
                    // tasks available
                    RecycleViewAdapterLamp adapter = new RecycleViewAdapterLamp(response.body());
                    m_recyRecyclerView.setAdapter(adapter);
                } else {
                    Log.d("Error:", response.message());
                }
            }

              @Override
            public void onFailure(Call<List<Lamp>> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }


    private void getLamp(String device_id) {
        Call<Lamp> call = m_gitHubService.getLamp(device_id);
        call.enqueue(new Callback<Lamp>() {
            @Override
            public void onResponse(Call<Lamp> call, Response<Lamp> response) {
                if (response.isSuccessful()) {
                    // tasks available
                    Lamp lamp = response.body();
                } else {
                    Log.d("Error:", response.message());
                }
            }

            @Override
            public void onFailure(Call<Lamp> call, Throwable t) {
                Log.d("Error", t.getMessage());
            }
        });
    }

    private void revertLamp(final Lamp lamp) {

        Call<ApiResponse> response = m_gitHubService.setLamp(lamp.getUuid(), !lamp.getSwitched());

        response.enqueue(new Callback<ApiResponse>() {

            final Lamp m_lamp = lamp;

            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    // tasks available
                    m_lamp.setSwitched(!m_lamp.getSwitched());
                } else {
                    Log.d("Error:", response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_recyRecyclerView = (RecyclerView) findViewById(R.id.RecycleViewMainActivity);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        m_recyRecyclerView.setLayoutManager(llm);

        if(m_lampController == null) {
            m_lampController = new LampController();
        }

        m_lampController.getLamps(new LampController.GetLampsListener() {
                                      @Override
                                      public void OnSuccess(List<Lamp> lamps, String error) {
                                          RecycleViewAdapterLamp adapter = new RecycleViewAdapterLamp(lamps, m_lampController);
                                          m_recyRecyclerView.setAdapter(adapter);
                                      }

                                      @Override
                                      public void OnFailure(Throwable t) {
                                          Log.e("Error:", t.getMessage());
                                      }
                                  });


                //getLamp("lalala");

/*
                Lamp lamp = new Lamp();
        lamp.setName("Lamp #" + String.valueOf(1));
        lamp.setUuid(UUID.randomUUID().toString());
        lamp.setSwitched(true);

        revertLamp(lamp);
*/
/*
        List<Lamp> lamps = new ArrayList<>();
        for (int index = 0;
                index < 100;
                index++)
        {
            Lamp lamp = new Lamp();
            lamp.setName("Lamp #" + String.valueOf(index));
            lamp.setUuid(UUID.randomUUID().toString());
            lamp.setSwitched(true);
            lamps.add(lamp);
        }

        RecycleViewAdapterLamp adapter = new RecycleViewAdapterLamp(lamps);
        m_recyRecyclerView.setAdapter(adapter);
 */
    }
}
