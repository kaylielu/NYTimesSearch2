package com.example.kaylie.nytimessearch.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.example.kaylie.nytimessearch.Article;
import com.example.kaylie.nytimessearch.ArticleArrayAdapter;
import com.example.kaylie.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.kaylie.nytimessearch.ItemClickSupport;
import com.example.kaylie.nytimessearch.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

   @BindView(R.id.etQuery) EditText etQuery;
   @BindView(R.id.rvArticles) RecyclerView rvArticles;
   @BindView(R.id.btnSearch) Button btnSearch;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        // Configure the RecyclerView
        rvArticles = (RecyclerView)findViewById(R.id.rvArticles);
//        // Add the scroll listener
//        rvItems.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                customLoadMoreDataForApi(page);
//            }
//        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpViews();

    }

    public void setUpViews(){
        //btnSearch = (Button)findViewById(R.id.btnSearch);
        //gvResults = (GridView)findViewById(R.id.gvResults);
        //etQuery = (EditText) findViewById(R.id.etQuery);
        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);
        rvArticles.setAdapter(adapter);
        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // Create an intent to display the article
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                //get the article to display
                Article article = articles.get(position);
                // pass in that article into intent
                intent.putExtra("url", article.getWebUrl());
                startActivity(intent);
            }
        });

    }

//    //Append more data into the adapter
//    //This method probably sends out a network request and appends new data items to your adapter.
//    public void customLoadMoreDataFromApi(int offset){
//        // Send an API request to retrieve appropriate data using the offset value as a parameter.
//        // Deserialize API response and then construct new objects to append to the adapter
//        // Add the new objects to data source for the adapter.
//        articles.addAll(moreItems);
//        int curSize = adapter.getItemCount();
//        adapter.notifyItemRangeInserted(curSize, items.size()-1);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onArticleSearch(View view) {

        String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();
        params.put("api-key","15e8378232bf4f4bad4f54081a151b80");
        params.put("page", 0);
        params.put("q", query);

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try{

                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    //Log.d("DEBUG", articleJsonResults.toString());
                    articles.addAll(Article.fromJSONarray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", articles.toString());

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });

        //Toast.makeText(this, "Searching for" + query, Toast.LENGTH_LONG).show();
    }
}
