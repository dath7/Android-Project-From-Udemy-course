package com.example.youtubeplayer;



import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class StandaloneActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId() ) {
            case R.id.btnPlayVideo:
                intent = YouTubeStandalonePlayer.createVideoIntent(this,YoutubeActivity.GOOGLE_API_KEY,YoutubeActivity.YOUTUBE_VIDEO_ID,0,true,false);
                break;
            case R.id.btnPlayList:
                intent = YouTubeStandalonePlayer.createPlaylistIntent(this,YoutubeActivity.GOOGLE_API_KEY,YoutubeActivity.YOUTUBE_VIDEO_ID,0,0,true,true);
                break;
        }
        if (intent!= null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standalone);
        Button btnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
        Button btnPlayPlaylist = (Button) findViewById(R.id.btnPlayList);

        btnPlayPlaylist.setOnClickListener(this);
        btnPlayVideo.setOnClickListener(this);
    }
}