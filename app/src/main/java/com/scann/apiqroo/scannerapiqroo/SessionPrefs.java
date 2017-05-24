package com.scann.apiqroo.scannerapiqroo;

/**
 * Created by Uriel Velasquez on 19/05/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

//import com.hermosaprogramacion.blog.saludmock.data.api.model.Affiliate;

/**
 * Manejador de preferencias de la sesiÃ³n del afiliado
 */
public class SessionPrefs {

    public static final String PREFS_NAME = "SALUDMOCK_PREFS";
    public static final String PREFS_USER_MAIL = "PREFS_USER_MAIL";
    public static final String PREF_USER_ID = "PREF_USER_ID";


    private final SharedPreferences mPrefs;

    private boolean mIsLoggedIn = false;

    private static SessionPrefs INSTANCE;

    public static SessionPrefs get(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new SessionPrefs(context);
        }
        return INSTANCE;
    }

    private SessionPrefs(Context context) {
        mPrefs = context.getApplicationContext()
                .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        mIsLoggedIn = !TextUtils.isEmpty(mPrefs.getString(PREF_USER_ID, null));
    }

    public boolean isLoggedIn() {
        return mIsLoggedIn;
    }

    public void saveUser(String mail , String id) {

            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putString(PREF_USER_ID, id);
            editor.putString(PREFS_USER_MAIL, mail);
            editor.putInt("first_log", 1);
            editor.apply();

            mIsLoggedIn = true;

    }

    public void logOut(){
        mIsLoggedIn = false;
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.putString(PREF_USER_ID, null);
        editor.putString(PREFS_USER_MAIL, null);
        editor.putInt("first_log", 0);

        editor.apply();
    }
}
