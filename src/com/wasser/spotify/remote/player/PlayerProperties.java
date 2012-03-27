package com.wasser.spotify.remote.player;

/**
 * A combination of all properties relative to the player It has everything that
 * a player needs to know about its state and of the track it's playing.
 * 
 * @author wasser
 * 
 */
public class PlayerProperties {

	private Status status = null;
	private Track track = null;

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	@Override
	public String toString() {
		return "State [status=" + status + ", track=" + track + "]";
	}

}
