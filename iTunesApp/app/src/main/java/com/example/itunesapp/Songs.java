package com.example.itunesapp;

import java.io.Serializable;

public class Songs implements Serializable {

    String trackName;
    String price_t;
    String artistName;
    String date;
    String genre;
    String album;
    String price_a;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Songs() {
    }


    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPrice_a() {
        return price_a;
    }

    public void setPrice_a(String price_a) {
        this.price_a = price_a;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getPrice() {
        return price_t;
    }

    public void setPrice(String price_t) {
        this.price_t = price_t;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Songs{" +
                "trackName='" + trackName + '\'' +
                ", price_t='" + price_t + '\'' +
                ", artistName='" + artistName + '\'' +
                ", date='" + date + '\'' +
                ", genre='" + genre + '\'' +
                ", album='" + album + '\'' +
                ", price_a='" + price_a + '\'' +
                '}';
    }


}
