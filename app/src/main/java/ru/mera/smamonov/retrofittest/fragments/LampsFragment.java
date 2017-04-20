package ru.mera.smamonov.retrofittest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.adapters.LampsRecycleViewAdapter;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.controller.IotManager;
import ru.mera.smamonov.retrofittest.model.Lamp;

public class LampsFragment extends Fragment {

    private static final String LOG_TAG = "LampsFragment";

    private RecyclerView m_lamps_view = null;

    public LampsFragment() {
        // Required empty public constructor
    }


    public static LampsFragment newInstance() {
        LampsFragment fragment = new LampsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lamps_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Lamps");

        m_lamps_view = (RecyclerView) rootView.findViewById(R.id.recycle_view_lamps);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        m_lamps_view.setLayoutManager(linearLayoutManager);

        AppContext.getIotManager().getLamps(new IotManager.GetListListener<Lamp>() {

            @Override
            public void OnSuccess(List<Lamp> lamps) {
                LampsRecycleViewAdapter adapter = new LampsRecycleViewAdapter(lamps,
                        getContext(),
                        null);
                m_lamps_view.setAdapter(adapter);
            }

            @Override
            public void OnFailure(Throwable t) {
                Log.e(LOG_TAG, "Unable to get lamps list, reason:" + t.getMessage());

                Toast toast = Toast.makeText(getActivity(),
                        "Unable to get lamps list, reason:" + t.getMessage(),
                        Toast.LENGTH_SHORT);
                TextView text_view = (TextView) toast.getView().findViewById(android.R.id.message);
                text_view.setTextColor(Color.RED);
                toast.show();
            }

            @Override
            public void OnFailure(String error) {
                Log.e(LOG_TAG, "Unable to get lamps list, reason:" + error);

                Toast toast = Toast.makeText(getActivity(),
                        "Unable to get lamps list, reason:" + error,
                        Toast.LENGTH_SHORT);
                TextView text_view = (TextView) toast.getView().findViewById(android.R.id.message);
                text_view.setTextColor(Color.RED);
                toast.show();
            }
        });
        return rootView;
    }
}
