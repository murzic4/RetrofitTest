package ru.mera.smamonov.retrofittest.fragments;

import android.content.Context;
import android.net.Uri;
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

import java.util.List;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.adapters.LampsRecycleViewAdapter;
import ru.mera.smamonov.retrofittest.context.AppContext;
import ru.mera.smamonov.retrofittest.controller.IotManager;
import ru.mera.smamonov.retrofittest.model.Lamp;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LampsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LampsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LampsFragment extends Fragment {

    private static final String LOG_TAG = "LampsFragment";

    private RecyclerView m_lamps_view = null;
    private OnFragmentInteractionListener m_listener = null;

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
                Log.e(LOG_TAG, t.getMessage());
            }

            @Override
            public void OnFailure(String error) {
                Log.e(LOG_TAG, error);
            }
        });
        return rootView;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
