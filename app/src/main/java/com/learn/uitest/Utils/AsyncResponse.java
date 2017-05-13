package com.learn.uitest.Utils;

import org.androidannotations.annotations.EBean;

/**
 * Created by Emily on 2017/4/18.
 */

public interface AsyncResponse {
    void processFinish(HttpJson output);
}
