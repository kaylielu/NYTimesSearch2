package com.example.kaylie.nytimessearch.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kaylie.nytimessearch.DatePickerFragment;
import com.example.kaylie.nytimessearch.R;
import com.example.kaylie.nytimessearch.models.SearchFilters;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    SearchFilters filters;
    @BindView(R.id.spSort) Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        filters = new SearchFilters();
    }

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        filters.setBegin_date("" + year + "" + monthOfYear + "" + dayOfMonth);

    }


    public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {

        filters.setSort_criteria(parent.getItemAtPosition(pos).toString());

            // An item was selected. You can retrieve the selected item using
            // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        filters.setSort_criteria("Newest");
            // Another interface callback
    }



    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbArt:
                if (checked)
                    filters.addNews_deskItem("Art");
                break;
            case R.id.cbFash:
                if (checked)
                    filters.addNews_deskItem("Fashion & Style");
                break;
            case R.id.cbSports:
                if(checked)
                    filters.addNews_deskItem("Sports");
                break;
        }
    }

    // ActivityNamePrompt.java -- launched for a result
    public void onSubmit(View v) {
        // Prepare data intent
        Intent data = new Intent();
        filters.setSort_criteria(spinner.getSelectedItem().toString());
        data.putExtra("filters", filters);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }



}
