package com.lawrey.newsapp;

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

/**
 * Created by Lawrey on 1/1/17.
 */

public class NewsUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = NewsUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link NewsUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private NewsUtils() {
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<News> extractNews(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        Log.v("Log Loader", "fetchNewsData");

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<News> news = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
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

    /**
     * Return an {@link News} object by parsing out information
     * about the first earthquake from the input earthquakeJSON string.
     */
    private static ArrayList<News> extractFeatureFromJson(String newsJSON) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<News> news = new ArrayList<>();

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        try {

            // build up a list of Earthquake objects with the corresponding data.
            JSONObject result = new JSONObject(newsJSON);
            JSONObject jsonObject = result.getJSONObject("response");

            JSONArray results = jsonObject.getJSONArray("results");

            Log.v("results", String.valueOf(results.length()));

            for (int i = 0; i < results.length(); i++) {

                JSONObject newsObj = results.getJSONObject(i);

                String webTitle = "", sectionName = "", author = "", webPublicationDate = "", webUrl = "";

                if(newsObj.has("webTitle")){
                    webTitle = newsObj.getString("webTitle");
                }

                if(newsObj.has("sectionName")) {
                    sectionName = newsObj.getString("sectionName").toString();
                }

                if(newsObj.has("webPublicationDate")){
                    webPublicationDate = newsObj.getString("webPublicationDate");
                }

                if(newsObj.has("webUrl")){
                    webUrl = newsObj.getString("webUrl");
                }

                news.add(new News(webTitle, sectionName, webPublicationDate, webUrl));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the books JSON results", e);
        }

        return news;
    }


}
