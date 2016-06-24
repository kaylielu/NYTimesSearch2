package com.example.kaylie.nytimessearch.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kaylie.nytimessearch.DatePickerFragment;
import com.example.kaylie.nytimessearch.R;
import com.example.kaylie.nytimessearch.models.SearchFilters;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    SearchFilters filters;
    @BindView(R.id.spSort) Spinner spinner;
    @BindView(R.id.tvDate) TextView date;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        filters = new SearchFilters();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        //newFragment.setStyle(DatePickerFragment.STYLE_NORMAL, );
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date.setText("");
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        monthOfYear++;
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if ((monthOfYear < 10) && (dayOfMonth < 10)) {
            filters.setBegin_date("" + year + "0" + monthOfYear + "0" + dayOfMonth);

        } else if (monthOfYear < 10) {
            filters.setBegin_date("" + year + "0" + monthOfYear + "" + dayOfMonth);

        } else if (dayOfMonth < 10) {
            filters.setBegin_date("" + year + "" + monthOfYear + "0" + dayOfMonth);
        } else {
            filters.setBegin_date("" + year + "" + monthOfYear + "" + dayOfMonth);

        }

        date.setText(monthOfYear + "/" + dayOfMonth + "/" + year );




    }


    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        filters.setSort_criteria(parent.getItemAtPosition(pos).toString());

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        filters.setSort_criteria("newest");
        // Another interface callback
    }


    public void onCheckboxClicked(View view) {
    }

    // ActivityNamePrompt.java -- launched for a result
    public void onSubmit(View v) {
        // Prepare data intent

        CheckBox art = (CheckBox) findViewById(R.id.cbArt);
        if (art.isChecked()) {
            filters.addNews_deskItem("Art");
        }
        CheckBox fash = (CheckBox) findViewById(R.id.cbFash);
        if (fash.isChecked()) {
            filters.addNews_deskItem("Fashion & Style");
        }

        CheckBox sports = (CheckBox) findViewById(R.id.cbSports);
        if (sports.isChecked()) {
            filters.addNews_deskItem("Sports");
        }

        CheckBox education = (CheckBox) findViewById(R.id.cbEducation);
        if (education.isChecked()) {
            filters.addNews_deskItem("Education");
        }

        CheckBox health = (CheckBox) findViewById(R.id.cbHealth);
        if (health.isChecked()) {
            filters.addNews_deskItem("Health");
        }

        CheckBox politics = (CheckBox) findViewById(R.id.cbPolitics);
        if (politics.isChecked()) {
            filters.addNews_deskItem("Politics");
        }

        Intent data = new Intent();
        filters.setSort_criteria(spinner.getSelectedItem().toString());
        data.putExtra("filters", filters);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Filter Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.kaylie.nytimessearch.Activities/http/host/path")
        );
        //AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Filter Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.kaylie.nytimessearch.Activities/http/host/path")
        );
        //AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
