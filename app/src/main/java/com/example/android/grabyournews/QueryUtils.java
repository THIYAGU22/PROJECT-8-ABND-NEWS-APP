package com.example.android.grabyournews;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ravi on 6/8/2017.
 */

public final class QueryUtils {

    //TAG FOR LOG MESSAGES
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * CREATE A PRIVATE CONSTRUCTOR
     * THIS class is only meant to hold the static variables and method which can be accesed
     * directly from the class name queryutil
     */
    private QueryUtils() {
    }

    /**
     * @param requestUrl query the guardian api and return a list of{@Link news}
     */

    public static List<news> fetchNewsData(String requestUrl) {

        //create a url obj
        URL url = createUrl(requestUrl);

        //perform a http req to url  and recieve a json response
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making http request", e);
        }
        //which extracts rjson response to newsList
        List<news> newsList = extractFeaturesJson(jsonResponse);
        return newsList;
    }

    //returns a url object from the given string url
    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Probs for building url !!!!!!!!!!!", e);
        }
        return url;
    }

    // setting up a new method to makeHttprequest method to given url and returns a string a a response
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //if url is null returns a jsondata
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000/*milliseconds**/);
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(15000/**milliseconds*/);
            urlConnection.connect();

            //if the req was succesful
            //gets the data from inputStream and parse the jonResponse
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem the retreiving the guardian results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                //closing the input stream throws IOEXCEPTion..
                //makeHttpRequet (URL) also throws IoException--->>>>
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * convert the {@Link InputStream} into a string which contain the whole
     * json response from server
     *
     * @param inputStream
     * @return
     * @throws IOException
     */

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<news> extractFeaturesJson(String JsonData) {
        if (TextUtils.isEmpty(JsonData)) {
            return null;
        }


        //Create an empty arraylist (newsList) to add news


        List<news> newsList = new ArrayList<>();

        //try to parsing the json data from guardian server
        //if some error is there when parsing the json,
        //catch will do the exception so the app doesnt crash it will the catch the errors
        //catch will prints error message
        try {
            //create a json object from json response string
            JSONObject myJsonObject = new JSONObject(JsonData);

            //extracting jsonobject called response
            JSONObject response = myJsonObject.getJSONObject("response");

            //extracting a json array called results
            JSONArray results = response.getJSONArray("results");


            //for each results in the array associated with response
            for (int d = 0; d < results.length(); d++) {

                //get a newsobject to retrieve the results
                JSONObject newsObject = results.getJSONObject(d);

                //Extract the value for key called "webTitle"
                String title = newsObject.getString("webTitle");

                //Extract the value for key called "sectionNAme"
                String sectionName = newsObject.getString("sectionName");

                //Extract the value for key called "webTUrl"
                String webUrl = newsObject.getString("webUrl");

                //if the author name isnt available it will be displayed
                String author = "NOT AVAIALABLE";





                //creating a list (authorList for the author name)
                List<String> authorList = new ArrayList<>();
                String[] authorArray = new String[]{};
                //extracting a json array called tags
                JSONArray Tags = newsObject.getJSONArray("tags");

                for (int t = 0; t < Tags.length(); t++) {
                    //creating a news object called tags to retrieve the author names
                    JSONObject tags = Tags.getJSONObject(t);
                    //extract the value for key called"firstName"
                    String firstName = tags.optString("firstName");
                    //extract the value for key called "latname"
                    String lastName = tags.optString("lastName");
                    //author names will be explained here
                    String Author;
                    //if the authorname equals firstname
                    //it  will be displayed
                    if (TextUtils.isEmpty(firstName)) {
                        Author = lastName;
                    }
                    //authorname + space + lastname will be displayed in else block
                    else {
                        Author = firstName + " " + lastName;
                    }
                    //authorList will be displayed with authorNames
                    authorList.add(Author);
                }
                //if authornames isnt available default author = N/A will be displayed
                if (authorList.size() == 0) {
                    author = "N/A";
                }
                //else author names will be displayed
                else {
                    author = TextUtils.join(",", authorList);
                }
                //title author weburl will be retrieved here and newslist wil be displayed
                news News = new news(title, author, sectionName, webUrl);
                newsList.add(News);
            }
        } catch (JSONException e) {
            //if the error persists the app doesnt crash
            // it will catch the exact messages and display in logcat
            Log.e(LOG_TAG, "Problem retrieving the json results", e);
        }
        //return the newslist
        return newsList;
    }
}