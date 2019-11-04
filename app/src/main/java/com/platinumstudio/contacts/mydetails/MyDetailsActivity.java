package com.platinumstudio.contacts.mydetails;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.platinumstudio.contacts.R;
import com.platinumstudio.contacts.model.Photo;
import com.platinumstudio.contacts.network.GetDataService;
import com.platinumstudio.contacts.network.RetrofitClientInstance;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MyDetailsActivity";

    private TextView quoteTextView;
    Random random = new Random();
    private ImageView myPicture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_details);

        quoteTextView = findViewById(R.id.quote_text_view);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance()
                .create(GetDataService.class);

        Call<List<Photo>> call = service.getAllPhotos();

        call.enqueue(new Callback<List<Photo>>() {

            @Override
            public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {

                if(response.body() != null){

                    List<Photo> photoList = response.body();
                    int randomInteger = random.nextInt(1000);
                    String randomText = photoList.get(randomInteger).getTitle();

                    quoteTextView.setText(randomText);
                }
            }

            @Override
            public void onFailure(Call<List<Photo>> call, Throwable t) {
                Toast.makeText(MyDetailsActivity.this, "Failed to retrieve Random Quote."
                        + "\nPlease check your internet connection!", Toast.LENGTH_SHORT).show();
            }
        });

        myPicture = findViewById(R.id.my_profile_picture);

        Glide.with(myPicture.getContext())
                .load(R.drawable.ic_account_circle_black_24dp)
                .apply(RequestOptions.circleCropTransform())
                .into(myPicture);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            // Respond to the action bar's Home button
            case android.R.id.home:

                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}