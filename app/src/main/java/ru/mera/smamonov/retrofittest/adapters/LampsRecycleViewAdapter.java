package ru.mera.smamonov.retrofittest.adapters;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.model.GenericDevice;
import ru.mera.smamonov.retrofittest.model.Lamp;


public class LampsRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static private String LOG_TAG = "LampsAdapter";

    public interface SetLampListener {
        void onLampSet(final Lamp lamp);
    }

    private List<Lamp> m_lamps = null;
    private AppCompatActivity m_parent_activity = null;
    private SetLampListener m_set_lamp_listener = null;

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
                                   final AppCompatActivity parent_activity,
                                   final SetLampListener set_lamp_listener) {
        this.m_lamps = lamps;
        this.m_parent_activity = parent_activity;

        if (set_lamp_listener != null) {
            m_set_lamp_listener = set_lamp_listener;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,
                                 int position) {
        final LampViewHolder lampViewHolder = (LampViewHolder) viewHolder;
        final Lamp lamp = m_lamps.get(position);

        lampViewHolder.m_lamp = lamp;

        /*
        lampViewHolder.m_lamp_name.setText(lamp.getName());
        lampViewHolder.m_lamp_uuid.setText(lamp.getUuid());
        lampViewHolder.setActualPicture();
        */
        lampViewHolder.setActualView();

        lampViewHolder.m_lamp.addListener(lampViewHolder, new GenericDevice.DeviceListener() {
            @Override
            public void onDelete() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void onUpdate() {
                lampViewHolder.setActualView();
            }
        });

        lampViewHolder.
                itemView.
                setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           if (m_set_lamp_listener != null) {
                                               lampViewHolder.m_lamp.setSwitched(!lampViewHolder.m_lamp.getSwitched());
                                               m_set_lamp_listener.onLampSet(lampViewHolder.m_lamp);
                                           }
                                           /*
                                           //Lamp lamp_to_be_updated = lamp_layout;
                                           Log.d("===>", "lamp_layout state is " + lampViewHolder.m_lamp.getSwitched());
                                           lampViewHolder.m_lamp.setSwitched(!lampViewHolder.m_lamp.getSwitched());
                                           Log.d("===>", "lamp_layout state change_to " + lampViewHolder.m_lamp.getSwitched());
                                           //lamp_layout.setSwitched(!lamp_layout.getSwitched());
                                           AppContext.getIotManager().setLamp(lampViewHolder.m_lamp,
                                                   new IotManager.SetListener<Lamp>() {
                                                       @Override
                                                       public void OnFailure(Throwable t) {

                                                       }

                                                       @Override
                                                       public void OnFailure(String error) {

                                                       }

                                                       @Override
                                                       public void OnSuccess(Lamp lamp) {
                                                           lampViewHolder.setActualPicture();
                                                       }
                                                   }
                                           );
                                           */
                                       }
                                   }
                );
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
