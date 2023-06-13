package si.uni_lj.fe.tnuv.groupsound2_1;



import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyplaylistsActivity extends AppCompatActivity {

    //private ArrayAdapter<String> playlistAdapter;
    private ArrayList<String> playlistItems;
    private ListView listViewPlaylists;
    private PlaylistsAdapter playlistAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplaylists);

        listViewPlaylists = findViewById(R.id.listViewPlaylists);

        // Initialize playlist items
        playlistItems = new ArrayList<>();

        // Create the adapter for the playlist rows
        playlistAdapter = new PlaylistsAdapter(this, playlistItems);
        listViewPlaylists.setAdapter(playlistAdapter);

        Intent intent = getIntent();
        String loggedInUsername = intent.getStringExtra("loggedInUsername");
        Log.d("LoginActivity", "Logged-in Username: " + loggedInUsername);



        // Load playlists from the database
        loadPlaylistsFromDatabase(loggedInUsername);

        // Handle playlist item click events
        listViewPlaylists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle playlist item click
                String selectedPlaylist = playlistItems.get(position);
                Toast.makeText(MyplaylistsActivity.this, "Selected Playlist: " + selectedPlaylist, Toast.LENGTH_SHORT).show();
                showPlaylistOptions(selectedPlaylist);

            }
        });

        // add button
        ImageButton addPlaylist = findViewById(R.id.addPlaylist);
        addPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddPlaylistDialog(loggedInUsername);
            }
        });


    }





    private void addNewPlaylist(String playlistName, String userAccount) {

        // Check if the playlist already exists
        if (playlistItems.contains(playlistName)) {
            // Show an error message or handle the duplicate entry as desired
            Toast.makeText(MyplaylistsActivity.this, "Playlist already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add the new playlist to the list
        playlistItems.add(playlistName);

        // Notify the adapter that the dataset has changed
        playlistAdapter.notifyDataSetChanged();

        // Scroll to the newly added playlist
        listViewPlaylists.smoothScrollToPosition(playlistItems.size() - 1);

        // Create an instance of the database helper
        PlaylistDatabaseHelper databaseHelper = new PlaylistDatabaseHelper(this);

        // Get a writable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Create a ContentValues object to store the playlist data
        ContentValues values = new ContentValues();
        values.put(PlaylistDatabaseHelper.COLUMN_PLAYLIST_NAME, playlistName);
        values.put(PlaylistDatabaseHelper.COLUMN_USER_ACCOUNT, userAccount);

        Log.d("values", "COLUMN_PLAYLIST_NAME,COLUMN_USER_ACCOUNT " + values);

        // Insert the playlist into the database
        long playlistId = db.insert(PlaylistDatabaseHelper.TABLE_PLAYLISTS, null, values);

        if (playlistId != -1) {
            // Insertion successful
            Toast.makeText(MyplaylistsActivity.this, "New playlist added: " + playlistName, Toast.LENGTH_SHORT).show();
        }

        // Close the database connection
        db.close();

    }

    private void showAddPlaylistDialog(String UserAccount) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_playlist, null);
        builder.setView(dialogView);

        // Find the EditText field in the dialog layout
        EditText etPlaylistName = dialogView.findViewById(R.id.etPlaylistName);

        // Set up the dialog buttons
        builder.setPositiveButton("Add", (dialog, which) -> {
            // Get the playlist name from the EditText field
            String playlistName = etPlaylistName.getText().toString().trim();

            // Check if the playlist name is empty
            if (!playlistName.isEmpty()) {
                // Add the new playlist to the list
                addNewPlaylist(playlistName,UserAccount);
                //Toast.makeText(MyplaylistsActivity.this, "New playlist added: " + playlistName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MyplaylistsActivity.this, "Playlist name cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    public void showPlaylistOptions(String playlistName) {

        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Playlist Options");


        // Define the options list
        final String[] options = {"Delete Playlist", "Create QR Code"};

        // Set the options list as the dialog items
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                handlePlaylistOption(playlistName, selectedOption);
            }
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void handlePlaylistOption(String playlistName, String option) {


        switch (option) {
            case "Delete Playlist":
                deletePlaylist(playlistName);
                break;
            case "Create QR Code":
                generateQRCode(playlistName);
                break;
        }
    }

    public void deletePlaylist(String playlistName) {
        // Remove the playlist from the list
        playlistItems.remove(playlistName);

        // Notify the adapter that the dataset has changed
        playlistAdapter.notifyDataSetChanged();

        // Delete the playlist from the database
        PlaylistDatabaseHelper databaseHelper = new PlaylistDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereClause = PlaylistDatabaseHelper.COLUMN_PLAYLIST_NAME + " = ?";
        String[] whereArgs = {playlistName};
        db.delete(PlaylistDatabaseHelper.TABLE_PLAYLISTS, whereClause, whereArgs);
        db.close();

        Toast.makeText(MyplaylistsActivity.this, "Playlist deleted: " + playlistName, Toast.LENGTH_SHORT).show();
    }

    public void generateQRCode(String playlistName) {
        // Generate the content for the QR code (e.g., playlist URL)
        String playlistUrl = generatePlaylistUrl(playlistName);

        // Set QR code dimensions
        int width = 500;
        int height = 500;

        try {
            // Configure QR code parameters
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Generate QR code bitmap
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(playlistUrl, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            qrCodeBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            // Display the QR code or save it as an image
            // Example: Show the QR code in an ImageView
            //ImageView imageViewQRCode = findViewById(R.id.imageViewQRCode);
            //imageViewQRCode.setImageBitmap(qrCodeBitmap);

            showQRCodeDialog(qrCodeBitmap);

            // Example: Save the QR code as a file
            // File qrCodeFile = new File(getExternalCacheDir(), "playlist_qr_code.png");
            // qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(qrCodeFile));

        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
        }
    }

    public String generatePlaylistUrl(String playlistName) {
        // Generate the URL based on the playlist name
        // Example: "https://your-domain.com/playlists/playlist-name"
        return "https://your-domain.com/playlists/" + playlistName;
    }

    public void showQRCodeDialog(Bitmap qrCodeBitmap) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Code");

        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_qr_code, null);
        builder.setView(dialogView);

        // Find the ImageView in the dialog layout
        ImageView imageViewQRCode = dialogView.findViewById(R.id.imageViewQRCode);
        imageViewQRCode.setImageBitmap(qrCodeBitmap);

        // Set up the dialog buttons
        builder.setPositiveButton("Close", null);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void loadPlaylistsFromDatabase(String userAccount) {
        PlaylistDatabaseHelper databaseHelper = new PlaylistDatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {PlaylistDatabaseHelper.COLUMN_PLAYLIST_NAME};
        String selection = PlaylistDatabaseHelper.COLUMN_USER_ACCOUNT + " = ?";
        String[] selectionArgs = {userAccount};


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
            playlistItems.clear(); // Clear the existing playlist items
            do {
                String playlistName = cursor.getString(cursor.getColumnIndexOrThrow(PlaylistDatabaseHelper.COLUMN_PLAYLIST_NAME));
                playlistItems.add(playlistName);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
    }




}
