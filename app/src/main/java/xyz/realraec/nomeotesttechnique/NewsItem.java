package xyz.realraec.nomeotesttechnique;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsItem implements Comparable<NewsItem> {

    private final int id;
    private final String title;
    private final int secondsSince1970;
    private final String description;
    private final String thumbnail;


    // Only created via a JSONObject so no need for other constructors
    public NewsItem(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.title = jsonObject.getString("title");
        this.secondsSince1970 = jsonObject.getInt("published_at");
        this.description = jsonObject.getString("description");
        this.thumbnail = jsonObject.getString("picture_url");
    }

    /*public NewsItem(int id, String title, int xDaysAgo, String description, String thumbnail) {
        this.id = id;
        this.title = title;
        this.xDaysAgo = xDaysAgo;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public NewsItem() {
        this.id = 0;
        this.title = null;
        this.xDaysAgo = 0;
        this.description = null;
        this.thumbnail = null;
    }*/


    public int getId() {
        return id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public int getSecondsSince1970() {
        return secondsSince1970;
    }

    public String getDescription() {
        return description;
    }

    // So that the Collections.sort() method compares the dates of publication
    @Override
    public int compareTo(NewsItem o) {
        return o.getSecondsSince1970() - this.getSecondsSince1970();
    }
}
