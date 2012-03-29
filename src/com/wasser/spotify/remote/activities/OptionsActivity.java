package com.wasser.spotify.remote.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.wasser.spotify.remote.R;
import com.wasser.spotify.remote.connection.SocketConnection;

public class OptionsActivity extends Activity implements OnClickListener {

	private EditText host;
	private EditText port;
	private SocketConnection socket;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// Get socket to server
		socket = SocketConnection.getClient();
		if (tryConnection(false))
			launchMainActivity();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.options);

		host = (EditText) findViewById(R.id.host_value);
		port = (EditText) findViewById(R.id.port_value);

		// Button Configuration
		Button okButton = (Button) findViewById(R.id.buttonOk);
		Button cancelButton = (Button) findViewById(R.id.buttonCancel);

		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
	}

	private void launchMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	/**
	 * Try to connect to the server and return the state of the connection
	 * 
	 * @param newValues
	 *            - If new values are specified, don't check if there is an open
	 *            connection and connect using new values.
	 * @return true if connected
	 */
	private boolean tryConnection(boolean newValues) {
		if (!newValues || socket.isClosed())
			socket.connect();
		return !socket.isClosed();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void showError() {
		new AlertDialog.Builder(this).setTitle("Error")
				.setMessage(R.string.connFail)
				.setPositiveButton(R.string.accept, null).show();
	}

	@Override
	public void onClick(View v) {
		/**
		 * If ok, and new values try to connect and go to main screen, else show
		 * warning If cancel try to connect with current values and go to main
		 * screen, else close
		 */

		switch (v.getId()) {
		case R.id.buttonOk:
			try {
				socket.setAddress(host.getText().toString());
				socket.setPort(Integer.parseInt(port.getText().toString()));
				if (tryConnection(true))
					launchMainActivity();
				else
					showError();
			} catch (NumberFormatException e) {
				showError();
			}
			break;
		case R.id.buttonCancel:
			finish();
			break;
		}
	}
}
