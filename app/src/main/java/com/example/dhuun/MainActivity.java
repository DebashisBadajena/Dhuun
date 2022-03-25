package com.example.dhuun;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    SongAdapter songAdapter;
    ImageView button;
    SearchView searchView;
    ArrayList<SongModel> datalist;
    DatabaseReference databaseReference;
    List<SongModel> backup;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    ArrayList<JcAudio> jcAudiosBacckup = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recView);
        button = (ImageView) findViewById(R.id.playerlogo);
        button.setVisibility(View.INVISIBLE);
        searchView = (SearchView) findViewById(R.id.searchview);



        backup = new ArrayList<>();
        datalist = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);


        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    SongModel data = ds.getValue(SongModel.class);
                    datalist.add(data);
                    backup.add(data);
                    jcAudios.add(JcAudio.createFromURL(data.getName(), data.getUrl()));
                    jcAudiosBacckup.add(JcAudio.createFromURL(data.getName(), data.getUrl()));

                }
                songAdapter = new SongAdapter(datalist);
                recyclerView.setAdapter(songAdapter);
button.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MainActivity.this.getFilter().filter(newText);


                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    getSupportFragmentManager().beginTransaction().replace(R.id.maincontainer,PlayerFragment.class,null)
                            //.setReorderingAllowed(true)
                            .addToBackStack(null).commit();
                }catch(Exception exception){
                }
            }
        });

    }

    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<SongModel> filteredData = new ArrayList<>();
            if (keyword.toString().isEmpty()) {
                filteredData.addAll(backup);
                jcAudios.addAll(jcAudiosBacckup);

            } else
                { jcAudios.clear();
                for (SongModel obj : backup) {
                    if (obj.getName().toString().toLowerCase()
                            .contains(keyword.toString().toLowerCase())){
                        filteredData.add(obj);
                    jcAudios.add(JcAudio.createFromURL(obj.getName(), obj.getUrl()));}
                }

            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredData;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {


            datalist.clear();
            datalist.addAll((ArrayList<SongModel>) results.values);
            songAdapter.notifyDataSetChanged();
        }
    };

}