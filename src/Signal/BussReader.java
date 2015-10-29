package com.example.hyperion.Signal;

import android.os.AsyncTask;
import android.util.Log;

import com.example.hyperion.Signal.SignalType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

/**
 * BussReader is responsible for connecting to the buss, reading the busses signals and updating the eventQueue based on the signals
 *
 * @author Anton Andrén
 * @version 2.0
 * @since 2015-09-27
 */

public class BussReader extends AsyncTask<LinkedList, Void, Void>
{
	private boolean stopFlag = false;
	private boolean doorFlag = false;
	private boolean indicatorFlag = false;
	private LinkedList<SignalType> eventQueue = new LinkedList<>();
	private String key = "Z3JwNTU6aGROZzhUaU5VbA==";
	private static final String TAG = "BussReader";

	@Override
	protected Void doInBackground(LinkedList... params) {
		this.eventQueue = params[0];
		try {
			checkStopState();
			checkDoorState();
			checkIndicatorState();
		} catch (IOException e) {
			Log.i(TAG, "IOException thrown at check...: "+  e.toString());
		} catch (JSONException e) {
			Log.i(TAG, "JSONException thrown at check...: " + e.toString());
		}
		return null;
	}
	
	/**
	 * Opens a connection the the door sensor then reads and updates the eventQueue accordingly before closing the connection.
	 *
	 * @throws IOException
	 * @throws JSONException
	 */
	private void checkDoorState() throws IOException, JSONException {
		boolean currentDoorValue = false;
		long t2 = System.currentTimeMillis();
		long t1 = t2 - (1000 * 120);

		// Prepare the URL and create the Http connection.
		String doorUrl = new String("https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Opendoor&t1=" + t1 + "&t2=" + t2);
		URL doorRequestURL = new URL(doorUrl);
		HttpURLConnection doorCon = (HttpURLConnection) doorRequestURL.openConnection();

		// Modify the Http connection
		doorCon.setRequestMethod("GET");
		doorCon.setRequestProperty("Authorization", "Basic " + key);

		// Run the connection and store the response code
		int responseCode = doorCon.getResponseCode();

		Log.i(TAG, "Door response code: " + responseCode);

		// download data, if the doors has open since last exec add to the eventQueue.
		if (responseCode == 200) {
			// download data, if the stop has been pressed since last exec add to the eventQueue.
			if (responseCode == 200) {
				BufferedReader doorReader = doorReader = new BufferedReader(new InputStreamReader(doorCon.getInputStream()));
				String s = doorReader.readLine();

				// reading is done for now
				doorReader.close();

				// the char at 70 is either [t]rue or [f]alse. This avoids JSON parsing.
				if (s.charAt(70) == 't') {
					currentDoorValue = true;
				}
				if (s.charAt(70) == 'f') {
					currentDoorValue = false;
				}

				Log.i(TAG, "Char checked in door: " + s.charAt(70));

				// determine if the state of the door has changed
				if (currentDoorValue && (!doorFlag)) {
					Log.i(TAG, "Adding door to eq");
					eventQueue.add(SignalType.DOOR);
					return;
				}

				if (!currentDoorValue && doorFlag) {
					doorFlag = false;
				}
			}
		}
	}

	/**
	 * Opens a connection to the indecator sensor and updates the eventQueue accordingly before closing the connection.
	 *
	 * @throws IOException
	 * @throws JSONException
	 */
	private void checkIndicatorState() throws IOException, JSONException {
		boolean currentIndicatorValue = false;
		long t2 = System.currentTimeMillis();
		long t1 = t2 - (1000 * 120);

		// Prepare the URL and create the Http connection.
		String indicatorUrl = new String ("https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Turn_Signals&t1=" + t1 + "&t2=" + t2);
		URL indicatorRequestURL = new URL(indicatorUrl);
		HttpURLConnection indicatorCon = (HttpURLConnection) indicatorRequestURL.openConnection();

		// Modify the Http connection
		indicatorCon.setRequestMethod("GET");
		indicatorCon.setRequestProperty("Authorization", "Basic " + key);

		// Run the connection and store the response code
		int responseCode = indicatorCon.getResponseCode();

		Log.i(TAG, "indicator response code: " + responseCode);

		// download data, if the Indicators has been turned on since last exec add to the eventQueue.
		if (responseCode == 200) {
			BufferedReader indicatorReader = null;
			indicatorReader = new BufferedReader(new InputStreamReader(indicatorCon.getInputStream()));
			String s = indicatorReader.readLine();

			// Reading is done for now
			indicatorReader.close();

			// the char at 73, 74, 75 is either 0 or 1
			if(s.charAt(73) == '0' && s.charAt(74) == '1' && s.charAt(75) == '1'){
				currentIndicatorValue = true;
			}
			if(s.charAt(73) == '0' && s.charAt(74) == '0' && s.charAt(75) == '0'){
				currentIndicatorValue = false;
			}

			// determine if the state of the stop indicator has changed
			if (currentIndicatorValue && (!indicatorFlag)) {
				Log.i(TAG, "Adding indicator to eq");
				eventQueue.add(SignalType.INDICATOR);
				indicatorFlag = true;
				return;
			}

			if (!currentIndicatorValue && stopFlag) {
				indicatorFlag = false;
			}
		}
	}
	
	/**
	 * Opens a connection to the stop sensor then reads and updates the eventQueue accordingly before closing the connection
	 *
	 * @throws IOException
	 * @throws JSONException
	 */
	private void checkStopState() throws IOException, JSONException {
		boolean currentStopValue = false;
		long t2 = System.currentTimeMillis();
		long t1 = t2 - (1000 * 120);

		// Prepare the URL and create the Http connection.
		String stopUrl = new String("https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Stop_Pressed&t1=" + t1 + "&t2=" + t2);
		URL stopRequestURL = new URL(stopUrl);
		HttpURLConnection stopCon = (HttpURLConnection) stopRequestURL.openConnection();

		// Modify the Http connection
		stopCon.setRequestMethod("GET");
		stopCon.setRequestProperty("Authorization", "Basic " + key);

		// Run the connection and store the response code
		int responseCode = responseCode = stopCon.getResponseCode();

		Log.i(TAG, "stop response code: " + responseCode);

		// download data, if the stop has been pressed since last exec add to the eventQueue.
		if (responseCode == 200) {
			BufferedReader stopReader = null;
			stopReader = new BufferedReader(new InputStreamReader(stopCon.getInputStream()));
			String s = stopReader.readLine();

			//reading is done for now
			stopReader.close();

			// the char at 73 is either [t]rue or [f]alse. This avoids JSON parsing.
			if(s.charAt(73) == 't'){
				currentStopValue = true;
			}
			if(s.charAt(73) == 'f'){
				currentStopValue = false;
			}

			Log.i(TAG, "Char checked in stop: " + s.charAt(73));

			// determine if the state of the stop indicator has changed
			if (currentStopValue && (!stopFlag)) {
				Log.i(TAG, "Adding stop to eq");
				eventQueue.add(SignalType.STOP);
				stopFlag = true;
				return;
			}

			if (!currentStopValue && stopFlag) {
				stopFlag = false;
			}
		}
	}
}