package com.lawrey.newsapp;

/**
 * Created by Lawrey on 1/1/17.
 */

public class News {

    private String mTitle;
    private String mSection;
    private String mPublishedDate;
    private String mWebUrl;

    public News(String mTitle, String mSection, String mPublishedDate, String mWebUrl) {
        this.mTitle = mTitle;
        this.mSection = mSection;
        this.mPublishedDate = mPublishedDate;
        this.mWebUrl = mWebUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSection() {
        return mSection;
    }

    public String getmPublishedDate() {
        return mPublishedDate;
    }

    public String getmWebUrl() {
        return mWebUrl;
    }
}
