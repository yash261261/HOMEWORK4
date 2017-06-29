package com.example.yash.newsapi;

/**
 * Created by Yash on 6/26/17.
 */

public class NewsItem {



        private String Author;
        private String title;
        private String Description;
        private String URL;
    private String URLtoImge;
     private String PublishedAt;


        public NewsItem(String Author, String title, String Description, String URL, String URLtoImge, String PublishedAt){
          //  this.Author = Author;
            this.title = title;
            this.Description = Description;
            this.URL = URL;
       //     this.URLtoImge = URLtoImge;
            this.PublishedAt = PublishedAt;

        }

    public String getAuthor() {

        return Author;
    }

    public void setAuthor(String author)
    {
        Author = author;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURLtoImge() {
        return URLtoImge;
    }

    public void setURLtoImge(String URLtoImge) {
        this.URLtoImge = URLtoImge;
    }

    public String getPublishedAt() {
        return PublishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        PublishedAt = publishedAt;
    }
}
