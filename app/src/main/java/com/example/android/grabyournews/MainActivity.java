
package com.example.android.grabyournews;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<news>> {
    /**
     * Log_tag for displaying the log messages
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Url for the guardian api link
     */
    private static final String NEWS_REQUEST_URL = "http://content.guardianapis.com/search?q=debate&tag=politics/politics&from-date=2014-01-01&api-key=test&page-size=20&show-tags=contributor";

    /**
     * constant value for loader id such as 1
     * and calling it back by loadermanager,loadercallbacks
     */
    private static final int NEWS_LOADER_ID = 1;

    /**
     * Adapter for the list of the news
     */
    private newsAdapter mAdapter;

    /**
     * it is a empty textview . this comes to play when nothing is diplayed
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**calling a news_list.xml into main stage
         */
        ListView newsListView = (ListView) findViewById(R.id.news_list);

        /**
         * create a new adapter that takes empty list of news
         */
        mAdapter = new newsAdapter(this, new ArrayList<news>());
        newsListView.setAdapter(mAdapter);
        /**
         * calling that empty textView
         * when there is nothing to display
         */
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(mEmptyStateTextView);

        //do something when the button is clicked
        /*making the item click listener to the news list view which makes it easier to browse the internet
         */
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //find the current news when the particular item was clicked on
                news currentNews = mAdapter.getItem(position);
                // here parsing work is done
                // string urL is converted into uri object--->>
                Uri newsUri = Uri.parse(currentNews.getWebUrl());
                //creating a intent which makes the link to URI object
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                //which makes the launching app successfully
                startActivity(websiteIntent);
            }
        });
        //Connectivity manager is the head and he will provide stable ecure connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        //if network connection is available info will be executed
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
        //if there isnt connection progress bar(loading symbol)will appear as long as
        //no data is retrieved
        else {
            View loadingSymbol = findViewById(R.id.loading_symbol);
            loadingSymbol.setVisibility(View.VISIBLE);
            //when there isnt secure connection default msg will be displayed
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    //implementing oncreateloader method to execute this activity with new request url (guardian api page)
    @Override
    public Loader<List<news>> onCreateLoader(int id, Bundle args) {
        return new newsLoader(this, NEWS_REQUEST_URL);
    }

    //on load finished method is called when data appeared
    //when data appears as soon as loading ymbol vanishes here in this method
    @Override
    public void onLoadFinished(Loader<List<news>> loader, List<news> data) {
        //loading symbol vanishes here--->>
        View loadingSymbol = findViewById(R.id.loading_symbol);
        loadingSymbol.setVisibility(View.GONE);

        //if there is list " news" add them to mAdapter
        //this lets them to add all data to listview
        if (data != null && data.isEmpty()) {
            mAdapter.addAll(data);
        } else {
            mEmptyStateTextView.setText(R.string.no_internet);
        }
        mAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<news>> loader) {
        //loader reset method is called when this method 's work is to vanish the list of previous one...
        mAdapter.clear();
    }
}
//All done ...work completed
