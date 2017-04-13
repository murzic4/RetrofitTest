package ru.mera.smamonov.retrofittest.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.adapters.ScenesRecycleViewAdapter;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Scene;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.controller.IotManager;

public class ScenesActivity extends AppCompatActivity {

    private static final String LOG_TAG = "ScenesActivity";

    RecyclerView m_recyRecyclerView = null;
    IotManager m_iotManager = AppContext.getIotManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        IotManager iotManager = new IotManager();
        m_recyRecyclerView = (RecyclerView) findViewById(R.id.RecycleViewSceneActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());

        m_recyRecyclerView.setLayoutManager(linearLayoutManager);

        final ScenesActivity scenesActivity = this;

        iotManager.getScenes(new IotManager.GetListListener<Scene>() {
            @Override
            public void OnSuccess(List<Scene> devices) {
                ScenesRecycleViewAdapter adapter = new ScenesRecycleViewAdapter(devices, scenesActivity);
                m_recyRecyclerView.setAdapter(adapter);
            }

            @Override
            public void OnFailure(Throwable t) {
                Log.e(LOG_TAG, t.getMessage());

                List<Scene> my_devices = new LinkedList<Scene>();

                for (int index = 0; index < 10; index++) {
                    my_devices.add(Scene.generate());
                }
                ScenesRecycleViewAdapter adapter = new ScenesRecycleViewAdapter(my_devices, scenesActivity);
                m_recyRecyclerView.setAdapter(adapter);
            }

            @Override
            public void OnFailure(String error) {
                Log.e(LOG_TAG, error);
            }
        });
    }

}
