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
    RecyclerView m_lamps_recycler_view = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lamps_fragment);
        m_lamps_recycler_view = (RecyclerView) findViewById(R.id.recycle_view_lamps);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        m_lamps_recycler_view.setLayoutManager(linearLayoutManager);

        AppContext.getIotManager().getLamps(new IotManager.GetListListener<Lamp>() {

            @Override
            public void OnSuccess(List<Lamp> lamps) {
                LampsRecycleViewAdapter adapter = new LampsRecycleViewAdapter(lamps,
                        getBaseContext(),
                        null);
                m_lamps_recycler_view.setAdapter(adapter);
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
