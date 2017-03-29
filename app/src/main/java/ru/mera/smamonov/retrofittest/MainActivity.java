package ru.mera.smamonov.retrofittest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import ru.mera.smamonov.retrofittest.HgInterface.HgInterface;
import ru.mera.smamonov.retrofittest.com.tilgin.model.Lamp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HgInterface gitHubService = HgInterface.retrofit.create(HgInterface.class);
        Call<List<Lamp>> call = gitHubService.getLamps();
        try {
            List<Lamp> result = call.execute().body();
        }
        catch (IOException exception)
        {

        }
    }
}
