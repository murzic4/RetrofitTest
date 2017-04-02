package ru.mera.smamonov.retrofittest.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.mera.smamonov.retrofittest.Activities.MainActivity;
import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;


public class RecycleViewAdapterLamp extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Lamp> m_lamps = null;
    //    private LampController m_lampController = null;
    private MainActivity m_parent_activity = null;

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
    }

    public RecycleViewAdapterLamp(List<Lamp> lamps, final MainActivity parent_activity) {
        this.m_lamps = lamps;
        this.m_parent_activity = parent_activity;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final LampViewHolder lampViewHolder = (LampViewHolder) viewHolder;
        final Lamp lamp = m_lamps.get(position);

        lampViewHolder.m_lamp = lamp;
        lampViewHolder.m_lamp_name.setText(lamp.getName());
        lampViewHolder.m_lamp_uuid.setText(lamp.getUuid());
        lampViewHolder.setActualPicture();

        lampViewHolder.
                itemView.
                setOnClickListener(new View.OnClickListener() {
                                       final Lamp m_lamp = lamp;
                                       final LampViewHolder m_lampViewHolder = lampViewHolder;

                                       @Override
                                       public void onClick(View v) {
                                           //Lamp lamp_to_be_updated = lamp;
                                           Log.d("===>", "lamp state is " + lampViewHolder.m_lamp.getSwitched());
                                           lampViewHolder.m_lamp.setSwitched(!lampViewHolder.m_lamp.getSwitched());
                                           Log.d("===>", "lamp state change_to " + lampViewHolder.m_lamp.getSwitched());
                                           //lamp.setSwitched(!lamp.getSwitched());
                                           m_parent_activity.updateLamp(lampViewHolder.m_lamp, new MainActivity.UpdateDeviceListener() {
                                               @Override
                                               public void onSuccess() {
                                                   lampViewHolder.setActualPicture();
                                               }

                                               @Override
                                               public void onFailure() {
                                               }
                                           });
                                       }

                                   }
                );
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lamp, parent, false);
        LampViewHolder lampViewHolder = new LampViewHolder(v);
        return lampViewHolder;
    }

    @Override
    public int getItemCount() {
        return this.m_lamps.size();
    }
}
