package si.uni_lj.fe.tnuv.groupsound2_1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PlaylistActivity extends AppCompatActivity{

    private String playlistName;
    // Other member variables and methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        // Get the playlist name from the intent
        playlistName = getIntent().getStringExtra("playlistName");

        // Load the playlist details
        loadPlaylistDetails();
    }

    private void loadPlaylistDetails() {
        // Retrieve the songs for the selected playlist from the database
        // and populate the playlist details in the UI
    }


}


