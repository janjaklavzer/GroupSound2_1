package si.uni_lj.fe.tnuv.groupsound2_1;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PlaylistDatabaseHelper extends SQLiteOpenHelper{
    public static final String TABLE_PLAYLISTS = "playlists";
    public static final String COLUMN_PLAYLIST_NAME = "playlist_name";
    private static final String DATABASE_NAME = "playlist.db";
    private static final int DATABASE_VERSION = 2;
    private static final String COLUMN_ID = "_id";



    public PlaylistDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the playlists table
        String createTableQuery = "CREATE TABLE " + TABLE_PLAYLISTS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_PLAYLIST_NAME + " TEXT" +
                ")";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }



    
}
