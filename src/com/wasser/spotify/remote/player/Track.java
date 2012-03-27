package com.wasser.spotify.remote.player;

import java.net.MalformedURLException;
import java.net.URL;

public class Track {

	private String title = "";
	private String artist = "";
	private String album = "";
	private URL imgUrl = null;
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

	public URL getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		try {
			this.imgUrl = new URL(imgUrl);
		} catch (MalformedURLException e) {
			// If the url is not well formatted, ignore it
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
				+ album + ", imgUrl=" + imgUrl + ", length=" + length + "]";
	}

}
