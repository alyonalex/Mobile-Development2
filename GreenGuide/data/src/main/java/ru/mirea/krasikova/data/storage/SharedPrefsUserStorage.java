package ru.mirea.krasikova.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUserStorage {
    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER_TYPE = "user_type";

    private final SharedPreferences prefs;

    public SharedPrefsUserStorage(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUserType(String userType) {
        prefs.edit().putString(KEY_USER_TYPE, userType).apply();
    }

    public String getUserType() {
        return prefs.getString(KEY_USER_TYPE, "guest");
    }
}
