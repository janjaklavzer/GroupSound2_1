package si.uni_lj.fe.tnuv.groupsound2_1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity{

    private SongAdapter songAdapter;
    private ListView listViewPlaylist;
    private ArrayList<String> songItem;

    private String qrCodeValue;


    private String playlistName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        listViewPlaylist = findViewById(R.id.listViewSongs);

        // Initialize the songItem ArrayList
        songItem = new ArrayList<>();

        // Create the adapter for the playlist rows
        songAdapter = new SongAdapter(this, songItem);
        listViewPlaylist.setAdapter(songAdapter);

        // Get the playlist name from the intent
        playlistName = getIntent().getStringExtra("playlistName");

        qrCodeValue = getIntent().getStringExtra("QRcodeValue");


        UserDatabase userDatabase = new UserDatabase(this);

        // Retrieve the logged-in user's information

        // Retrieve the username from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_database", Context.MODE_PRIVATE);
        String loggedInUsername = sharedPreferences.getString("logged_in_username", null);

        if (qrCodeValue != null)  playlistName = qrCodeValue;
        loadSongsFromDatabase(playlistName);

        // add button
        ImageButton addSong = findViewById(R.id.addSong);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSongDialog(loggedInUsername);
            }
        });

    }

    private void addNewSong(String songName,String playlistName) {
        // Check if the song already exists
        if (songItem.contains(songName)) {
            // Show an error message or handle the duplicate entry as desired
            Toast.makeText(PlaylistActivity.this, "Song already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the new playlist to the list
        songItem.add(songName);

        // Notify the adapter that the dataset has changed
        songAdapter.notifyDataSetChanged();

        // Scroll to the newly added playlist
        listViewPlaylist.smoothScrollToPosition(songItem.size() - 1);

        // Create an instance of the database helper
        SongDatabaseHelper databaseHelper = new SongDatabaseHelper(this);

        // Get a writable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a ContentValues object to store the playlist data
        ContentValues values = new ContentValues();
        values.put(SongDatabaseHelper.COLUMN_PLAYLIST_ID, playlistName);
        values.put(SongDatabaseHelper.COLUMN_SONG_NAME, songName);

        Log.d("values", "COLUMN_PLAYLIST_NAME,COLUMN_USER_ACCOUNT " + values);

        // Insert the playlist into the database
        long playlistId = db.insert(SongDatabaseHelper.TABLE_SONGS, null, values);

        if (playlistId != -1) {
            // Insertion successful
            Toast.makeText(PlaylistActivity.this, "New song added: " + songName, Toast.LENGTH_SHORT).show();
        }

        // Close the database connection
        db.close();
    }

    private void showSongDialog(String UserAccount) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_song, null);
        builder.setView(dialogView);

        // Find the EditText field in the dialog layout
        EditText etSongName = dialogView.findViewById(R.id.etSongName);

        // Set up the dialog buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            // Get the playlist name from the EditText field
            String songName = etSongName.getText().toString().trim();

            // Check if the playlist name is empty
            if (!playlistName.isEmpty()) {
                // Add the new playlist to the list
                addNewSong(songName,playlistName);
                //Toast.makeText(MyplaylistsActivity.this, "New playlist added: " + playlistName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PlaylistActivity.this, "Song name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteSong(String songName) {
        // Remove the playlist from the list
        songItem.remove(songName);

        // Notify the adapter that the dataset has changed
        songAdapter.notifyDataSetChanged();

        // Delete the playlist from the database
        SongDatabaseHelper databaseHelper = new SongDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereClause = SongDatabaseHelper.COLUMN_SONG_NAME + " = ?";
        String[] whereArgs = {songName};
        db.delete(SongDatabaseHelper.TABLE_SONGS, whereClause, whereArgs);
        db.close();

        Toast.makeText(PlaylistActivity.this, "Song deleted", Toast.LENGTH_SHORT).show();
    }
    private void loadSongsFromDatabase(String playlistId) {
        Log.d("LOAD SONG", playlistId);
        SongDatabaseHelper databaseHelper = new SongDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {SongDatabaseHelper.COLUMN_SONG_NAME};
        String selection = SongDatabaseHelper.COLUMN_PLAYLIST_ID + " = ?";
        String[] selectionArgs = {playlistId};


        Cursor cursor = db.query(
                SongDatabaseHelper.TABLE_SONGS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            //songItem.clear(); // Clear the existing song items
            do {
                String songName = cursor.getString(cursor.getColumnIndexOrThrow(SongDatabaseHelper.COLUMN_SONG_NAME));
                songItem.add(songName);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
    }


}


