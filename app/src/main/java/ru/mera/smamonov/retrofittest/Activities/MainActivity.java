package ru.mera.smamonov.retrofittest.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;

import retrofit2.Response;
import ru.mera.smamonov.retrofittest.Adapters.RecycleViewAdapterLamp;
import ru.mera.smamonov.retrofittest.HgInterface.HgInterface;
import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    RecyclerView m_recyRecyclerView = null;
    HgInterface m_gitHubService = null;

    private void getLamps()
    {
        Call<List<Lamp>> call = m_gitHubService.getLamps();

        call.enqueue(new Callback<List<Lamp>>(){
            @Override
            public
            void onResponse(Call<List<Lamp>> call, Response<List<Lamp>> response)
            {
                if (response.isSuccessful()) {
                    // tasks available
                    RecycleViewAdapterLamp adapter = new RecycleViewAdapterLamp(response.body());
                    m_recyRecyclerView.setAdapter(adapter);
                } else {
                    // error response, no access to resource?
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected
             * exception occurred creating the request or processing the response.
             */
            @Override
            public
            void onFailure(Call<List<Lamp>> call, Throwable t)
            {
                Log.d("Error", t.getMessage());
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_gitHubService = HgInterface.retrofit.create(HgInterface.class);
        m_recyRecyclerView = (RecyclerView)findViewById(R.id.RecycleViewMainActivity);

        LinearLayoutManager llm = new LinearLayoutManager(this.getBaseContext());
        m_recyRecyclerView.setLayoutManager(llm);

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
    }
}
