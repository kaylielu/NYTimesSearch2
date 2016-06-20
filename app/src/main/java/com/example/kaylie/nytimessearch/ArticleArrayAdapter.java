package com.example.kaylie.nytimessearch;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by kaylie on 6/20/16.
 */
public class ArticleArrayAdapter extends ArrayAdapter {

    public ArticleArrayAdapter(Context context, List<Article> articles){
        super(context, android.R.layout.simple_list_item_1, articles);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        // get the data item for posiiton
        Article article = (Article)this.getItem(position);

        //check to see if existing view being reused
        //not using a recycled view -> inflate the layout

        if (convertView == null){

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView  = inflater.inflate(R.layout.item_article_result,parent, false );
        }
        // find the image view
        ImageView imageView = (ImageView)convertView.findViewById(R.id.ivImage);

        // clear out recycled image from convertView from last time
        imageView.setImageResource(0);

        TextView tvTitle = (TextView)convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(article.getHeadline());

        //populate the thumbnail image
        String thumbnail = article.getThumbnail();
        if(!TextUtils.isEmpty(thumbnail)){
            Picasso.with(getContext()).load(thumbnail).into(imageView);
        }

        return convertView;
        //remotely download image in the background



    }
}
