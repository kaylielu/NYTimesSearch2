package com.example.kaylie.nytimessearch.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by kaylie on 6/22/16.
 */
public class SearchFilters implements Parcelable{

    private String begin_date;
    private ArrayList<String> news_desk;
    private String sort_criteria;

    public SearchFilters(Parcel in) {
        begin_date = in.readString();
        news_desk = in.createStringArrayList();
        sort_criteria = in.readString();
    }

    public SearchFilters(){

        news_desk = new ArrayList<String>();

    }

    public static final Creator<SearchFilters> CREATOR = new Creator<SearchFilters>() {
        @Override
        public SearchFilters createFromParcel(Parcel in) {
            return new SearchFilters(in);
        }

        @Override
        public SearchFilters[] newArray(int size) {
            return new SearchFilters[size];
        }
    };

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
        Log.d("DEBUG", "the date is " + this.begin_date);
    }

    public ArrayList<String> getNews_desk() {
        return news_desk;
    }

    public void addNews_desk_item(String item){
        news_desk.add(item);
    }
    public void setNews_desk(ArrayList<String> news_desk) {
        this.news_desk = news_desk;
    }

    public String getSort_criteria() {
        return sort_criteria;
    }

    public void setSort_criteria(String sort_criteria) {
        this.sort_criteria = sort_criteria;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // This is where you write the values you want to save to the `Parcel`.
    // The `Parcel` class has methods defined to help you save all of your values.
    // Note that there are only methods defined for simple values, lists, and other Parcelable objects.
    // You may need to make several classes Parcelable to send the data you want.
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(begin_date);
        out.writeStringArray((String[])news_desk.toArray());
        out.writeString(sort_criteria);
    }



}
