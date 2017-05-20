package com.kalyter.ccwcc.data.repository;

import android.content.SharedPreferences;
import android.content.Context;

import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.model.Checkpoint;
import com.kalyter.ccwcc.model.Token;
import com.kalyter.ccwcc.model.User;
import com.kalyter.ccwcc.util.Util;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class SplashRepository implements SplashSource {
    private SharedPreferences mSharedPreferences;
    private Context mContext;
    private static final String KEY_IS_FIRST_RUN = "IS_FIRST_RUN";
    private static final String KEY_USER = "USER";
    private static final String KEY_CHECKPOINT = "CHECKPOINT";
    private static final String KEY_IS_LOGIN = "IS_LOGIN";
    private static final String KEY_TOKEN = "TOKEN";

    public SplashRepository(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Config.SP, Context.MODE_PRIVATE);
    }

    @Override
    public boolean getIsFirstRun() {
        return mSharedPreferences.getBoolean(KEY_IS_FIRST_RUN, true);
    }

    @Override
    public void updateIsFirstRun(boolean isFirstRun) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(KEY_IS_FIRST_RUN, isFirstRun);
        editor.apply();
    }

    @Override
    public void updateUser(User user) {
        Util.updateField(mContext, KEY_USER, user);
    }

    @Override
    public User getCurrentUser() {
        return Util.deserialize(mSharedPreferences.getString(KEY_USER, ""), User.class);
    }

    @Override
    public Checkpoint getCheckpoint() {
        return Util.deserialize(mSharedPreferences.getString(KEY_CHECKPOINT, ""), Checkpoint.class);
    }

    @Override
    public void updateCheckpoint(Checkpoint checkpoint) {
        Util.updateField(mContext, KEY_CHECKPOINT, checkpoint);
    }

    @Override
    public boolean getIsLogin() {
        return mSharedPreferences.getBoolean(KEY_IS_LOGIN, false);
    }

    @Override
    public void updateIsLogin(boolean isLogin) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGIN, isLogin);
        editor.apply();
    }

    @Override
    public String getToken() {
        return mSharedPreferences.getString(KEY_TOKEN, "");
    }

    @Override
    public void updateToken(Token token) {
        Util.updateField(mContext, KEY_TOKEN, token);
    }
}
