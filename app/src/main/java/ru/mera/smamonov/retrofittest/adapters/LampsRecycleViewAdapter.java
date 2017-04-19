package ru.mera.smamonov.retrofittest.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.controller.IotManager;
import ru.mera.smamonov.retrofittest.model.Lamp;


public class LampsRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface SetLampListener

    {
        void onLampSet(final Lamp lamp);
    }

    static private String LOG_TAG = "LampsAdapter";

    void updateLampList() {
        notifyDataSetChanged();
    }

    void switchLamp(final Lamp lamp) {
        lamp.setSwitched(!lamp.getSwitched());
        saveLamp(lamp);
    }

    void saveLamp(final Lamp lamp) {

        Log.d(LOG_TAG, "Saving lamp " + lamp.getUuid() + " ...");

        Toast.makeText(m_parent_context,
                "Saving lamp " + lamp.getUuid(),
                Toast.LENGTH_SHORT).show();

        AppContext.getIotManager().setLamp(lamp,
                new IotManager.SetListener<Lamp>() {
                    @Override
                    public void OnSuccess(Lamp device) {
                        Log.e(LOG_TAG, "Lamp saved " + lamp.getUuid());
                        updateLampList();
                    }

                    @Override
                    public void OnFailure(Throwable t) {
                        Log.e(LOG_TAG,
                                "Unable to set: " + lamp.getUuid() + " reason:" + t.getMessage());
                        //TODO: remove this:
                        updateLampList();
                    }

                    @Override
                    public void OnFailure(String error) {
                        Log.e(LOG_TAG,
                                "Unable to set: " + lamp.getUuid() + " reason:" + error);
                        //TODO: remove this:
                        updateLampList();
                    }
                });
    }

    private List<Lamp> m_lamps = null;
    //private AppCompatActivity m_parent_activity = null;
    private Context m_parent_context = null;
    private SetLampListener m_lamp_set_listener = null;

    public class LampViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView m_card_view = null;
        TextView m_lamp_name = null;
        TextView m_lamp_uuid = null;
        ImageView m_lamp_picture = null;
        Lamp m_lamp = null;

        LampViewHolder(View itemView) {
            super(itemView);
            m_card_view = (CardView) itemView.findViewById(R.id.lamp_card_view);
            m_lamp_name = (TextView) itemView.findViewById(R.id.lamp_name);
            m_lamp_uuid = (TextView) itemView.findViewById(R.id.lamp_uuid);
            m_lamp_picture = (ImageView) itemView.findViewById(R.id.lamp_picture);
        }

        void RevertLamp() {
            m_lamp.setSwitched(!m_lamp.getSwitched());
            setActualPicture();
        }

        void setActualPicture() {
            if (m_lamp.getSwitched()) {
                m_lamp_picture.setImageResource(R.drawable.ic_lamp_on);
            } else {
                m_lamp_picture.setImageResource(R.drawable.ic_lamp_off);
            }
        }

        void setActualView() {
            m_lamp_name.setText(m_lamp.getName());
            m_lamp_uuid.setText(m_lamp.getUuid());
            setActualPicture();
        }
    }

    public LampsRecycleViewAdapter(List<Lamp> lamps,
                                   /*final AppCompatActivity parent_activity,*/
                                   final Context context,
                                   final SetLampListener set_lamp_listener) {
        this.m_lamps = lamps;
        //this.m_parent_activity = parent_activity;
        this.m_parent_context = context;
        this.m_lamp_set_listener = set_lamp_listener;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {
        final LampViewHolder lampViewHolder = (LampViewHolder) viewHolder;
        final Lamp lamp = m_lamps.get(position);

        lampViewHolder.m_lamp = lamp;

        lampViewHolder.setActualView();

        lampViewHolder.
                itemView.
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (m_lamp_set_listener == null) {
                            Log.d(LOG_TAG,
                                    "Executing default click listener for Lamp:" + lamp.getUuid());
                            switchLamp(lampViewHolder.m_lamp);
                        } else {
                            Log.d(LOG_TAG,
                                    "Executing custom click listener for Lamp:" + lamp.getUuid());
                            m_lamp_set_listener.onLampSet(lamp);
                        }
                    }
                });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lamp_layout, parent, false);
        LampViewHolder lampViewHolder = new LampViewHolder(v);
        return lampViewHolder;
    }

    @Override
    public int getItemCount() {
        return this.m_lamps.size();
    }
}
