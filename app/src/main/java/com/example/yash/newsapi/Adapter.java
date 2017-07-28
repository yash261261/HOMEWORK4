package com.example.yash.newsapi;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;


import com.example.yash.newsapi.Database.Contract;

import java.util.ArrayList;

/**
 * Created by Yash on 6/28/17.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ItemHolder> {

    private ArrayList<NewsItem> data;
    ItemClickListener listener;
    private Cursor cursor;
    Context context;

    public Adapter(Cursor cursor, ItemClickListener listener) {
       // this.data = data;
        this.listener = listener;
        this.cursor=cursor;
    }

    public interface ItemClickListener {
        void onItemClick(Cursor cursor,int clickedItemIndex);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.list_items, parent, shouldAttachToParentImmediately);
        ItemHolder holder = new ItemHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        holder.bind(holder,position);
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }


    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView description;
        TextView date;
        String url;
        long id;
        String articlename;
        String desc;
        String published;
        ImageView iv;
        String image;

        ItemHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            date = (TextView) view.findViewById(R.id.date);
            iv= (ImageView) view.findViewById(R.id.urlImage);

            view.setOnClickListener(this);
        }

        public void bind(ItemHolder holder,int position) {
            cursor.moveToPosition(position);
            id = cursor.getLong(cursor.getColumnIndex(Contract.TABLE_ARTICLES._ID));
            articlename = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE));
            desc = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION));
            url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_NEWS_URL));
            published = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED));
            image= cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_IMAGE_URL));

            title.setText(articlename);
            description.setText(desc);
            date.setText(published);

            if(image!=null){



                // THUMBNAIL FOR EACH NEWSITEM IN RECYCLER VIEW
                Picasso.with(context).load(image).into(iv);

            }
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            listener.onItemClick(cursor,pos);
        }
    }
}


