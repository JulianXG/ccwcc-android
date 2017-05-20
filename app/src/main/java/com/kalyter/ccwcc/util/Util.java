package com.kalyter.ccwcc.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.common.Config;
import com.kalyter.ccwcc.login.LoginActivity;
import com.kalyter.ccwcc.model.Checkpoint;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public final class Util {

    public static void updateField(Context context, String key, Object data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SP, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, getGsonInstance().toJson(data));
        editor.apply();
    }

    /**
     * MD5单向加密，生成32位大写字母和数字的组合
     * @param originData 原始字符串
     * @return MD5加密后的数据
     */
    public static String md5(String originData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(originData.getBytes());
            return new BigInteger(1, digest.digest()).toString(16).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * 使Activity全屏显示，需要在setContentView之前调用
     * @param activity 需要全屏的Activity
     */
    public static void fullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static Checkpoint getCheckpointById(Context context, int checkpointId) {
        Checkpoint checkpoint = new Checkpoint();
        String[] checkpoints = context.getResources().getStringArray(R.array.checkpoints);
        checkpoint.setId(checkpointId + 1);
        checkpoint.setName(checkpoints[checkpointId]);
        return checkpoint;
    }


    /**
     * 如果系统版本大于等于6，则动态请求权限，如果权限已经拥有则跳过
     * @param activity 当前Activity
     * @param permissions 需要启用的permission
     */
    public static void requestPermissions(Activity activity, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> needPermissions = new ArrayList<>();
            for (String permission : permissions) {
                int result = ContextCompat.checkSelfPermission(activity, permission);
                if (result == PackageManager.PERMISSION_DENIED) {
                    needPermissions.add(permission);
                }
            }
            if (needPermissions.size() > 0) {
                ActivityCompat.requestPermissions(activity,
                        needPermissions.toArray(new String[]{}),
                        Config.REQUEST_CODE_PERMISSION);
            }
        }
    }

    /**
     * 返回GSON实例单例
     * @return 返回GSON实例单例
     */
    public static Gson getGsonInstance() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        try {
                            long timestamp = json.getAsLong();
                            return new Date(timestamp);
                        } catch (NumberFormatException ignored) {
                        }
                        try {
                            String asString = json.getAsString();
                            asString = asString.replace("\"\"", "\"");
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            return format.parse(asString);
                        } catch (ParseException ignored) {
                        }
                        return null;
                    }
                })
                .create();
    }

    /**
     * 将对象序列化JSON字符串
     * @param object 对象实例
     * @param <T> 泛型类型
     * @return 序列化后的JSON字符串
     */
    public static <T> String serialize(T object) {
        return getGsonInstance().toJson(object);
    }

    /**
     * 将JSON字符串反序列化为对应的对象实例
     * @param serializeContent JSON字符串
     * @param classOfT 对象实例类型
     * @param <T> 对象实例类型
     * @return 反序列化结果
     */
    public static <T> T deserialize(String serializeContent, Class<T> classOfT) {
        return getGsonInstance().fromJson(serializeContent, classOfT);
    }

    /**
     * 将JSON字符串反序列化为对应的对象实例列表
     * @param serializeContent JSON字符串
     * @param classOfT 对象实例类型
     * @param <T> 对象实例类型
     * @return 反序列化结果
     */
    public static <T> List<T> deserializeArray(String serializeContent, Class<T[]> classOfT) {
        T[] array = getGsonInstance().fromJson(serializeContent, classOfT);
        List<T> asList = Arrays.asList(array);
        return new ArrayList<>(asList);
    }

    /**
     * 初始化SQLite数据库，在APP启动的时候
     * @param context
     * @return 初始化后的SQLiteDatabase对象实例
     */
    public static SQLiteDatabase initDatabase(Context context, String path, String filename) {
        SQLiteDatabase sqLiteDatabase = null;
        String absoluteFilename = path + filename;
        File file = new File(absoluteFilename);
        if (file.exists()) {
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(absoluteFilename, null);
        } else {
            new File(path).mkdir();
            try {
                AssetManager assetManager = context.getAssets();
                InputStream inputStream = assetManager.open(filename);
                FileOutputStream outputStream = new FileOutputStream(absoluteFilename);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, count);
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(absoluteFilename, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sqLiteDatabase;
    }

    public static boolean parseBoolean(int status) {
        if (status == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showAuthDenied(Context context) {
        Toast.makeText(context, "登录身份过期，请重新登录", Toast.LENGTH_SHORT).show();
        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
