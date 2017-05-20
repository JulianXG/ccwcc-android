package com.kalyter.ccwcc.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.data.repository.DBRepository;
import com.kalyter.ccwcc.data.repository.DraftRepository;
import com.kalyter.ccwcc.data.repository.SplashRepository;
import com.kalyter.ccwcc.data.source.DBSource;
import com.kalyter.ccwcc.data.source.DraftSource;
import com.kalyter.ccwcc.data.source.FlagService;
import com.kalyter.ccwcc.data.source.RecordService;
import com.kalyter.ccwcc.data.source.SplashSource;
import com.kalyter.ccwcc.data.source.UserService;
import com.kalyter.ccwcc.util.Util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class InjectClass {
    private static final String TAG = "InjectClass";

    private UserService mUserService;
    private RecordService mRecordService;
    private DBSource mDBSource;
    private DraftSource mDraftSource;
    private SplashSource mSplashSource;
    private Retrofit mRetrofit;
    private SQLiteDatabase mSQLiteDatabase;
    private FlagService mFlagService;

    private Context mContext;

    public InjectClass(Context context) {
        mContext = context;
        initRetrofit();
        mUserService = mRetrofit.create(UserService.class);
        mRecordService = mRetrofit.create(RecordService.class);
        mSplashSource = new SplashRepository(mContext);
        mSQLiteDatabase = Util.initDatabase(mContext, Config.DB_PATH, Config.DB_FILENAME);
        mDraftSource = new DraftRepository(mContext);
        mDBSource = new DBRepository(mSQLiteDatabase);
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mSQLiteDatabase;
    }

    public UserService getUserService() {
        return mUserService;
    }

    public RecordService getRecordService() {
        return mRecordService;
    }

    public SplashSource getSplashSource() {
        return mSplashSource;
    }

    public DraftSource getDraftSource() {
        return mDraftSource;
    }

    public DBSource getDBSource() {
        return mDBSource;
    }

    public FlagService getFlagService() {
        return mRetrofit.create(FlagService.class);
    }

    private void initRetrofit() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(
                new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.i(TAG, message);
                    }
                });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
            .baseUrl(Config.BASE_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Util.getGsonInstance()))
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();
    }

    public void terminate() {
        mSQLiteDatabase.close();
    }
}
