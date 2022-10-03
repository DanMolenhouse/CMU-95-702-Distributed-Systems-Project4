package edu.cmu.ds.project4android;

import android.graphics.Bitmap;

// Project4 Android App - tvShow class
// Allows the storage of important TV Show related variables as an object to be populated into the TV Show List
// Dan Molenhouse - dmolenho
// 04.10.2022
// Based on the Interesting Picture android lab code & online resources as cited

public class tvShow {

    //initializations
    private String artist;
    private String track;
    private String genre;
    private Bitmap thumbnail;

    //Simple constructor
    public tvShow(String artist, String track, String genre, Bitmap thumbnail) {
        this.artist = artist;
        this.track = track;
        this.genre = genre;
        this.thumbnail = thumbnail;

    }

    //Getters for the four important values
    //Setters not allowed because android app doesnt need the ability to alter this info

    //For TV shows, "Artist" refers to the actual TV Show title
    public String getArtist() {
        return artist;
    }

    //Genre is type of show like Comedy or Drama
    public String getGenre() {
        return genre;
    }

    //Track is the episode title
    public String getTrack() {
        return track;
    }

    //Track is the episode title
    public Bitmap getThumbnail() {
        return thumbnail;
    }

}
