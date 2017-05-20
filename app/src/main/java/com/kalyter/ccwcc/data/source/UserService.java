package com.kalyter.ccwcc.data.source;

import com.kalyter.ccwcc.model.LoginResult;
import com.kalyter.ccwcc.model.Response;
import com.kalyter.ccwcc.model.User;

import retrofit2.http.POST;
import retrofit2.http.Body;
import rx.Observable;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface UserService {
    @POST("/api/login")
    Observable<Response<LoginResult>> login(@Body User user);
}
