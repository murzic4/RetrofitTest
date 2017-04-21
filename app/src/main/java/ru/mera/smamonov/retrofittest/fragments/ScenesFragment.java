package ru.mera.smamonov.retrofittest.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import ru.mera.smamonov.retrofittest.adapters.ScenesRecycleViewAdapter;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.controller.IotManager;
import ru.mera.smamonov.retrofittest.model.Scene;


public class ScenesFragment extends GenericFragment {

    private static final String LOG_TAG = "ScenesFragment";

    RecyclerView m_recycler_view = null;
    FloatingActionButton m_fab = null;
    boolean m_list_was_requested = false;

    public ScenesFragment() {
        // Required empty public constructor
    }


    public static ScenesFragment newInstance() {
        ScenesFragment fragment = new ScenesFragment();
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
        if (!m_list_was_requested) {
            m_list_was_requested = true;
            AppContext.getIotManager().getScenes(new IotManager.GetListListener<Scene>() {
                @Override
                public void OnSuccess(List<Scene> devices) {
                    ScenesRecycleViewAdapter adapter = new ScenesRecycleViewAdapter(devices, getContext());
                    m_recycler_view.setAdapter(adapter);

                    m_fab.setVisibility(View.VISIBLE);
                    m_list_was_requested = false;
                }

                @Override
                public void OnFailure(Throwable t) {
                    Log.e(LOG_TAG, t.getMessage());

                    final Context context = getActivity();
                    if (context != null) {
                        Toast toast = Toast.makeText(getActivity(),
                                "Unable to get scenes list, reason:" + t.getMessage(),
                                Toast.LENGTH_SHORT);
                        TextView text_view = (TextView) toast.getView().findViewById(android.R.id.message);
                        text_view.setTextColor(Color.RED);
                        toast.show();
                    }

                    m_fab.setVisibility(View.INVISIBLE);
                    m_list_was_requested = false;
                }

                @Override
                public void OnFailure(String error) {
                    Log.e(LOG_TAG, error);

                    final Context context = getActivity();
                    if (context != null) {
                        Toast toast = Toast.makeText(getActivity(),
                                "Unable to get scenes list, reason:" + error,
                                Toast.LENGTH_SHORT);
                        TextView text_view = (TextView) toast.getView().findViewById(android.R.id.message);
                        text_view.setTextColor(Color.RED);
                        toast.show();
                    }

                    m_fab.setVisibility(View.INVISIBLE);
                    m_list_was_requested = false;
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(LOG_TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.scenes_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Scenes");

        m_recycler_view = (RecyclerView) rootView.findViewById(R.id.RecycleViewSceneActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        m_recycler_view.setLayoutManager(linearLayoutManager);

        m_fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        m_fab.setVisibility(View.INVISIBLE);

        m_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScenesRecycleViewAdapter adapter = (ScenesRecycleViewAdapter) m_recycler_view.getAdapter();
                if (adapter != null) {
                    adapter.createScene();
                }
            }
        });

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(LOG_TAG, "onHiddenChanged");
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
