package com.kalyter.ccwcc.data.source;

import com.kalyter.ccwcc.model.Checkpoint;
import com.kalyter.ccwcc.model.Token;
import com.kalyter.ccwcc.model.User;

/**
 * Created by Kalyter on 2017-4-30 0030.
 */

public interface SplashSource {
    boolean getIsFirstRun();

    void updateIsFirstRun(boolean isFirstRun);

    void updateUser(User user);

    User getCurrentUser();

    Checkpoint getCheckpoint();

    void updateCheckpoint(Checkpoint checkpoint);

    boolean getIsLogin();

    void updateIsLogin(boolean isLogin);

    String getToken();

    void updateToken(Token token);
}
