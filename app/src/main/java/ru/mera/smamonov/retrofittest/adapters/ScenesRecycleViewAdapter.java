package ru.mera.smamonov.retrofittest.adapters;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.activities.ScenesActivity;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.controller.IotManager;
import ru.mera.smamonov.retrofittest.model.GenericDevice;
import ru.mera.smamonov.retrofittest.model.Lamp;
import ru.mera.smamonov.retrofittest.model.Scene;

/**
 * Created by sergeym on 11.04.2017.
 */

public class ScenesRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static private final String LOG_TAG = "ScenesAdapter";
    private List<Scene> m_scenes = null;
    private ScenesActivity m_parent_activity = null;

    void updateSceneList() {
        notifyDataSetChanged();
    }

    public void createScene() {
        Toast.makeText(m_parent_activity,
                "Add scene ...",
                Toast.LENGTH_SHORT).show();
    }

    public void modifyListDevices(final Scene scene) {
        Toast.makeText(m_parent_activity,
                "Add/remove elemnts in scene " + scene.getUuid(),
                Toast.LENGTH_SHORT).show();

        AppContext.getIotManager().getLamps(new IotManager.GetListListener<Lamp>() {
            @Override
            public void OnSuccess(final List<Lamp> lamps) {

                List<String> device_names = new LinkedList();
                List<Boolean> device_used_in_scene = new LinkedList();
                boolean[] device_used_flags = new boolean[lamps.size()];
                int device_index = 0;

                for (GenericDevice device : lamps) {
                    device_names.add(device.getName());
                    device_used_flags[device_index] = scene.getDevices().contains(device);
                    device_index++;
                }

                final CharSequence[] names_array = device_names.toArray(new CharSequence[device_names.size()]);
                final List<Lamp> copy_of_devices_list = new LinkedList<Lamp>(scene.getDevices());

                AlertDialog.Builder builder = new AlertDialog.Builder(m_parent_activity);
                builder.setTitle("Title")
                        .setMultiChoiceItems(names_array,
                                device_used_flags,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which,
                                                        boolean isChecked) {

                                        Lamp lamp = lamps.get(which);

                                        if (isChecked) {
                                            // If the user checked the item, add it to the selected items
                                            copy_of_devices_list.add(lamp);
                                        } else if (copy_of_devices_list.contains(lamp)) {
                                            // Else, if the item is already in the array, remove it
                                            copy_of_devices_list.remove(lamp);
                                        }
                                    }
                                }).setPositiveButton(R.string.update_scene_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        scene.setDevices(copy_of_devices_list);
                        updateSceneList();
                    }
                }).setNegativeButton(R.string.update_scene_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        updateSceneList();
                    }
                });

                builder.create();
                builder.show();
            }

            @Override
            public void OnFailure(Throwable t) {
                Log.e(LOG_TAG, "Unable to obtain list of devices, reason:" + t.getMessage());

                //TODO: remove this code:
                OnSuccess(Lamp.getList());
            }

            @Override
            public void OnFailure(String error) {
                Log.e(LOG_TAG, "Unable to obtain list of devices, reason:" + error);
            }
        });
    }

    void saveScene(final Scene scene) {

        Log.d(LOG_TAG, "Saving scene " + scene.getUuid() + " ...");

        Toast.makeText(m_parent_activity,
                "Saving scene " + scene.getUuid(),
                Toast.LENGTH_SHORT).show();

        AppContext.getIotManager().setScene(scene,
                new IotManager.SetListener<Scene>() {
                    @Override
                    public void OnSuccess(Scene device) {
                        Log.e(LOG_TAG, "Scene saved " + scene.getUuid());
                        notifyDataSetChanged();
                    }

                    @Override
                    public void OnFailure(Throwable t) {
                        Log.e(LOG_TAG,
                                "Unable to set: " + scene.getUuid() + " reason:" + t.getMessage());
                        notifyDataSetChanged();
                    }

                    @Override
                    public void OnFailure(String error) {
                        Log.e(LOG_TAG,
                                "Unable to set: " + scene.getUuid() + " reason:" + error);
                        notifyDataSetChanged();
                    }
                });
    }

    void activateScene(final Scene scene) {

        Log.d(LOG_TAG, "Activate scene " + scene.getUuid() + " ...");

        Toast.makeText(m_parent_activity,
                "Activating scene " + scene.getUuid(),
                Toast.LENGTH_SHORT).show();

        scene.setActivated(true);

        saveScene(scene);
    }

    void deleteScene(final Scene scene) {
        Toast.makeText(m_parent_activity,
                "Deleting scene " + scene.getUuid() + " ...",
                Toast.LENGTH_SHORT).show();

        AppContext.getIotManager().deleteScene(scene,
                new IotManager.DeleteListener<Scene>() {
                    @Override
                    public void OnSuccess(Scene device) {
                        Toast.makeText(m_parent_activity,
                                "Scene " + scene.getUuid() + " was successfully removed",
                                Toast.LENGTH_SHORT).show();
                        m_scenes.remove(scene);
                        updateSceneList();
                    }

                    @Override
                    public void OnFailure(Throwable t) {
                        Toast.makeText(m_parent_activity,
                                "Unable to delete Scene " + scene.getUuid() + ", reason: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        //TODO: remove this in release
                        m_scenes.remove(scene);
                        updateSceneList();
                    }

                    @Override
                    public void OnFailure(String error) {
                        Toast.makeText(m_parent_activity,
                                "Unable to delete Scene " + scene.getUuid() + ", reason: " + error,
                                Toast.LENGTH_SHORT).show();
                        //TODO: remove this in release
                        m_scenes.remove(scene);
                        updateSceneList();
                    }
                });
    }

    public class SceneViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView m_card_view = null;
        TextView m_scene_name = null;
        TextView m_scene_uuid = null;
        RecyclerView m_recycler_view = null;
        Switch m_switch = null;
        ImageButton m_menu_image = null;

        Scene m_scene = null;

        SceneViewHolder(View itemView) {
            super(itemView);
            m_card_view = (CardView) itemView.findViewById(R.id.scene_card_view);
            m_scene_name = (TextView) itemView.findViewById(R.id.scene_name);
            m_scene_uuid = (TextView) itemView.findViewById(R.id.scene_uuid);
            m_recycler_view = (RecyclerView) itemView.findViewById(R.id.recycle_view_devices);
            m_switch = (Switch) itemView.findViewById(R.id.scene_activated_switch);
            m_menu_image = (ImageButton) itemView.findViewById(R.id.scene_popup_menu);
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

        LampsRecycleViewAdapter adapter = new LampsRecycleViewAdapter(scene.getDevices(),
                m_parent_activity,
                new LampsRecycleViewAdapter.SetLampListener() {
                    @Override
                    public void onLampSet(Lamp lamp) {
                        lamp.setSwitched(!lamp.getSwitched());
                        //sceneViewHolder.setActualView();
                        updateSceneList();
                    }
                });

        sceneViewHolder.m_recycler_view.setLayoutManager(new LinearLayoutManager(m_parent_activity.getBaseContext()));
        sceneViewHolder.m_recycler_view.setAdapter(adapter);

        sceneViewHolder.m_menu_image.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {

                final PopupMenu popup_menu = new PopupMenu(m_parent_activity,
                        view);
                MenuInflater inflate = popup_menu.getMenuInflater();
                inflate.inflate(R.menu.scene_popup_menu, popup_menu.getMenu());

                popup_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.menu_scene_save: {
                                AlertDialog.Builder builder = new AlertDialog.Builder(m_parent_activity);
                                builder.setMessage(R.string.update_scene_caption)
                                        .setPositiveButton(R.string.update_scene_ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        saveScene(sceneViewHolder.m_scene);
                                                    }
                                                })
                                        .setNegativeButton(R.string.update_scene_cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                    }
                                                });

                                AlertDialog alert = builder.create();
                                alert.show();
                                return true;
                            }
                            case R.id.menu_scene_activate: {
                                activateScene(sceneViewHolder.m_scene);
                                return true;
                            }
                            case R.id.menu_scene_add_remove_elements: {
                                modifyListDevices(sceneViewHolder.m_scene);
                                return true;
                            }
                            case R.id.menu_scene_delete: {
                                AlertDialog.Builder builder = new AlertDialog.Builder(m_parent_activity);
                                builder.setMessage(R.string.delete_scene_caption)
                                        .setPositiveButton(R.string.delete_scene_ok,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        deleteScene(sceneViewHolder.m_scene);
                                                    }
                                                })
                                        .setNegativeButton(R.string.delete_scene_cancel,
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        Toast.makeText(m_parent_activity,
                                                                "Deleting scene " + sceneViewHolder.m_scene.getUuid() + " cancelled",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                AlertDialog alert = builder.create();
                                alert.show();

                                return true;
                            }
                            default:
                                return false;

                        }
                    }
                });

                popup_menu.setOnDismissListener(new PopupMenu.OnDismissListener() {

                    @Override
                    public void onDismiss(PopupMenu menu) {
                        Toast.makeText(m_parent_activity,
                                "onDismiss",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                popup_menu.show();
            }
        });

        sceneViewHolder.
                itemView.
                setOnClickListener(new View.OnClickListener() {
                                       final Scene m_scene = scene;
                                       final SceneViewHolder m_sceneViewHolder = sceneViewHolder;

                                       @Override
                                       public void onClick(View v) {
                                           activateScene(m_scene);
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
