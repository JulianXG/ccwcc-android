package com.kalyter.ccwcc.data.source;

import com.kalyter.ccwcc.model.Flag;
import com.kalyter.ccwcc.model.Response;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Kalyter on 2017-5-20 0020.
 */

public interface FlagService {
    @POST("/api/record/flag")
    Observable<Response> postFlag(@Body Flag flag);

}
