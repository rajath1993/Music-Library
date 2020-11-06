package com.example.itunesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    EditText search_t;
    TextView limit;
    SeekBar seekBar;
    Button search_btn;
    Button reset_btn;
    Switch switch1;
    int state;
    ArrayList<Songs> songs_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("iTunes App");

        search_t = findViewById(R.id.search_t);
        seekBar = findViewById(R.id.seekBar);
        search_btn = findViewById(R.id.search_btn);
        reset_btn = findViewById(R.id.reset_btn);
        switch1 = findViewById(R.id.switch1);
        recyclerView = findViewById(R.id.recyclerView);
        limit = findViewById(R.id.txt);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);


        seekBar.setMax(25);
        seekBar.incrementProgressBy(1);

        switch1.setChecked(true);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    state = 0;
                    if(songs_list.size()!=0){
                        Collections.sort(songs_list,new compareDate());
                        Log.d("demo","sorted by date: "+songs_list);
                        mAdapter = new SongsAdapter(songs_list);
                        recyclerView.setAdapter(mAdapter);

                    }
                    Toast.makeText(MainActivity.this,"is checked",Toast.LENGTH_SHORT).show();
                }
                else if(b==false){
                    state = 1;
                    if(songs_list.size()!=0){
                        Collections.sort(songs_list,new compareDateDesc());
                        mAdapter = new SongsAdapter(songs_list);
                        recyclerView.setAdapter(mAdapter);
                    }
                    Toast.makeText(MainActivity.this,"is not checked",Toast.LENGTH_SHORT).show();

                }
            }
        });



        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                limit.setText(i+" ");


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //rv = findViewById(R.id.recyclerView);

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = search_t.getText().toString();
                String[] strs = str.split(" ");
                StringBuffer sb = new StringBuffer();

                for(int i=0;i<strs.length;i++){
                    sb.append(strs[i]);
                    sb.append("+");
                }
                sb.deleteCharAt(sb.length()-1);
                Log.d("demo","String buffer: "+sb);

                int limit = seekBar.getProgress();

                Boolean check = switch1.isChecked();

                if(isConnected()){
                    songs_list.clear();
                    new getDataAsync().execute("https://itunes.apple.com/search?term="+sb+"&limit="+limit);

                }
                else {
                    Toast.makeText(MainActivity.this,"No connection",Toast.LENGTH_SHORT).show();
                }

                //Log.d("demo","Selected: "+str+"   "+s1+"   "+s2+"   "+limit+"    "+check);
            }
        });

        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seekBar.setProgress(0);

            }
        });
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }

    private class getDataAsync extends AsyncTask<String, Void, ArrayList<Songs>> {

        @Override
        protected ArrayList<Songs> doInBackground(String... strings) {
            ArrayList<Date> recorded_dates = new ArrayList<>();
            URL url = null;
            HttpURLConnection connection = null;
            try {
                url = new URL(strings[0]);
                Log.d("demo","url: "+url);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){

                    String json = IOUtils.toString(connection.getInputStream(), "UTF-8");
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.d("demo","length: "+results.length());

                    for(int i=0;i<results.length();i++){
                        Songs songs = new Songs();
                        JSONObject jobj =results.getJSONObject(i);
                        songs.trackName = jobj.getString("trackName");
                        songs.artistName= jobj.getString("artistName");
                        songs.price_t = jobj.getString("trackPrice");
                        songs.genre = jobj.getString("primaryGenreName");
                        songs.album = jobj.getString("collectionName");
                        songs.price_a = jobj.getString("collectionPrice");
                        songs.url = jobj.getString("artworkUrl30");



                        String str = jobj.getString("releaseDate");
                        Log.d("demo",str);

                        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
                        LocalDate date = LocalDate.parse(str, formatter);

                        Date dates = date.toDate();

                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");

                        Log.d("demo","Date: "+dates);


                        recorded_dates.add(dates);

                        String newD = sdf.format(dates);

                        songs.date = newD;

                        Log.d("demo","Data found: "+songs.toString());

                        songs_list.add(songs);
                    }

//                    Collections.sort(recorded_dates);
//                    for(int j=0;j<recorded_dates.size();j++){
//                        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
//                        String newD = sdf.format(recorded_dates.get(j));
//
//                        Songs songs = new Songs(newD);
//                        Log.d("demo","songs: "+songs.toString());
//
//
//                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return songs_list;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<Songs> songs) {
            super.onPostExecute(songs);
//            ArrayList<Date> recorded_dates = new ArrayList<>();
//            for(int i=0;i<songs.size();i++){
//                String dt = songs.get(i).date;
//                SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
//                try {
//                    Date date = formatter.parse(dt);
//                    recorded_dates.add(date);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            Collections.sort(recorded_dates);
            Log.d("demo","data length: "+songs.size());

            if(state == 1){
                Collections.sort(songs,new compareDateDesc());
            }
            else{
                Collections.sort(songs,new compareDate());
            }
            mAdapter = new SongsAdapter(songs);
            recyclerView.setAdapter(mAdapter);
        }
    }

    public class compareDate implements Comparator<Songs> {

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        @Override
        public int compare(Songs o1, Songs o2) {
            try {
                return dateFormat.parse(o1.date).compareTo(dateFormat.parse(o2.date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    public class compareDateDesc implements Comparator<Songs> {

        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        @Override
        public int compare(Songs o1, Songs o2) {
            try {
                return dateFormat.parse(o2.date).compareTo(dateFormat.parse(o1.date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    public class comparePrice implements Comparator<Songs> {

        @Override
        public int compare(Songs o1, Songs o2) {
            return Double.valueOf(o1.price_t).compareTo(Double.valueOf(o2.price_t));
        }
    }


}
