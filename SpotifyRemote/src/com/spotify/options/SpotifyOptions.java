package com.spotify.options;

import com.spotify.play.R;
import android.app.Activity;
import android.os.Bundle;

public class SpotifyOptions extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);
		setTitle(R.string.optionstitle);
	}

}
