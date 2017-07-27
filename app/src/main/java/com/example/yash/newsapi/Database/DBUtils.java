package com.example.yash.newsapi.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.yash.newsapi.NewsItem;

/**
 * Created by Yash on 7/27/17.
 */

public class DBUtils {
    public static Cursor getAllNewsCursor(SQLiteDatabase db) {
        Cursor cursor = db.query(Contract.TABLE_ARTICLES.TABLE_NAME, null, null,
                null, null, null,
                Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED + " DESC");
        return cursor;
    }


    public static void getNews(SQLiteDatabase db, NewsItem[] newsItems) {
        db.beginTransaction();
        deleteAllNews(db);
        try {
            for (NewsItem newsItem : newsItems) {
                ContentValues cv = new ContentValues();
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE,
                        newsItem.getTitle());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION,
                        newsItem.getDescription());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED,
                        newsItem.getPublishedAt());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_IMAGE_URL,
                        newsItem.getURLtoImge());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_NEWS_URL,
                        newsItem.getURL());
                db.insert(Contract.TABLE_ARTICLES.TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private static void deleteAllNews(SQLiteDatabase db) {
        db.delete(Contract.TABLE_ARTICLES.TABLE_NAME, null, null);
    }
}