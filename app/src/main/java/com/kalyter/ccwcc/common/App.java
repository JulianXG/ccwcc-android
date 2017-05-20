package com.kalyter.ccwcc.common;

import android.app.Application;
import android.content.Context;

import com.baidu.location.LocationClient;
import com.kalyter.ccwcc.data.InjectClass;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public class App extends Application {
    private static InjectClass mInjectClass;
    private static LocationClient mLocationClient;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mInjectClass = new InjectClass(mContext);
        mLocationClient = new LocationClient(mContext);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        mInjectClass.terminate();
    }

    public static InjectClass getInjectClass() {
        return mInjectClass;
    }

    public static LocationClient getLocationClient() {
        return mLocationClient;
    }
}
