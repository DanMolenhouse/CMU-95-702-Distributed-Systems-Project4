package edu.cmu.ds.project4android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

// Project4 Android App - tvShowList is the list object of TV Shows
// Dan Molenhouse - dmolenho
// 04.10.2022
// Based on the Interesting Picture android lab code & online resources as cited

//ArrayAdapters information: https://developer.android.com/reference/android/widget/ArrayAdapter (android documentation)
// used to help assemble this class
public class tvShowList extends ArrayAdapter<tvShow> {

    //Variables
    Context context;
    int resource;
    ArrayList<tvShow> showList;

    //Simple constructor
    public tvShowList(Context context, int resource, ArrayList<tvShow> showList) {
        super(context, resource, showList);
        this.context = context;
        this.resource = resource;
        this.showList = showList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //LayoutInflator taken from https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
        // and Android documentation
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.activity_item, null, true);
        }


        final tvShow show = getItem(position);

        //Setup view of each item in list
        TextView artistText = (TextView) convertView.findViewById(R.id.artistTextView);
        artistText.setText(show.getArtist());

        TextView trackText = (TextView) convertView.findViewById(R.id.trackTextView);
        trackText.setText(show.getTrack());

        TextView genreText = (TextView) convertView.findViewById(R.id.genreTextView);
        genreText.setText(show.getGenre());

        ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnailImageView);
        thumbnail.setImageBitmap(show.getThumbnail());

        return convertView;
    }
}
