package ru.mera.smamonov.retrofittest.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.activities.ScenesActivity;
import ru.mera.smamonov.retrofittest.com.tilgin.model.GenericDevice;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Scene;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.controller.IotManager;

/**
 * Created by sergeym on 11.04.2017.
 */

public class ScenesRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static private final String LOG_TAG = "ScenesRecycleViewAdapter";
    private List<Scene> m_scenes = null;
    private ScenesActivity m_parent_activity = null;


    public class SceneViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView m_card_view = null;
        TextView m_scene_name = null;
        TextView m_scene_uuid = null;
        RecyclerView m_recycler_view = null;
        Switch m_switch = null;

        Scene m_scene = null;

        SceneViewHolder(View itemView) {
            super(itemView);
            m_card_view = (CardView) itemView.findViewById(R.id.scene_card_view);
            m_scene_name = (TextView) itemView.findViewById(R.id.scene_name);
            m_scene_uuid = (TextView) itemView.findViewById(R.id.scene_uuid);
            m_recycler_view = (RecyclerView) itemView.findViewById(R.id.recycle_view_devices);
            m_switch = (Switch) itemView.findViewById(R.id.scene_activated_switch);
        }

        void setActualView() {
            m_scene_name.setText(m_scene.getName());
            m_scene_uuid.setText(m_scene.getUuid());
            m_switch.setChecked(m_scene.getActivated());
        }
    }

    public ScenesRecycleViewAdapter(List<Scene> scenes,
                                    final ScenesActivity parent_activity) {
        this.m_scenes = scenes;
        this.m_parent_activity = parent_activity;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final SceneViewHolder sceneViewHolder = (SceneViewHolder) viewHolder;
        final Scene scene = m_scenes.get(position);

        sceneViewHolder.m_scene = scene;
        sceneViewHolder.setActualView();

        sceneViewHolder.m_scene.addListener(new GenericDevice.DeviceListener() {
            @Override
            public void onDelete() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void onUpdate() {
                sceneViewHolder.setActualView();
            }
        });

        LampsRecycleViewAdapter adapter = new LampsRecycleViewAdapter(scene.getDevices(),
                m_parent_activity,
                new LampsRecycleViewAdapter.SetLampListener() {
                    @Override
                    public void onLampSet(Lamp lamp) {
                        lamp.Update();
                    }
                });

        sceneViewHolder.m_recycler_view.setLayoutManager(new LinearLayoutManager(m_parent_activity.getBaseContext()));
        sceneViewHolder.m_recycler_view.setAdapter(adapter);

        sceneViewHolder.
                itemView.
                setOnClickListener(new View.OnClickListener() {
                                       final Scene m_scene = scene;
                                       final SceneViewHolder m_sceneViewHolder = sceneViewHolder;

                                       @Override
                                       public void onClick(View v) {

                                           scene.setActivated(!scene.getActivated());

                                           Log.d(LOG_TAG, "Scene click " + sceneViewHolder.m_scene.getUuid());

                                           AppContext.getIotManager().setScene(m_scene,
                                                   new IotManager.SetListener<Scene>() {
                                                       @Override
                                                       public void OnSuccess(Scene device) {
                                                           Log.e(LOG_TAG, "Scene saved " + m_scene.getUuid());
                                                           device.Update();
                                                       }

                                                       @Override
                                                       public void OnFailure(Throwable t) {
                                                           Log.e(LOG_TAG,
                                                                   "Unable to set: " +
                                                                           m_scene.getUuid() +
                                                                           " reason:" +
                                                                           t.getMessage());

                                                           m_scene.Update();
                                                       }

                                                       @Override
                                                       public void OnFailure(String error) {
                                                           Log.e(LOG_TAG,
                                                                   "Unable to set: " +
                                                                           m_scene.getUuid() +
                                                                           " reason:" +
                                                                           error);
                                                           m_scene.Update();
                                                       }
                                                   });
                                       }
                                   }
                );
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scene_layout,
                parent,
                false);
        SceneViewHolder sceneViewHolder = new SceneViewHolder(v);
        return sceneViewHolder;
    }

    @Override
    public int getItemCount() {
        return this.m_scenes.size();
    }
}
