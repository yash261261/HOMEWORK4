package com.example.yash.newsapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.yash.newsapi.Database.Contract;
import com.example.yash.newsapi.Database.DBHelper;
import com.example.yash.newsapi.Database.DBUtils;
import com.example.yash.newsapi.Scheduler.Utils;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Void>>, Adapter.ItemClickListener {

    //  private TextView NewsApiTextView;

    private TextView errorTextView;
    ProgressBar pb;
    private RecyclerView rv;

    private Adapter adapter;
    private Cursor cursor;
    private SQLiteDatabase db;
    private static final int NEWS_LOADER =1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NewsApiTextView = (TextView) findViewById(R.id.news_data_search);

        rv = (RecyclerView) findViewById(R.id.rv1);
        errorTextView = (TextView) findViewById(R.id.error_message);
        pb = (ProgressBar) findViewById(R.id.progressbar);
        rv.setLayoutManager(new LinearLayoutManager(this));


        // ACTIVITY IS LOAD IN DATABASE FOR DISPLAY
        db=  new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DBUtils.getAllNewsCursor(db);
        adapter = new Adapter(cursor, this);
        rv.setAdapter(adapter);

        checkLoadFirstTime();

        Utils.scheduleRefresh(this);


    }

    // ACTIVITY CHECK WHEATHER AP IS INSTALLED OR LOAD IN DATBASE OR NOT

    private void checkLoadFirstTime() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                this);
        boolean checkFirst = prefs.getBoolean("checkFirst", true);
        if (checkFirst) {
            getSupportLoaderManager().restartLoader(0, null, this).forceLoad();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("checkFirst", false);
            editor.commit();

        }
    }


//    private void NewsApiQuery() {
//        String key = "fa0f164779a54f41a9d16d0329bf51af";
//
//        //  String NewsQuery= NewsApiTextView.getText().toString();
//     URL NewsApiSearchURL=NetworkUtils.buildUrl();
//        //  NewsDisplayTextView.setText(NewsApiSearchURL.toString());
//
//        new NewsApiTask().execute();
//
//    }


//    public class NewsApiTask extends AsyncTask<String, Void, ArrayList<NewsItem>> {
//        ArrayList<NewsItem> result;
//
//        @Override
//        protected void onPreExecute() {
//            pb.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected ArrayList<NewsItem> doInBackground(String... params) {
//
//            URL searchUrl = NetworkUtils.buildUrl();
//            ArrayList<NewsItem> result = null;
//
//
//            String NewsApiSearchResults = null;
//            try {
//
//                NewsApiSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
//                result = ParseJSON.parseJSON(NewsApiSearchResults);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(final ArrayList<NewsItem> data) {
//            pb.setVisibility(View.INVISIBLE);
//            errorTextView.setVisibility(View.INVISIBLE);
//            if (data != null) {
//                Adapter adapter = new Adapter(data, new Adapter.ItemClickListener() {
//                    @Override
//                    public void onItemClick(int clickedItemIndex) {
//
//                        String url = data.get(clickedItemIndex).getURL();
//                        openWebPage(url);
//                    }
//                });
//                rv.setAdapter(adapter);
//            } else {
//                showErrorMessgae();
//            }
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();
        if (itemNumber == R.id.action_search) {

            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
        }

//        if (itemNumber == R.id.action_search) {
//            NewsApiTask task = new NewsApiTask();
//            task.execute();
//        } else
//
//        {
//            showErrorMessgae();
//        }
//        return true;
        else {
            showErrorMessgae();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showErrorMessgae() {

        errorTextView.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);


    }

    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    // ADDED ASYNC TASK LOADER REPLACING ASYNC TASK

    @Override
    public Loader<ArrayList<Void>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Void>>(this) {

            ArrayList<NewsItem> output= null;


            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                pb.setVisibility(View.VISIBLE);
            }


            @Override
            public ArrayList<Void> loadInBackground() {

                URL searchurl = NetworkUtils.buildUrl();
                try {
                    String results = NetworkUtils.getResponseFromHttpUrl(searchurl);


                    output = ParseJSON.parseJSON(results);

                    db = new DBHelper(this.getContext()).getWritableDatabase();

                    DBUtils.getNews(db, output);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                return  null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Void>> loader, ArrayList<Void> data) {

        pb.setVisibility(View.INVISIBLE);

        db=  new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DBUtils.getAllNewsCursor(db);
        adapter = new Adapter(cursor, this);
        rv.setAdapter(adapter);

        adapter.notifyDataSetChanged();


    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex) {

        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_NEWS_URL));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
