package com.buah.farmconnect.session;

import android.content.Context;
import android.content.SharedPreferences;
import com.buah.farmconnect.object.ObjectUser;

/**
 * Created by joenellis on 28/11/2017.
 */

public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private static final String SHARED_PREF_NAME = "FarmConnect";

    private static final String KEY_USER_ID = "keyuserid";
    private static final String KEY_USER_NAME = "keyusername";
    private static final String KEY_USER_EMAIL = "keyuseremail";
    private static final String KEY_USER_PASSWORD = "keyuserpassword";
    private static final String KEY_USER_CONTACT = "keyusercontact";

    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(ObjectUser objectUser) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_USER_ID, objectUser.getUser_id());
        editor.putString(KEY_USER_NAME, objectUser.getFullname());
        editor.putString(KEY_USER_EMAIL, objectUser.getEmail());
        editor.putString(KEY_USER_PASSWORD, objectUser.getPassword());
        editor.putString(KEY_USER_CONTACT, objectUser.getContact());
        editor.apply();
        return true;
    }

    public ObjectUser getobjectUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new ObjectUser(
                sharedPreferences.getString(KEY_USER_ID, null),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_EMAIL, null),
                sharedPreferences.getString(KEY_USER_PASSWORD, null),
                sharedPreferences.getString(KEY_USER_CONTACT, null)

        );
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USER_EMAIL, null) != null) {
            return true;
        }
        {
            return false;
        }
    }

    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }
}
