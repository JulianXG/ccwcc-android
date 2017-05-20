package com.kalyter.ccwcc.data.source;

import com.kalyter.ccwcc.model.Record;
import com.kalyter.ccwcc.model.Response;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface RecordService {
    @POST("/api/record/bird")
    Observable<Response> postBirdRecord(@Body Record record);

    @POST("/api/record/birds")
    Observable<Response> postBirdRecords(@Body List<Record> records);
}
