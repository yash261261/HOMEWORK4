package com.example.yash.newsapi.Scheduler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.yash.newsapi.Database.DBHelper;
import com.example.yash.newsapi.Database.DBUtils;
import com.example.yash.newsapi.NetworkUtils;
import com.example.yash.newsapi.NewsItem;
import com.example.yash.newsapi.ParseJSON;
import com.firebase.jobdispatcher.JobParameters;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Yash on 7/27/17.
 */

public class Service extends com.firebase.jobdispatcher.JobService {


    @Override
    public boolean onStartJob(JobParameters job) {
        new AsyncTask(){

            @Override
            protected Object doInBackground(Object[] params) {
                updateDatabaseFromServer(Service.this);
                return null;
            }
        }.execute();
        Toast.makeText(Service.this, "News Updated", Toast.LENGTH_SHORT).show();
        return false;
    }

    // REFRESHES THE DATABASE WITH NEW NEWS
        public static void updateDatabaseFromServer(Context context) {
            try {
                URL newsURL = NetworkUtils.buildUrl();

                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(newsURL);
                if (jsonResponse != null && !jsonResponse.isEmpty()) {
                    ArrayList<NewsItem> newsItems = ParseJSON.parseJSON(jsonResponse);
                    SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
                    DBUtils.getNews(db, newsItems);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
