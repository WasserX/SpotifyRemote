package com.wasser.spotify.remote.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.wasser.spotify.remote.R;
import com.wasser.spotify.remote.connection.SocketConnection;

public class PreferenceActivity extends android.preference.PreferenceActivity {

	public static String RETRIES = "retries";

	private boolean contentChanged = false;
	private int retries = 0;

	private EditText host;
	private EditText port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		retries = getIntent().getIntExtra(RETRIES, -1);
		retries = retries == -1 ? 0 : retries;
		System.out.println("Retries " + retries);

		addPreferencesFromResource(R.layout.preferences);
		port = ((EditTextPreference) findPreference("port")).getEditText();
		port.addTextChangedListener(textWatcher);
		host = ((EditTextPreference) findPreference("host")).getEditText();
		host.addTextChangedListener(textWatcher);
	}

	@Override
	public void onBackPressed() {
		if (contentChanged) {
			SharedPreferences sharedPrefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			SocketConnection socket = SocketConnection.getClient();
			socket.setAddress(sharedPrefs.getString("host", "192.168.0.100"));
			socket.setPort(Integer.parseInt(sharedPrefs.getString("port",
					"15000")));
		}
		super.onBackPressed();
	}

	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra(RETRIES, retries);
		setResult(RESULT_OK, data);
		super.finish();
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			contentChanged = true;

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable arg0) {

		}
	};
}
