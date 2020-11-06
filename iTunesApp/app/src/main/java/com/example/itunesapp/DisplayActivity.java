package com.example.itunesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisplayActivity extends AppCompatActivity {

    ImageView img;
    TextView trackName;
    TextView genreName;
    Button fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        img = findViewById(R.id.imageView);
        trackName = findViewById(R.id.trackName);
        genreName = findViewById(R.id.genreName);
        fin = findViewById(R.id.finish);


        Songs song = (Songs) getIntent().getExtras().getSerializable(SongsAdapter.DATA_KEY);

        Picasso.get().load(song.url).into(img);
        trackName.setText(song.trackName);
        genreName.setText(song.genre);

        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });







        //Log.d("demo","Data: "+song.url+"  "+song.trackName);

    }
}
