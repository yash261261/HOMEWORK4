package com.example.yash.newsapi;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

  //  private TextView NewsApiTextView;

    private TextView NewsDisplayTextView;
    private TextView errorTextView;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // NewsApiTextView = (TextView) findViewById(R.id.news_data_search);

        NewsDisplayTextView = (TextView) findViewById(R.id.news_data_search);
        errorTextView =(TextView) findViewById(R.id.error_message);
        pb= (ProgressBar) findViewById(R.id.progressbar);


    }
   private void NewsApiQuery(){

      //  String NewsQuery= NewsApiTextView.getText().toString();
        URL NewsApiSearchURL=NetworkUtils.buildUrl();
      //  NewsDisplayTextView.setText(NewsApiSearchURL.toString());

        new NewsApiTask().execute(NewsApiSearchURL);

    }

    public class NewsApiTask extends AsyncTask<URL, Void, String>{
        @Override
        protected void onPreExecute() {
            pb.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... params) {

            URL searchUrl= params[0];
            String NewsApiSearchResults=null;
            try{
                NewsApiSearchResults=NetworkUtils.getResponseFromHttpUrl(searchUrl);
            }
            catch (IOException e){
                e.printStackTrace();
            }
                return NewsApiSearchResults;
        }

        @Override
        protected void onPostExecute(String s) {
            pb.setVisibility(View.INVISIBLE);
            errorTextView.setVisibility(View.INVISIBLE);
            NewsDisplayTextView.setVisibility(View.VISIBLE);
            if(s != null && !s.equals("")){
                NewsDisplayTextView.setText(s);
            }
            else{
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
        int ItemID= item.getItemId();
        if (ItemID==R.id.action_search){
            NewsDisplayTextView.setText("");
            NewsApiQuery();
            return true;
        }
        else{
            showErrorMessgae();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showErrorMessgae() {

        errorTextView.setVisibility(View.VISIBLE);
        NewsDisplayTextView.setVisibility(View.INVISIBLE);


    }
}
