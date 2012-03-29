package com.wasser.spotify.remote.player;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Track {

	private String title = "";
	private String artist = "";
	private String album = "";
	private Bitmap coverArt = null;
	private int length = 0;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public Bitmap getCoverArt() {
		return this.coverArt;
	}

	public void setCoverArt(String imgUrl) {
		try {
			this.coverArt = BitmapFactory.decodeStream((InputStream) new URL(
					imgUrl).getContent());
		} catch (MalformedURLException e) {
			// If the url is not well formatted, ignore it
			this.coverArt = null;
		} catch (IOException e) {
			// If the image couldn't be retrieved, ignore it
			this.coverArt = null;
		}
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "Track [title=" + title + ", artist=" + artist + ", album="
				+ album + ", length=" + length + "]";
	}

}
