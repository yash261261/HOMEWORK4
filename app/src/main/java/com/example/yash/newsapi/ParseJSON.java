package com.example.yash.newsapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Yash on 6/22/17.
 */

public final class ParseJSON {





        public static ArrayList<NewsItem> parseJSON(String jsonStr) throws JSONException{

            final String Articles ="articles";

            final String Author= "author";

            final String Title="title";

            final String Description="description";

            final String URL="url";

            final String URLtoImge= "urlToImage";

            final String PublishedAt ="publishedAt";


            ArrayList<NewsItem> result = new ArrayList<>();
            JSONObject NewsJson = new JSONObject(jsonStr);
            JSONArray items = NewsJson.getJSONArray(Articles);

            for(int i = 0; i < items.length(); i++){
                JSONObject item = items.getJSONObject(i);


               String Author0=item.getString(Author);
                String Title0=item.getString(Title);
                String Description0=item.getString(Description);
                String URL0=item.getString(URL);
                String URLtoImge0=item.getString(URLtoImge);
                String PublishedAt0=item.getString(PublishedAt);



                NewsItem repo = new NewsItem(Author0, Title0,Description0,URL0,URLtoImge0,PublishedAt0);
                result.add(repo);
            }
            return result;
        }

        }
