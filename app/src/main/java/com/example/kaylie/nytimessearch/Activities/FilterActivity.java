package com.example.kaylie.nytimessearch.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kaylie.nytimessearch.DatePickerFragment;
import com.example.kaylie.nytimessearch.R;
import com.example.kaylie.nytimessearch.models.SearchFilters;

import java.util.ArrayList;
import java.util.Calendar;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    SearchFilters filters;
    private String begin_date;
    private ArrayList<String> news_desk;
    private String sort_criteria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
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
        this.begin_date = "" + year + "" + monthOfYear + "" + dayOfMonth;

    }


    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.cbDate:
                if (checked)
                    filters.setBegin_date(begin_date);
                else
                    filters.setBegin_date("");
                break;
            case R.id.cbSort:
                if (checked)
                    filters.setSort_criteria(view.getId(spSort));
                else
                // I'm lactose intolerant
                break;
        }
    }

    // ActivityNamePrompt.java -- launched for a result
    public void onSubmit(View v) {
        // Prepare data intent
        Intent data = new Intent();
        data.putExtra("filters", filters);

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); // closes the activity, pass data to parent
    }



}
