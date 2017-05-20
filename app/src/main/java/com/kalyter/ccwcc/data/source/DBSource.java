package com.kalyter.ccwcc.data.source;

import com.kalyter.ccwcc.model.Bird;

import java.util.List;
import java.util.Map;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface DBSource {
    List<Bird> searchBirds(String keyword);

    Map<String, List<Bird>> getAllBirds();

    List<String> getCategories();
}
