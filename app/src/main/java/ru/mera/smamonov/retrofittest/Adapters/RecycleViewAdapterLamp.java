package ru.mera.smamonov.retrofittest.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;


public class RecycleViewAdapterLamp extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<Lamp> m_lamps = null;

    public static class LampViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        CardView m_card_view;
        TextView m_lamp_name;
        TextView m_lamp_uuid;
        ImageView m_lamp_picture;

        LampViewHolder(View itemView) {
            super(itemView);
            m_card_view = (CardView)itemView.findViewById(R.id.lamp_card_view);
            m_lamp_name = (TextView)itemView.findViewById(R.id.lamp_name);
            m_lamp_uuid = (TextView)itemView.findViewById(R.id.lamp_uuid);
            m_lamp_picture = (ImageView)itemView.findViewById(R.id.lamp_picture);
        }
    }

    public RecycleViewAdapterLamp(List<Lamp> lamps) {
        this.m_lamps = lamps;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position)
    {
        LampViewHolder lampViewHolder = (LampViewHolder)viewHolder;
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
