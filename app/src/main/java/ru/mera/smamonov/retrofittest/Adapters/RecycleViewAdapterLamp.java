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

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;
import ru.mera.smamonov.retrofittest.controller.LampController;


public class RecycleViewAdapterLamp extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Lamp> m_lamps = null;
    private LampController lampController = null;

    public class LampViewHolder extends RecyclerView.ViewHolder
                                       implements View.OnClickListener
    {
        // each data item is just a string in this case
        CardView m_card_view;
        TextView m_lamp_name;
        TextView m_lamp_uuid;
        ImageView m_lamp_picture;

        Lamp m_lamp = null;

        LampViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            m_card_view = (CardView)itemView.findViewById(R.id.lamp_card_view);
            m_lamp_name = (TextView)itemView.findViewById(R.id.lamp_name);
            m_lamp_uuid = (TextView)itemView.findViewById(R.id.lamp_uuid);
            m_lamp_picture = (ImageView)itemView.findViewById(R.id.lamp_picture);
        }

        public void onClick(View v)
        {
            Log.d("=================", "+================================onClick");
            lampController.revertLamp(m_lamp,
                    new LampController.UpdateListener() {
                        @Override
                        public void OnSuccess(Lamp lamp, String error) {
                            Log.d("MainActivity", "Updating result:" + error);
                        }

                        @Override
                        public void OnFailure(Throwable t) {
                            Log.e("MainActivity", "Updating failure:" + t.getMessage());
                        }
                    });

        }
    }

    public RecycleViewAdapterLamp(List<Lamp> lamps, final LampController lampController)  {
        this.m_lamps = lamps;
        this.lampController = lampController;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        LampViewHolder lampViewHolder = (LampViewHolder)viewHolder;
        lampViewHolder.m_lamp = m_lamps.get(position);
        lampViewHolder.m_lamp_name.setText(m_lamps.get(position).getName());
        lampViewHolder.m_lamp_uuid.setText(m_lamps.get(position).getUuid());
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
