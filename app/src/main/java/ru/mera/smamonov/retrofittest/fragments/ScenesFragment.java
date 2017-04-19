package ru.mera.smamonov.retrofittest.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.adapters.ScenesRecycleViewAdapter;
import ru.mera.smamonov.retrofittest.controller.IotManager;
import ru.mera.smamonov.retrofittest.model.Scene;


public class ScenesFragment extends Fragment {

    private static final String LOG_TAG = "ScenesActivity";

    RecyclerView m_recycler_view = null;
    private OnFragmentInteractionListener m_listener = null;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.scenes_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle("Scenes");

        final FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScenesRecycleViewAdapter adapter = (ScenesRecycleViewAdapter) m_recycler_view.getAdapter();
                if (adapter != null) {
                    adapter.createScene();
                }
            }
        });

        IotManager iotManager = new IotManager();
        m_recycler_view = (RecyclerView) rootView.findViewById(R.id.RecycleViewSceneActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        m_recycler_view.setLayoutManager(linearLayoutManager);


        iotManager.getScenes(new IotManager.GetListListener<Scene>() {
            @Override
            public void OnSuccess(List<Scene> devices) {
                ScenesRecycleViewAdapter adapter = new ScenesRecycleViewAdapter(devices, getContext());
                m_recycler_view.setAdapter(adapter);
                fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnFailure(Throwable t) {
                Log.e(LOG_TAG, t.getMessage());

                List<Scene> my_devices = new LinkedList<Scene>();

                for (int index = 0; index < 10; index++) {
                    my_devices.add(Scene.generate());
                }
                ScenesRecycleViewAdapter adapter = new ScenesRecycleViewAdapter(my_devices, getContext());
                m_recycler_view.setAdapter(adapter);
                fab.setVisibility(View.VISIBLE);
            }

            @Override
            public void OnFailure(String error) {
                Log.e(LOG_TAG, error);
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*
        if (m_listener != null) {
            mListener.onFragmentInteraction(uri);
        }
        */
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            m_listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        m_listener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}