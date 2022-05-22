package com.shcollege.bfitadmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.PlayerView;

public class VideoFragment2 extends Fragment {

    String video_uri;
    PlayerView playerView;
    ExoPlayer simpleExoPlayer;
    Button play;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_video2, container, false);

        playerView = v.findViewById(R.id.video_view_view_diet);
        simpleExoPlayer = new ExoPlayer.Builder(getContext()).build();

        play = v.findViewById(R.id.btn_view_diet_play);

        DietDetails dietDetails = (DietDetails) getActivity();
        video_uri = dietDetails.getS_video_uri();

        if(video_uri!=null)
        {

            playerView.setPlayer(simpleExoPlayer);
            MediaItem mediaItem = MediaItem.fromUri(video_uri);
            simpleExoPlayer.addMediaItem(mediaItem);
            simpleExoPlayer.prepare();
            simpleExoPlayer.setPlayWhenReady(true);

        }
        else
        {
            Toast.makeText(getContext(), "Video not available", Toast.LENGTH_SHORT).show();
        }



        return v;
    }
}