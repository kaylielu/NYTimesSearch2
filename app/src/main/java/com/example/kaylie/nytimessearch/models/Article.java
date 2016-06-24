package com.example.kaylie.nytimessearch.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kaylie on 6/20/16.
 */
public class Article {
    String webUrl;
    String headline;
    String thumbnail;
    public static boolean topStories = true;

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setTopStories(boolean set){
        this.topStories = set;
    }

    public boolean getTopStories(){
        return this.topStories;
    }


    public Article(JSONObject jsonObject){

        if (topStories){
            try{

                this.webUrl = jsonObject.getString("url");
                this.headline = jsonObject.getString("title");
                JSONArray multimedia = jsonObject.getJSONArray("multimedia");
                if(multimedia.length() != 0){
                    JSONObject multimediaJson = multimedia.getJSONObject(3);
                    this.thumbnail = multimediaJson.getString("url");
                }else{
                    this.thumbnail = "";
                }

            }catch( JSONException e){
                e.printStackTrace();
            }
        }else {
            try {

                this.webUrl = jsonObject.getString("web_url");
                this.headline = jsonObject.getJSONObject("headline").getString("main");

                JSONArray multimedia = jsonObject.getJSONArray("multimedia");

                if (multimedia.length() != 0) {
                    JSONObject multimediaJson = multimedia.getJSONObject(0);
                    this.thumbnail = "http://www.nytimes.com/" + multimediaJson.getString("url");

                } else {
                    this.thumbnail = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
    }

    public static ArrayList<Article> fromJSONarray(JSONArray array){
        ArrayList<Article> results = new ArrayList<>();

        for(int x = 0; x < array.length(); x++){
            try{
                results.add(new Article(array.getJSONObject(x)));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }

        return results;
    }
}
