package com.example.kaylie.nytimessearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kaylie on 6/20/16.
 */
public class ArticleArrayAdapter extends RecyclerView.Adapter<ArticleArrayAdapter.ViewHolder>{

    //EndlessScrollListener endlessScrollListener;
    List<Article> articles;
    // Store the context for easy access
    private Context mContext;


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivView;
        public TextView tvTitle;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivView = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }



    // Pass in the contact array into the constructor
    public ArticleArrayAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        mContext = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
//
//    public void setEndlessScrollListener(EndlessScrollListener endlessScrollListener) {
//        this.endlessScrollListener = endlessScrollListener;
//    }

    // Inflates a layout from XML
    @Override
    public ArticleArrayAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View articleView = inflater.inflate(R.layout.item_article_result, parent, false);

        //Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(articleView);
        return viewHolder;
    }

    // Populating data into the item through holder
    @Override
    public void onBindViewHolder(ArticleArrayAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        Article currArticle = articles.get(position);
        final int VISIBLE_THRESHOLD = 5;
        //Set item view based on the data model
        TextView textView = viewHolder.tvTitle;
        textView.setText(currArticle.getHeadline());

        ImageView imageView = viewHolder.ivView;

        imageView.setImageResource(0);

        String imageUrl = currArticle.getThumbnail();

        if(!TextUtils.isEmpty(imageUrl)){
            Picasso.with(getContext()).load(imageUrl).into(imageView);
        }


    }


    @Override
    public int getItemCount() {
        return articles.size();
    }

}
