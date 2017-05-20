package com.kalyter.ccwcc.data.source;

import com.kalyter.ccwcc.model.Record;

import java.util.List;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface DraftSource {
    void save(List<Record> recordList);

    void save(Record record);

    List<Record> getAllRecords();

    void updateAll(List<Record> records);

    void delete(int index);

    void delete(List<Record> records);
}
