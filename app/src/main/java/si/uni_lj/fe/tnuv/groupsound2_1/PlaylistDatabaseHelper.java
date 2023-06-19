package si.uni_lj.fe.tnuv.groupsound2_1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaylistDatabaseHelper extends SQLiteOpenHelper{
    public static final String TABLE_PLAYLISTS = "playlists";
    public static final String COLUMN_PLAYLIST_NAME = "playlist_name";
    public static final String COLUMN_USER_ACCOUNT = "user_account";
    private static final String DATABASE_NAME = "playlist.db";
    private static final int DATABASE_VERSION = 10;
    private static final String COLUMN_ID = "_id";

    public static final String COLUMN_UUID = "uuid";


    public PlaylistDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        Log.d("PLAY LIST", "PLAY LIST");
        // Create the playlists table
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_PLAYLISTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ACCOUNT + " TEXT, " +
                COLUMN_PLAYLIST_NAME + " TEXT, " +
                COLUMN_UUID + " TEXT" +
                ")";
        db.execSQL(createTableQuery);


        String createSongTable = "CREATE TABLE IF NOT EXISTS " + SongDatabaseHelper.TABLE_SONGS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SongDatabaseHelper.COLUMN_SONG_NAME + " TEXT," +
                SongDatabaseHelper.COLUMN_PLAYLIST_ID + " TEXT" +
                ")";
        db.execSQL(createSongTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Perform necessary modifications for version 3
            db.execSQL("ALTER TABLE " + TABLE_PLAYLISTS);

            onCreate(db);
        }

    }



    
}
