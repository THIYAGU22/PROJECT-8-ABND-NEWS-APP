package com.example.android.grabyournews;

/**
 * Created by ravi on 6/8/2017.
 */

public class news {

    //title of the news
    private String title;

    //name of the author
    private String author;

    //link of the page
    private String webUrl;

    //section of the news
    private String sectionName;

    /**
     * constructs a new {@Link news}
     *
     * @param title
     * @param author
     * @param webUrl
     */
    public news(String title, String author, String sectionName, String webUrl) {
        this.title = title;
        this.author = author;
        this.webUrl = webUrl;
        this.sectionName = sectionName;
    }

    //retrieves the title name using get method
    public String getTitle() {
        return title;
    }

    //retrieves the author name using get method
    public String getAuthor() {
        return author;
    }


    //retrieves the weburl  using get method
    public String getWebUrl() {
        return webUrl;
    }

    //retrieves the sectionName  using get method
    public String getSectionName() {
        return sectionName;
    }

}


