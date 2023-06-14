package si.uni_lj.fe.tnuv.groupsound2_1;

import android.content.ContentValues;
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

import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity{

    private ArrayAdapter<String> songAdapter;
    private ListView listViewPlaylist;
    private ArrayList<String> songItem;

    private String playlistName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        listViewPlaylist = findViewById(R.id.listViewSongs);

        // Initialize the songItem ArrayList
        songItem = new ArrayList<>();

        // Create the adapter for the playlist rows
        songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songItem);
        listViewPlaylist.setAdapter(songAdapter);

        // Get the playlist name from the intent
        playlistName = getIntent().getStringExtra("playlistName");

        UserDatabase userDatabase = new UserDatabase(this);

        // Retrieve the logged-in user's information
        SharedPreferences sharedPreferences = getSharedPreferences("user_database", MODE_PRIVATE);
        String loggedInUsername = sharedPreferences.getString("logged_in_username", null);
        if (loggedInUsername != null) {
            String loggedInUser = userDatabase.getUser(loggedInUsername);
            loadSongsFromDatabase(loggedInUser, playlistName);
            } else {
                // Handle case when user is not found
                Log.d("values", "User not found");
            }



        // add button
        ImageButton addSong = findViewById(R.id.addSong);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSongDialog(playlistName);
            }
        });

    }

    private void addNewSong(String songName,String playlistName, String userAccount) {
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
        PlaylistDatabaseHelper databaseHelper = new PlaylistDatabaseHelper(this);

        // Get a writable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a ContentValues object to store the playlist data
        ContentValues values = new ContentValues();
        values.put(PlaylistDatabaseHelper.COLUMN_PLAYLIST_NAME, playlistName);
        values.put(PlaylistDatabaseHelper.COLUMN_USER_ACCOUNT, userAccount);
        values.put(PlaylistDatabaseHelper.COLUMN_SONG_NAME, songName);

        Log.d("values", "COLUMN_PLAYLIST_NAME,COLUMN_USER_ACCOUNT " + values);

        // Insert the playlist into the database
        long playlistId = db.insert(PlaylistDatabaseHelper.TABLE_PLAYLISTS, null, values);

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
                addNewSong(songName,playlistName,UserAccount);
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

    private void loadSongsFromDatabase(String userAccount, String playlistName) {
        PlaylistDatabaseHelper databaseHelper = new PlaylistDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {PlaylistDatabaseHelper.COLUMN_SONG_NAME};
        String selection = PlaylistDatabaseHelper.COLUMN_USER_ACCOUNT + " = ? AND " +
                            PlaylistDatabaseHelper.COLUMN_PLAYLIST_NAME + " = ?";
        String[] selectionArgs = {userAccount, playlistName};


        Cursor cursor = db.query(
                PlaylistDatabaseHelper.TABLE_PLAYLISTS,
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
                String songName = cursor.getString(cursor.getColumnIndexOrThrow(PlaylistDatabaseHelper.COLUMN_SONG_NAME));
                songItem.add(songName);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
    }


}


