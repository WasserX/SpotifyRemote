package com.wasser.spotify.remote.connection;

import org.json.JSONException;
import org.json.JSONObject;

import com.wasser.spotify.remote.player.Commands;
import com.wasser.spotify.remote.player.PlayerProperties;
import com.wasser.spotify.remote.player.Status;
import com.wasser.spotify.remote.player.Track;

public class DataParser {

	public static PlayerProperties getStateFromJSON(String json) {
		try {
			return getStateFromJSON(new JSONObject(json));
		} catch (JSONException e) {
			return null;
		}
	}

	public static PlayerProperties getStateFromJSON(JSONObject json) {
		PlayerProperties state = new PlayerProperties();
		Track track = getTrackFromJSON(json);
		Status status = getStatusFromJSON(json);

		if (track != null || status != null) {
			state.setStatus(status);
			state.setTrack(track);
			return state;
		} else {
			return null;
		}
	}

	public static Track getTrackFromJSON(String json) {
		try {
			return getTrackFromJSON(new JSONObject(json));
		} catch (JSONException e) {
			return null;
		}
	}

	public static Track getTrackFromJSON(JSONObject json) {
		if (json.isNull("metadata")) {
			return null;
		}
		Track track = new Track();
		try {
			JSONObject jsonTrack = json.getJSONObject("metadata");
			track.setAlbum(jsonTrack.getString("album"));
			track.setArtist(jsonTrack.getString("artist"));
			track.setImgUrl(jsonTrack.getString("artUrl"));
			track.setTitle(jsonTrack.getString("title"));
			track.setLength(jsonTrack.getInt("length"));
		} catch (JSONException e) {
			return null;
		}
		return track;
	}

	public static Status getStatusFromJSON(String json) {
		try {
			return getStatusFromJSON(new JSONObject(json));
		} catch (JSONException e) {
			return null;
		}
	}

	public static Status getStatusFromJSON(JSONObject json) {
		if (json.isNull("status"))
			return null;
		try {
			String status = json.getString("status");
			if (status.equalsIgnoreCase("PLAYING"))
				return Status.PLAYING;
			else if (status.equalsIgnoreCase("PAUSED"))
				return Status.PAUSED;
			else
				return Status.STOPPED;
		} catch (JSONException e) {
			return null;
		}
	}

	public static String getJSONCommand(Commands command) {
		JSONObject json = new JSONObject();
		try {
			switch (command) {
			case PLAY:
				json.put("command", "PLAY");
				break;
			case PAUSE:
				json.put("command", "PAUSE");
				break;
			case PREV:
				json.put("command", "PREV");
				break;
			case NEXT:
				json.put("command", "NEXT");
				break;
			case VOLUP:
				json.put("command", "VOLUP");
				break;
			case VOLDOWN:
				json.put("command", "VOLDOWN");
				break;
			case GET_TRACK:
				json.put("command", "GET_TRACK");
				break;
			}
		} catch (JSONException e) {
			return null;
		}
		return json.toString();
	}
}