package edu.cmu.ds.project4android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
// Project4 Android App - "Main" - Basically the main process for this android app
// Dan Molenhouse - dmolenho
// 04.10.2022
// Based on the Interesting Picture android lab code & online resources as cited

public class itunesMain extends AppCompatActivity {

    //based on the interesting picture android app presented to us in class
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //bundle information https://developer.android.com/guide/app-bundle
        //show search bar
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        //show submit button
        final itunesMain tvMain = this;
        Button submitButton = findViewById(R.id.submit);

        //Submit button listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                String searchTerm = ((EditText) findViewById(R.id.searchTerm)).getText().toString();
                itunesGet getter = new itunesGet();
                getter.search(searchTerm, tvMain);
            }
        });
    }

    //arrangeViews just sets up all the views that exist in the "results" screen of the android
    // application, so the listView, and two textViews
    public void arrangeViews(int resultCount, ArrayList tvShowList) {

        ListView listView = findViewById(R.id.tvShowList);
        TextView searchView = findViewById(R.id.searchTerm);
        TextView resultView = findViewById(R.id.result);

        if (resultCount > 0) {
            //Show view if results exist
            listView.setAdapter(new tvShowList(getApplicationContext(), R.layout.activity_item, tvShowList));
            listView.setVisibility(View.VISIBLE);
            resultView.setText("TV Show results for " + searchView.getText().toString());
        } else {
            //dont show listView in the case of no results
            listView.setVisibility(View.INVISIBLE);
            resultView.setText("No results for " + searchView.getText().toString());
        }
        searchView.setText(""); //reset search box
        listView.invalidate();
    }
}
