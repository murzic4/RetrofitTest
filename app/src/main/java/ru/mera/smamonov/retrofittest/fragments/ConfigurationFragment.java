package ru.mera.smamonov.retrofittest.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import ru.mera.smamonov.retrofittest.R;
import ru.mera.smamonov.retrofittest.context.AppContext;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigurationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigurationFragment extends GenericFragment {

    static final String LOG_TAG = "ConfigurationFragment";
    public ConfigurationFragment() {
        // Required empty public constructor
    }

    public static ConfigurationFragment newInstance() {
        ConfigurationFragment fragment = new ConfigurationFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.configuration_fragment, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.title_configuration);

        final SharedPreferences settings = getActivity().getSharedPreferences(getResources().getString(R.string.settings_file_name),
                MODE_PRIVATE);

        final EditText url_text = (EditText) rootView.findViewById(R.id.url_text);
        String default_url = AppContext.getAppContext().getString(R.string.default_url);
        url_text.setText(settings.getString("url", default_url));

        Button button = (Button) rootView.findViewById(R.id.apply_configuration_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.title_update_configuration)
                        .setPositiveButton(R.string.restart,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String url = url_text.getText().toString();
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("url", url);
                                        editor.commit();

                                        AppContext.getIotManager().configureRetrofit(url);

                                        Intent intent = getActivity().getIntent();
                                        getActivity().finish();
                                        startActivity(intent);
                                    }
                                })
                        .setNegativeButton(R.string.restart_cancel,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                    }
                                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return rootView;
    }

    @Override
    public void updateView() {
        Log.e(LOG_TAG, "updateView");
    }
}
