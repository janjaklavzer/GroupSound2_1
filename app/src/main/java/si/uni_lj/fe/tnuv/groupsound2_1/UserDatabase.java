package si.uni_lj.fe.tnuv.groupsound2_1;

import android.content.Context;
import android.content.SharedPreferences;

public class UserDatabase {
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "user_database";
    private static final String KEY_USERNAME_PREFIX = "username_";
    private static final String KEY_PASSWORD_PREFIX = "password_";



    public UserDatabase(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Add a user to the database
    public void addUser(String username, String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME_PREFIX + username, username);
        editor.putString(KEY_PASSWORD_PREFIX + username, password);
        editor.apply();
    }

    public String getUser(String username) {
        return username;
    }

    // Check if a username already exists in the database
    public boolean isUserExists(String username) {
        return sharedPreferences.contains(KEY_USERNAME_PREFIX + username);
    }

    // Validate user credentials
    public boolean isValidCredentials(String username, String password) {
        String savedPassword = sharedPreferences.getString(KEY_PASSWORD_PREFIX + username, null);
        return savedPassword != null && savedPassword.equals(password);
    }


}
