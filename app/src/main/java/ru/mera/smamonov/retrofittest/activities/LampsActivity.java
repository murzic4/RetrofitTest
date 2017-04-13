package ru.mera.smamonov.retrofittest.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.adapters.LampsRecycleViewAdapter;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.controller.IotManager;
import ru.mera.smamonov.retrofittest.model.Lamp;

public class LampsActivity extends AppCompatActivity {

    private static final String LOG_TAG = "LampsActivity";

    RecyclerView m_recyRecyclerView = null;
    IotManager m_iotManager = AppContext.getIotManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamps);
        m_recyRecyclerView = (RecyclerView) findViewById(R.id.RecycleViewMainActivity);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        m_recyRecyclerView.setLayoutManager(linearLayoutManager);

        final LampsActivity lampsActivity = this;

        m_iotManager.getLamps(new IotManager.GetListListener<Lamp>() {

            @Override
            public void OnSuccess(List<Lamp> lamps) {
                LampsRecycleViewAdapter adapter = new LampsRecycleViewAdapter(lamps,
                        lampsActivity,
                        new LampsRecycleViewAdapter.SetLampListener() {
                            @Override
                            public void onLampSet(final Lamp lamp) {
                                Log.d(LOG_TAG, "lamp_layout state is " + lamp.getSwitched());
                                Log.d(LOG_TAG, "lamp_layout state change_to " + lamp.getSwitched());
                                AppContext.getIotManager().setLamp(lamp,
                                        new IotManager.SetListener<Lamp>() {
                                            @Override
                                            public void OnFailure(Throwable t) {
                                                Log.e(LOG_TAG,
                                                        "Unable to set: " +
                                                                lamp.getUuid() +
                                                                " reason:" +
                                                                t.getMessage());
                                            }

                                            @Override
                                            public void OnFailure(String error) {
                                                Log.e(LOG_TAG,
                                                        "Unable to set: " +
                                                                lamp.getUuid() +
                                                                " reason:" +
                                                                error);
                                            }

                                            @Override
                                            public void OnSuccess(Lamp lamp) {
                                                lamp.Update();
                                            }
                                        }
                                );
                            }
                        });
                m_recyRecyclerView.setAdapter(adapter);
            }

            @Override
            public void OnFailure(Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }

            @Override
            public void OnFailure(String error) {
                Log.e(LOG_TAG, error);
            }
        });
    }
}
