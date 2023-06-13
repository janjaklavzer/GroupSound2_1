package si.uni_lj.fe.tnuv.groupsound2_1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaylistDatabaseHelper extends SQLiteOpenHelper{
    public static final String TABLE_PLAYLISTS = "playlists";
    public static final String COLUMN_PLAYLIST_NAME = "playlist_name";
    public static final String COLUMN_USER_ACCOUNT = "user_account";
    private static final String DATABASE_NAME = "playlist.db";
    private static final int DATABASE_VERSION = 5;
    private static final String COLUMN_ID = "_id";

    public static final String COLUMN_SONG_NAME = "song_name";



    public PlaylistDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the playlists table
        String createTableQuery = "CREATE TABLE " + TABLE_PLAYLISTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_PLAYLIST_NAME + " TEXT," +
                COLUMN_SONG_NAME + " TEXT" +
                ")";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion > newVersion) {
            // Perform necessary modifications for version 3
            db.execSQL("ALTER TABLE " + TABLE_PLAYLISTS );

            onCreate(db);
        }

    }



    
}
