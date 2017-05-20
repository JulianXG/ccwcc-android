package com.kalyter.ccwcc.common;

import java.text.SimpleDateFormat;

/**
 * Created by Kalyter on 2017-4-30 0030.
 * 基本配置类
 */

public final class Config {
    public static final String SP = "CCWCC";

    public static final String BASE_API_URL = "http://ccwcc.kalyter.cn";
//    public static final String BASE_API_URL = "http://192.168.1.111:8080";

    public static final int SPLASH_TIME = 3;

    // 百度地图结果码
    public static final int LOCATE_DENIED = 167;

    public static final int REQUEST_CODE_PERMISSION = 1000;

    public static final String KEY_BIRD_NAME = "BIRD_NAME";

    public static final String KEY_QUANTITY = "QUANTITY_NAME";

    public static final String MALE = "MAN";

    public static final String FEMALE = "WOMAN";

    public static final SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy年MM月dd号 HH:mm:ss");

    // SQLite 相关
    public static final String DB_PATH = "/data/data/com.kalyter.ccwcc/databases";
    public static final String DB_FILENAME = "ccwcc.db";

    // Bundle Keys
    public static final String KEY_BUNDLE_BIRD = "BUNDLE_BIRD";
    public static final String KEY_REQUEST_SOURCE = "request_source";
    public static final String KEY_BUNDLE_SELECT_BIRDS = "SELECT_BIRDS";

    // Intent 中的请求码，requestCode
    public static final int REQUEST_SEARCH_CODE = 1001;
    public static final int REQUEST_BATCH_CODE = 1002;

    public static final String NO_SPECIES_NAME_TAG = "未被分类";

    // API返回码
    public static final int RESPONSE_CODE_OK = 200;
    public static final int RESPONSE_CODE_AUTH_DENIED = 403;
}
