package com.example.android.grabyournews;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by ravi on 6/9/2017.
 */

public class newsLoader extends AsyncTaskLoader<List<news>> {

    /**
     * tag for log msgs (which helps us to idenyify the error in logcat)
     */
    private static final String LOG_TAG = newsLoader.class.getSimpleName();

    /**
     * URL query
     */
    private String mUrl;

    /**
     * constructs a new @link newsLoader
     *
     * @param url to load the data
     */
    public newsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }
    /**
     * performs the action
     * to intrepret the data for the user
     */
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /*
    *will load the data and executes the task for user in the background
     */
    @Override
    public List<news> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        //performs the network request ,parse the response and extract a list of news
        List<news> News = QueryUtils.fetchNewsData(mUrl);
        return News;
    }
}
