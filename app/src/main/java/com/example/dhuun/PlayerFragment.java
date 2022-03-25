package com.example.dhuun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jean.jcplayer.JcPlayerManager;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;

import java.util.ArrayList;
import java.util.List;

public class PlayerFragment extends Fragment {

    ImageView imageButton;
    String Name, Url;
    PlayerFragment fragment;
    Activity context;
    JcPlayerManager jcPlayerManager;


    int position;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public PlayerFragment() {
    }

    public PlayerFragment(String Name, String Url,int positionn) {
        this.Name = Name;
        this.Url = Url;
        this.position = positionn;



    }


    public static PlayerFragment newInstance(String param1, String param2) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_player, container, false);
        context = getActivity();


        jcPlayerView = view.findViewById(R.id.jcplayer);
        imageButton = (ImageView) view.findViewById(R.id.goBackToList);
        jcPlayerView.createNotification(R.drawable.logo); // Your icon resource


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

                //Toast.makeText(getContext(), "back pressed", Toast.LENGTH_SHORT).show();
            }
        });
        loadSongsList();

        return view;
    }


    void loadSongsList() {
        jcAudios.clear();
        jcAudios = ((MainActivity)getActivity()).jcAudios;
        try {
            jcPlayerView.initPlaylist(jcAudios, null);

            jcPlayerView.playAudio(jcAudios.get(position));
        }catch(Exception ex){

        }
        Toast.makeText(getContext(),""+ position, Toast.LENGTH_SHORT).show();


    }



    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

}