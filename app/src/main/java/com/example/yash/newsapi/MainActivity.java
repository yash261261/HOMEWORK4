package com.example.yash.newsapi;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //  private TextView NewsApiTextView;

    private TextView errorTextView;
    ProgressBar pb;
    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // NewsApiTextView = (TextView) findViewById(R.id.news_data_search);

        rv = (RecyclerView) findViewById(R.id.rv1);
        errorTextView = (TextView) findViewById(R.id.error_message);
        pb = (ProgressBar) findViewById(R.id.progressbar);
        rv.setLayoutManager(new LinearLayoutManager(this));


    }


    private void NewsApiQuery() {
        String key = "fa0f164779a54f41a9d16d0329bf51af";

        //  String NewsQuery= NewsApiTextView.getText().toString();
//        URL NewsApiSearchURL=NetworkUtils.buildUrl();
        //  NewsDisplayTextView.setText(NewsApiSearchURL.toString());

        new NewsApiTask().execute();

    }


    public class NewsApiTask extends AsyncTask<String, Void, ArrayList<NewsItem>> {
        ArrayList<NewsItem> result;

        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected ArrayList<NewsItem> doInBackground(String... params) {

            URL searchUrl = NetworkUtils.buildUrl();
            ArrayList<NewsItem> result = null;


            //URL Search = NetworkUtils.buildUrl(searchUrl);
            String NewsApiSearchResults = null;
            try {

                NewsApiSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                result = ParseJSON.parseJSON(NewsApiSearchResults);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return result;
        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> data) {
            pb.setVisibility(View.INVISIBLE);
            errorTextView.setVisibility(View.INVISIBLE);
            if (data != null) {
                Adapter adapter = new Adapter(data, new Adapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int clickedItemIndex) {

                        String url = data.get(clickedItemIndex).getURL();
                        //   Log.d(TAG, String.format("Url %s", url));
                        openWebPage(url);
                    }
                });
                rv.setAdapter(adapter);
            } else {
                showErrorMessgae();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        if (itemNumber == R.id.action_search) {
            NewsApiTask task = new NewsApiTask();
            task.execute();
        } else

        {
            showErrorMessgae();
        }
        return true;
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
}
