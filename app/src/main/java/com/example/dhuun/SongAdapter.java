package com.example.dhuun;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.jcplayer.model.JcAudio;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter {

    List<SongModel> songModels;                     //array data from firebase by main activity
    List<SongModel> backup;                         //backup of original data for compairision
    int pos;

    public SongAdapter(List<SongModel> songModels) {

        this.songModels = songModels;
        this.backup = new ArrayList<>(songModels);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_row, parent, false);
        ViewHolderClass viewHolderClass = new ViewHolderClass(view);

        return viewHolderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ViewHolderClass viewHolderClass = (ViewHolderClass) holder;
        SongModel songModel = songModels.get(position);
        viewHolderClass.name.setText(songModel.getName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction()
                        .add(R.id.maincontainer, new PlayerFragment(songModel.getName(), songModel.getUrl(),position))
                        .addToBackStack(null).commit();

                // Toast.makeText(view.getContext(), " " + position, Toast.LENGTH_SHORT).show();

            }
        });


    }


    @Override
    public int getItemCount() {
        return songModels.size();
    }


    public class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView name;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.songText);
        }
    }

}
