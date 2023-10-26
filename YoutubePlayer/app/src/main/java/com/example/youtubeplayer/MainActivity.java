package com.example.youtubeplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId() ) {
            case R.id.btnPlayVideo:
                intent = YouTubeStandalonePlayer.createVideoIntent(this,YoutubeActivity.GOOGLE_API_KEY,YoutubeActivity.YOUTUBE_VIDEO_ID);
                break;
            case R.id.btnPlayList:
                intent = YouTubeStandalonePlayer.createPlaylistIntent(this,YoutubeActivity.GOOGLE_API_KEY,YoutubeActivity.YOUTUBE_VIDEO_ID);
                break;
        }
        if (intent!= null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  Button btnPlayVideo = (Button) findViewById(R.id.btnPlaySingle);
        Button btnPlayPlaylist = (Button) findViewById(R.id.btnStandAlone);

        btnPlayPlaylist.setOnClickListener(this);
        btnPlayVideo.setOnClickListener(this);*/
    }
}