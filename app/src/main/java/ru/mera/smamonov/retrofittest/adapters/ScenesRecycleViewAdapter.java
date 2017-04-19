package ru.mera.smamonov.retrofittest.adapters;

import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ru.mera.smamonov.retrofittest.R;
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
    private Context m_parent_context = null;

    private interface ModifyDeviceListListener {
        void onModify();
    }

    void updateSceneList() {
        notifyDataSetChanged();
    }

    void getAndUpdateScenesList() {
        AppContext.getIotManager().getScenes(new IotManager.GetListListener<Scene>() {
            @Override
            public void OnSuccess(List<Scene> devices) {
                Log.e(LOG_TAG, "Obtaining list of scenes");
                m_scenes.clear();
                m_scenes.addAll(devices);
                notifyDataSetChanged();
            }

            @Override
            public void OnFailure(Throwable t) {
                Log.e(LOG_TAG, "Unable to obtain list of scenes, reason:" + t.getMessage());
            }

            @Override
            public void OnFailure(String error) {
                Log.e(LOG_TAG, "Unable to obtain list of scenes, reason:" + error);
            }
        });
    }

    public void createScene() {
        Toast.makeText(m_parent_context,
                "Add scene ...",
                Toast.LENGTH_SHORT).show();

        final Scene scene_to_be_created = new Scene();
        scene_to_be_created.setName("New scene");

        AlertDialog.Builder builder = new AlertDialog.Builder(m_parent_context);
        /*
        m_parent_context
        LayoutInflater inflater = m_parent_context.getLayoutInflater();
*/
        LayoutInflater inflater = LayoutInflater.from(m_parent_context);

        LinearLayout scene_card_view = (LinearLayout) inflater.inflate(R.layout.scene_layout, null);
        final SceneViewHolder scene_view_holder = new SceneViewHolder(scene_card_view);
        scene_view_holder.m_scene = scene_to_be_created;
        scene_view_holder.setActualView();

        LampsRecycleViewAdapter adapter = new LampsRecycleViewAdapter(scene_view_holder.m_scene.getDevices(),
                m_parent_context,
                new LampsRecycleViewAdapter.SetLampListener() {
                    @Override
                    public void onLampSet(Lamp lamp) {
                        lamp.setSwitched(!lamp.getSwitched());
                        scene_view_holder.setActualView();
                    }
                });

        scene_view_holder.m_recycler_view.setLayoutManager(new LinearLayoutManager(m_parent_context));
        scene_view_holder.m_recycler_view.setAdapter(adapter);
        scene_view_holder.m_menu_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final PopupMenu popup_menu = new PopupMenu(m_parent_context,
                        view);
                MenuInflater inflate = popup_menu.getMenuInflater();
                inflate.inflate(R.menu.create_scene_actvity_popup_menu, popup_menu.getMenu());

                popup_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_scene_rename: {

                                Toast.makeText(m_parent_context,
                                        "Renaming scene ...",
                                        Toast.LENGTH_SHORT).show();

                                scene_view_holder.showRenameDialog();

                                return true;
                            }
                            case R.id.menu_scene_create: {

                                Toast.makeText(m_parent_context,
                                        "Creating scene ...",
                                        Toast.LENGTH_SHORT).show();

                                createScene(scene_view_holder.m_scene);

                                return true;
                            }
                            case R.id.menu_scene_create_activate: {

                                Toast.makeText(m_parent_context,
                                        "Creating and activating scene ...",
                                        Toast.LENGTH_SHORT).show();

                                scene_view_holder.m_scene.setActivated(true);
                                scene_view_holder.setActualView();

                                createScene(scene_view_holder.m_scene);

                                return true;
                            }
                            case R.id.menu_scene_add_remove_elements: {
                                modifyListDevices(scene_view_holder.m_scene,
                                        new ModifyDeviceListListener() {
                                            @Override
                                            public void onModify() {
                                                scene_view_holder.setActualView();
                                            }
                                        });
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
                        Toast.makeText(m_parent_context,
                                "onDismiss",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                popup_menu.show();
            }
        });

        builder.setView(scene_card_view)
                .setTitle(R.string.create_scene_caption)
                .setPositiveButton(R.string.create_scene_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                createScene(scene_view_holder.m_scene);
                            }
                        })
                .setNegativeButton(R.string.create_scene_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        builder.create();
        builder.show();
    }

    public void modifyListDevices(final Scene scene,
                                  final ModifyDeviceListListener listener) {
        Toast.makeText(m_parent_context,
                "Add/remove elements in scene " + scene.getUuid(),
                Toast.LENGTH_SHORT).show();

        AppContext.getIotManager().getLamps(new IotManager.GetListListener<Lamp>() {
            @Override
            public void OnSuccess(final List<Lamp> lamps) {

                List<String> device_names = new LinkedList();
                boolean[] device_used_flags = new boolean[lamps.size()];
                int device_index = 0;

                for (GenericDevice device : lamps) {
                    device_names.add(device.getName());
                    device_used_flags[device_index] = scene.getDevices().contains(device);
                    device_index++;
                }

                final CharSequence[] names_array = device_names.toArray(new CharSequence[device_names.size()]);
                final List<Lamp> copy_of_devices_list = new LinkedList<Lamp>(scene.getDevices());

                AlertDialog.Builder builder = new AlertDialog.Builder(m_parent_context);
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
                        scene.getDevices().clear();
                        scene.getDevices().addAll(copy_of_devices_list);

                        Log.d(LOG_TAG, "Scene updated:" + scene.getUuid());
                        for (Lamp lamp : scene.getDevices()) {
                            Log.d(LOG_TAG, "Scene updated:" + scene.getName() + " lamp:" + lamp.getName());
                        }
                        updateSceneList();
                        if (listener != null) {
                            listener.onModify();
                        }
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

        Toast.makeText(m_parent_context,
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

        Toast.makeText(m_parent_context,
                "Activating scene " + scene.getUuid(),
                Toast.LENGTH_SHORT).show();

        scene.setActivated(true);

        saveScene(scene);
    }

    void createScene(final Scene scene) {
        AppContext.getIotManager().createScene(scene,
                new IotManager.CreateListener<Scene>() {
                    @Override
                    public void OnSuccess(Scene device) {
                        getAndUpdateScenesList();
                    }

                    @Override
                    public void OnFailure(Throwable t) {
                        Log.e(LOG_TAG, "Unable to create scene, reason:" + t.getMessage());
                    }

                    @Override
                    public void OnFailure(String error) {
                        Log.e(LOG_TAG, "Unable to create scene, reason:" + error);
                    }
                });
    }

    void deleteScene(final Scene scene) {
        Toast.makeText(m_parent_context,
                "Deleting scene " + scene.getUuid() + " ...",
                Toast.LENGTH_SHORT).show();

        AppContext.getIotManager().deleteScene(scene,
                new IotManager.DeleteListener<Scene>() {
                    @Override
                    public void OnSuccess(Scene device) {
                        Toast.makeText(m_parent_context,
                                "Scene " + scene.getUuid() + " was successfully removed",
                                Toast.LENGTH_SHORT).show();
                        m_scenes.remove(scene);
                        updateSceneList();
                    }

                    @Override
                    public void OnFailure(Throwable t) {
                        Toast.makeText(m_parent_context,
                                "Unable to delete Scene " + scene.getUuid() + ", reason: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        //TODO: remove this in release
                        m_scenes.remove(scene);
                        updateSceneList();
                    }

                    @Override
                    public void OnFailure(String error) {
                        Toast.makeText(m_parent_context,
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

        void showRenameDialog() {
            final EditText input = new EditText(m_parent_context);
            input.setText(m_scene.getName());

            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(layout);
            AlertDialog.Builder dialog_builder = new AlertDialog.Builder(m_parent_context);
            dialog_builder.setView(input)
                    .setTitle(R.string.rename_scene_caption)
                    .setPositiveButton(R.string.rename_scene_ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    m_scene.setName(input.getText().toString());
                                    setActualView();
                                }
                            })
                    .setNegativeButton(R.string.rename_scene_cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
            dialog_builder.create();
            dialog_builder.show();
        }

        void setActualView() {
            m_scene_name.setText(m_scene.getName());
            m_scene_uuid.setText(m_scene.getUuid());
            m_switch.setChecked(m_scene.getActivated());

            final RecyclerView.Adapter adapter = m_recycler_view.getAdapter();

            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public ScenesRecycleViewAdapter(List<Scene> scenes,
                                    final Context parent_context) {
        this.m_scenes = scenes;
        this.m_parent_context = parent_context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final SceneViewHolder sceneViewHolder = (SceneViewHolder) viewHolder;
        final Scene scene = m_scenes.get(position);

        sceneViewHolder.m_scene = scene;
        sceneViewHolder.setActualView();

        LampsRecycleViewAdapter adapter = new LampsRecycleViewAdapter(scene.getDevices(),
                m_parent_context,
                new LampsRecycleViewAdapter.SetLampListener() {
                    @Override
                    public void onLampSet(Lamp lamp) {
                        lamp.setSwitched(!lamp.getSwitched());
                        updateSceneList();
                    }
                });

        sceneViewHolder.m_recycler_view.setLayoutManager(new LinearLayoutManager(m_parent_context));
        sceneViewHolder.m_recycler_view.setAdapter(adapter);

        sceneViewHolder.m_menu_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final PopupMenu popup_menu = new PopupMenu(m_parent_context,
                        view);
                MenuInflater inflate = popup_menu.getMenuInflater();
                inflate.inflate(R.menu.scenes_activity_popup_menu, popup_menu.getMenu());

                popup_menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.menu_scene_rename: {
                                sceneViewHolder.showRenameDialog();
                                return true;
                            }
                            case R.id.menu_scene_create: {
                                AlertDialog.Builder builder = new AlertDialog.Builder(m_parent_context);
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
                                modifyListDevices(sceneViewHolder.m_scene, null);

                                return true;
                            }
                            case R.id.menu_scene_delete: {
                                AlertDialog.Builder builder = new AlertDialog.Builder(m_parent_context);
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
                                                        Toast.makeText(m_parent_context,
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
                        Toast.makeText(m_parent_context,
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
