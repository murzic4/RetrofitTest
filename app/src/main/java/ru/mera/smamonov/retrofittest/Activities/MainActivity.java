package ru.mera.smamonov.retrofittest.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import ru.mera.smamonov.retrofittest.Adapters.RecycleViewAdapterLamp;
import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;
import ru.mera.smamonov.retrofittest.controller.LampController;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    RecyclerView m_recyRecyclerView = null;
    LampController m_lampController = null;

    public interface UpdateDeviceListener {
        void onSuccess();

        void onFailure();
    }

    public void updateLamp(final Lamp lamp, final UpdateDeviceListener updateDeviceListener) {
        m_lampController.setLamp(lamp, new LampController.UpdateListener() {
            final Lamp m_lamp = lamp;

            final UpdateDeviceListener m_updateDeviceListener = updateDeviceListener;

            @Override
            public void OnSuccess(Lamp lamp, String error) {
                Log.d(LOG_TAG, "Lamp " + m_lamp.getUuid() + "was successfully updated");
                updateDeviceListener.onSuccess();
            }

            @Override
            public void OnFailure(Throwable t) {
                Log.e(LOG_TAG, "Unable to update Lamp " +
                        m_lamp.getUuid() +
                        ", reason: " +
                        t.getMessage());
                updateDeviceListener.onFailure();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_recyRecyclerView = (RecyclerView) findViewById(R.id.RecycleViewMainActivity);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getBaseContext());
        m_recyRecyclerView.setLayoutManager(linearLayoutManager);

        if (m_lampController == null) {
            m_lampController = new LampController();
        }

        final MainActivity mainActivity = this;

        m_lampController.getLamps(new LampController.GetLampsListener() {

            @Override
            public void OnSuccess(List<Lamp> lamps, String error) {
                RecycleViewAdapterLamp adapter = new RecycleViewAdapterLamp(lamps, mainActivity);
                m_recyRecyclerView.setAdapter(adapter);
            }

            @Override
            public void OnFailure(Throwable t) {
                Log.e(LOG_TAG, t.getMessage());
            }
        });
    }
}
