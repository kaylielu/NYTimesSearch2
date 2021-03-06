package com.example.kaylie.nytimessearch.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaylie.nytimessearch.models.Article;
import com.example.kaylie.nytimessearch.ArticleArrayAdapter;
import com.example.kaylie.nytimessearch.EndlessRecyclerViewScrollListener;
import com.example.kaylie.nytimessearch.ItemClickSupport;
import com.example.kaylie.nytimessearch.R;
import com.example.kaylie.nytimessearch.SpacesItemDecoration;
import com.example.kaylie.nytimessearch.models.SearchFilters;
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
import cz.msebera.android.httpclient.util.TextUtils;

public class SearchActivity extends AppCompatActivity {

   @BindView(R.id.rvArticles) RecyclerView rvArticles;
    @BindView(R.id.toolbar_title) TextView toolbar_title;
    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    StaggeredGridLayoutManager gridLayoutManager;
    String query;
    SearchFilters filter;
    boolean topStories;
    @BindView(R.id.toolbar_real_main) Toolbar toolbar;

    SpacesItemDecoration decoration = new SpacesItemDecoration(20);

    private final int REQUEST_CODE = 20;

    /* What happens at the start*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        topStories = true;
        ButterKnife.bind(this);
        // Configure the RecyclerView
        rvArticles = (RecyclerView)findViewById(R.id.rvArticles);
        rvArticles.addItemDecoration(decoration);
        gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvArticles.setLayoutManager(gridLayoutManager);
      // Add the scroll listener
      rvArticles.addOnScrollListener(new EndlessRecyclerViewScrollListener(gridLayoutManager) {
          @Override
          public void onLoadMore(int page, int totalItemsCount) {

              // Triggered only when new data needs to be appended to the list
              // Add whatever code is needed to append new items to the bottom of the list
                customLoadMoreDataFromApi(page);
          }

      });

        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Oxygen-Bold.ttf");

       toolbar_title.setTypeface(custom_font);
        setUpViews();

    }

    /* Sets up the the different views*/
    public void setUpViews(){

        articles = new ArrayList<>();
        adapter = new ArticleArrayAdapter(this, articles);

        rvArticles.setLayoutManager(gridLayoutManager);
        rvArticles.setAdapter(adapter);


        AsyncHttpClient client = new AsyncHttpClient();
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json?q=trump&api-key=15e8378232bf4f4bad4f5408";

        articles.clear();
        adapter.notifyDataSetChanged();

        ItemClickSupport.addTo(rvArticles).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {

            @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // Create an intent to display the article
                Intent intent = new Intent(getApplicationContext(), ArticleActivity.class);

                //get the article to display
                Article article = articles.get(position);
                // pass in that article into intent
                intent.putExtra("url", article.getWebUrl());
                intent.putExtra("title", article.getHeadline());
                startActivity(intent);
            }
        });

        articleSearch("foobar");



    }


    /* Inflates the menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        Log.d("DEBUG","inflated");
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                topStories = false;
                toolbar_title.setText(query);
                launchFilter();
                setQuery(query);

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }

    public void setQuery(String query){
        this.query = query;
    }

    /* launches filter activity which gets a result*/
    public void launchFilter() {
        Intent intent = new Intent(this, FilterActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /* Gets result of filter activity*/
    // ActivityOne.java, time to handle the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            filter = data.getParcelableExtra("filters");
            String date = filter.getBegin_date();
            String news_desk = filter.getNews_desk();
            String sort = filter.getSort_criteria();

            articleSearch(query);


        }
    }

    /* Searches for an article for the first time*/
    public void articleSearch(String query){


        articles.clear();
        adapter.notifyDataSetChanged();
        this.query = query;
        AsyncHttpClient client = new AsyncHttpClient();
        String url;
        RequestParams params = new RequestParams();

        if(topStories){

            url = "https://api.nytimes.com/svc/topstories/v2/home.json";

            Article.topStories = true;
            params.put("api-key", "15e8378232bf4f4bad4f54081a151b80");
            params.put("callback", "callbackTopStories");
        }else {

            url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
            Article.topStories = false;
            if (!TextUtils.isEmpty(filter.getBegin_date())) {
                params.put("begin_date", filter.getBegin_date());
            }
            params.put("api-key", "15e8378232bf4f4bad4f54081a151b80");
            params.put("page", 0);
            params.put("q", query);
            String newsDeskParamValue =
                    String.format("news_desk:(%s)", filter.getNews_desk());
            if (!TextUtils.isEmpty(filter.getNews_desk())) {
                params.put("fq", newsDeskParamValue);
            }
            params.put("sort", filter.getSort_criteria());
        }
        articles.clear();
        adapter.notifyDataSetChanged();

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                JSONArray articleJsonResults = null;
                if(topStories){
                    try{
                        articleJsonResults = response.getJSONArray("results");
                        articles.addAll(Article.fromJSONarray(articleJsonResults));
                        adapter.notifyDataSetChanged();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }else {


                    try {

                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        //Log.d("DEBUG", articleJsonResults.toString());
                        articles.addAll(Article.fromJSONarray(articleJsonResults));
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    // Append more data into the adapter
    // This method probably ends out a network request and appends new data items to your adapter.
    public void customLoadMoreDataFromApi(int offset) {
        // Send an API request to retrieve appropriate data using the offset value as a parameter.
        // Deserialize API response and then construct new objects to append to the adapter
        // Add the new objects to the data source for the adapter
        //String query = etQuery.getText().toString();
        AsyncHttpClient client = new AsyncHttpClient();
        String url;
        RequestParams params = new RequestParams();
        params.put("api-key","15e8378232bf4f4bad4f54081a151b80");
        params.put("page", offset);
        params.put("q", this.query);

        if(topStories){

            url = "https://api.nytimes.com/svc/topstories/v2/home.json";

            Article.topStories = true;

            params.put("callback", "callbackTopStories");
        }else {

            url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

            if (!TextUtils.isEmpty(filter.getBegin_date())) {
                params.put("begin_date", filter.getBegin_date());
            }

            String newsDeskParamValue =
                    String.format("news_desk:(%s)", filter.getNews_desk());
            if (!TextUtils.isEmpty(filter.getNews_desk())) {
                params.put("fq", newsDeskParamValue);
            }
            params.put("sort", filter.getSort_criteria());
        }

        Log.d("search_activity", url + "?" + params);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                JSONArray articleJsonResults = null;

                try{

                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    articles.addAll(Article.fromJSONarray(articleJsonResults));

                    adapter.notifyDataSetChanged();

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                throwable.printStackTrace();

            }
        });
    }
}
