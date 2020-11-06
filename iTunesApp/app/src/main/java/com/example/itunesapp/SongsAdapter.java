package com.example.itunesapp;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.itunesapp.MainActivity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {
    ArrayList<Songs> mData;
    public static String DATA_KEY = "DATA";

    public SongsAdapter(ArrayList<Songs> mData) {
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.use_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongsAdapter.ViewHolder holder, final int position) {
        Songs song = mData.get(position);

        PrettyTime pt = new PrettyTime();

        Date d1 = new Date();

        String sDate = song.date;

        try {
            d1 = new SimpleDateFormat("MM-dd-yyyy").parse(sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        holder.trackName.setText(song.trackName);
        holder.artistName.setText(song.artistName);
        holder.date.setText(pt.format(d1));
        holder.price_t.setText(song.price_t);

        holder.song = song;



        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mData.remove(position);
                notifyDataSetChanged();
                return false;
            }
        });
   }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView trackName,price_t,artistName,date;
        Songs song;

        public ViewHolder(final View itemView){
            super(itemView);

            trackName = itemView.findViewById(R.id.trackName);
            price_t = itemView.findViewById(R.id.artistname);
            artistName = itemView.findViewById(R.id.albumName);
            date = itemView.findViewById(R.id.ddmmyy);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(),DisplayActivity.class);
                    ArrayList<String> add_data = new ArrayList<>();
                    intent.putExtra(DATA_KEY,song);



                    //intent.putExtra(DATA_KEY,add_data);
                    itemView.getContext().startActivity(intent);




                }

            });

        }

    }
}
