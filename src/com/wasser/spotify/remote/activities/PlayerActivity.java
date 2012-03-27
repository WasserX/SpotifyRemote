package com.wasser.spotify.remote.activities;

import android.app.Activity;

import com.wasser.spotify.remote.player.PlayerProperties;
import com.wasser.spotify.remote.player.Status;
import com.wasser.spotify.remote.player.Track;

public abstract class PlayerActivity extends Activity {

	/**
	 * Update all the components of the screen, meaning the buttons and the
	 * Metadata
	 * 
	 * @param state
	 */
	public abstract void changeState(PlayerProperties state);

	/**
	 * Should present the information of the track somewhere
	 * 
	 * @param track
	 */
	public abstract void setMetadata(Track track);

	/**
	 * Maybe we need to change some buttons according to the status
	 * 
	 * @param status
	 */
	public abstract void setStatus(Status status);

}
