package ru.mera.smamonov.retrofittest.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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

public class LampsFragment extends GenericFragment {

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

        Log.e(LOG_TAG, "onCreate");
    }

    @Override
    public void updateView() {
        Log.e(LOG_TAG, "updateView");

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

                final Context context = getActivity();
                if (context != null) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Unable to get lamps list, reason:" + t.getMessage(),
                            Toast.LENGTH_SHORT);
                    TextView text_view = (TextView) toast.getView().findViewById(android.R.id.message);
                    text_view.setTextColor(Color.RED);
                    toast.show();
                }
            }

            @Override
            public void OnFailure(String error) {
                Log.e(LOG_TAG, "Unable to get lamps list, reason:" + error);

                final Context context = getActivity();
                if (context != null) {
                    Toast toast = Toast.makeText(getActivity(),
                            "Unable to get lamps list, reason:" + error,
                            Toast.LENGTH_SHORT);
                    TextView text_view = (TextView) toast.getView().findViewById(android.R.id.message);
                    text_view.setTextColor(Color.RED);
                    toast.show();
                }
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.lamps_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Lamps");

        m_lamps_view = (RecyclerView) rootView.findViewById(R.id.recycle_view_lamps);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        m_lamps_view.setLayoutManager(linearLayoutManager);

        updateView();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(LOG_TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(LOG_TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(LOG_TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(LOG_TAG, "onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(LOG_TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e(LOG_TAG, "onDetach");
    }
}
