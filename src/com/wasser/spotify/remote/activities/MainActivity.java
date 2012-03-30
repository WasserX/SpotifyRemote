package com.wasser.spotify.remote.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wasser.spotify.remote.R;
import com.wasser.spotify.remote.connection.SocketConnection;
import com.wasser.spotify.remote.player.Commands;
import com.wasser.spotify.remote.player.PlayerProperties;
import com.wasser.spotify.remote.player.Status;
import com.wasser.spotify.remote.player.Track;

public class MainActivity extends PlayerActivity implements OnClickListener {

	private static int REQ_CODE_PREF = 0;

	private Status currentStatus = Status.STOPPED;
	SocketConnection socket = SocketConnection.getClient();
	private int retries = 0; // How many times have we failed to connect

	// Fields with Track info
	private TextView artistField;
	private TextView albumField;
	private TextView titleField;
	private ImageView coverArt;

	// Controls
	private ImageView prevButton;
	private ImageView nextButton;
	private ImageView playPauseButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);

		// Selecting fonts
		Typeface tfFixedText = Typeface.createFromAsset(getAssets(),
				"fonts/GOODTIME.TTF");
		Typeface tfText = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Regular.ttf");

		artistField = (TextView) findViewById(R.id.artist_value);
		albumField = (TextView) findViewById(R.id.album_value);
		titleField = (TextView) findViewById(R.id.title_value);
		coverArt = (ImageView) findViewById(R.id.cover);

		artistField.setTypeface(tfText);
		albumField.setTypeface(tfText);
		titleField.setTypeface(tfText);

		// Labels
		TextView artistLabel = (TextView) findViewById(R.id.artist);
		TextView albumLabel = (TextView) findViewById(R.id.album);
		TextView titleLabel = (TextView) findViewById(R.id.title);

		artistLabel.setTypeface(tfFixedText);
		albumLabel.setTypeface(tfFixedText);
		titleLabel.setTypeface(tfFixedText);

		// Button Configuration
		prevButton = (ImageView) findViewById(R.id.rewind);
		nextButton = (ImageView) findViewById(R.id.fast_forward);
		playPauseButton = (ImageView) findViewById(R.id.play_pause);

		prevButton.setOnClickListener(this);
		nextButton.setOnClickListener(this);
		playPauseButton.setOnClickListener(this);
		
		//Connect to server and starts the data transferrer
		new ConnectToServer().execute();
	}

	@Override
	public void changeState(PlayerProperties state) {
		if (state != null) {
			if (state.getStatus() != null)
				setStatus(state.getStatus());
			if (state.getStatus() == Status.STOPPED)
				setMetadata(new Track());
			else if (state.getTrack() != null)
				setMetadata(state.getTrack());
		}
	}

	public void setMetadata(Track track) {
		if (track == null) {
			track = new Track();
		}
		titleField.setText(track.getTitle());
		artistField.setText(track.getArtist());
		albumField.setText(track.getAlbum());
		if (track.getCoverArt() != null)
			coverArt.setImageBitmap(track.getCoverArt());
	}

	public void setStatus(Status newStatus) {
		if (newStatus == null)
			newStatus = Status.STOPPED;
		if (newStatus == Status.PLAYING) {
			playPauseButton.setImageResource(R.drawable.pause);
		} else {
			playPauseButton.setImageResource(R.drawable.play);
		}
		this.currentStatus = newStatus;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.play_pause:
			if (currentStatus == Status.PLAYING)
				socket.sendCommand(Commands.PAUSE);
			else
				socket.sendCommand(Commands.PLAY);
			break;
		case R.id.rewind:
			socket.sendCommand(Commands.PREV);
			break;
		case R.id.fast_forward:
			socket.sendCommand(Commands.NEXT);
			break;
		default:
			System.out.println("Command not recognized");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQ_CODE_PREF) {
			if (data.hasExtra(PreferenceActivity.RETRIES)) {
				retries = data.getIntExtra(PreferenceActivity.RETRIES, retries);
			}
			new ConnectToServer().execute(); //Connect to server and start data transferrer
		}
	}

	/**
	 * Gets States from the server asynchronously during the whole duration of
	 * the activity.
	 * 
	 * @author wasser
	 * 
	 */
	private class DataTransferrer extends
			AsyncTask<Void, PlayerProperties, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			socket.sendCommand(Commands.GET_TRACK);
			while (!this.isCancelled() && !socket.isClosed()) {
				PlayerProperties receivedState = socket.receiveState();
				publishProgress(new PlayerProperties[] { receivedState });
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(PlayerProperties... values) {
			changeState(values[0]);
		}

	}

	private class ConnectToServer extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			if(socket.isClosed())
				socket.connect();
			return !socket.isClosed();
		}

		@Override
		protected void onPostExecute(Boolean connected) {
			super.onPostExecute(connected);
			if (connected) {
				// Receive data from server asynchronously
				new DataTransferrer().execute();

			} else {
				Intent intent = new Intent(getApplicationContext(),
						PreferenceActivity.class);
				intent.putExtra(PreferenceActivity.RETRIES, ++retries);
				startActivityForResult(intent, REQ_CODE_PREF);
			}
		}
	}
}