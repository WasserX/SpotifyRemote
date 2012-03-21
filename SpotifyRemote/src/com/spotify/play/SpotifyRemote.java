package com.spotify.play;

import com.spotify.options.SpotifyOptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SpotifyRemote extends Activity {
    
	// properties you need to update when playing
	private TextView artist;
	private TextView album;
	private TextView song;
	
	private ImageView rewind;
	private ImageView fastForward;
	private ImageView playPause;
		
	// allow to know if the app is playing a song (true) or not (false)
	private static boolean PLAY_STATE = false;
	// id of the menu items
	public static final int OPTIONS_ID = Menu.FIRST;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);
        
        artist = (TextView) findViewById(R.id.artist_value);
        album = (TextView) findViewById(R.id.album_value);
        song = (TextView) findViewById(R.id.title_value);
        
        rewind = (ImageView) findViewById(R.id.rewind);
        fastForward = (ImageView) findViewById(R.id.fast_forward);
        playPause = (ImageView) findViewById(R.id.play_pause);
        
        // adding listener to image
        rewind.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SpotifyRemote.this.onRewind();
				
			}
		});
        
        fastForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SpotifyRemote.this.onFastForward();
				
			}
		});
        
        playPause.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SpotifyRemote.this.onPlayPause();
			}
		});
    }

    // add items to the menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, OPTIONS_ID, 0, R.string.options);
		// add other menu items
		return super.onCreateOptionsMenu(menu);
	}
	
	// actions to do when options (or other items in the menu is pressed)
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){
    	case OPTIONS_ID:
    		// calling the new Activity
    		Options();
    		return true;
    	}
        return super.onOptionsItemSelected(item);
    }
	
	// actions to do when user clicks on rewind
	private void onRewind(){
		// update info about song
		artist.setText("new artist"); // doesn't change if same album
		album.setText("new album"); // doesn't change if same album
		song.setText("new song");
		
		// other actions
		/*.......*/
	}
	
	// actions to do when user clicks on fastforward
	private void onFastForward(){
		// update info about song
		artist.setText("new artist"); // doesn't change if same album
		album.setText("new album"); // doesn't change if same album
		song.setText("new song");
		
		// other actions
		/*.......*/
	}
	
	// actions to do when user clicks on play/pause
	private void onPlayPause(){
		// the user clicks on pause
		if(SpotifyRemote.PLAY_STATE){
			playPause.setImageResource(R.drawable.play_white);
			SpotifyRemote.currentState(false);
			//other actions
			/*..........*/
		}
		// the user clicks on play
		else {
			playPause.setImageResource(R.drawable.pause_white);
			SpotifyRemote.currentState(true);
			// other actions
			/*..........*/
		}
	}
	
	// set the player state (playing or not)
	private static void currentState(boolean currentState){
		SpotifyRemote.PLAY_STATE = currentState;
	}
	
	// Calling the new Activity to get the options screen
	private void Options(){
		Intent i = new Intent(this, SpotifyOptions.class);
		startActivityForResult(i, 0);
	}
}