package si.uni_lj.fe.tnuv.groupsound2_1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SongDatabaseHelper extends SQLiteOpenHelper{
    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_NAME = "song_name";
    // TODO Convert this from text to ID
    public static final String COLUMN_PLAYLIST_ID = "playlist_id";

    private static final String DATABASE_NAME = "playlist.db";
    private static final int DATABASE_VERSION = 10;
    private static final String COLUMN_ID = "_id";


    public SongDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("aod", "CREATE SONG TABLRE");
        // Create the playlists table
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_SONGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SONG_NAME + " TEXT," +
                COLUMN_PLAYLIST_ID + " TEXT" +
                ")";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Perform necessary modifications for version 3
            db.execSQL("ALTER TABLE " + TABLE_SONGS +
                    " ADD COLUMN " + COLUMN_SONG_NAME + " TEXT");
            onCreate(db);
        }

    }




}
