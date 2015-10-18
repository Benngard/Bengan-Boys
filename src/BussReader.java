package com.example.hyperion;


import com.benganboys.example.jsontest.SignalType;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Anton Andrén
 * @version 0.5
 * @since 2015-09-27
 *
 * BussReader
 * BussReader is responsible for connecting to the buss, reading the busses signals
 * and updating the eventQueue based on the signals
 */

public class BussReader 
{
	private boolean stopFlag = false;
	private boolean doorFlag = false;
	private String indicatorFlag = "000";
	private final LinkedList<SignalType> eventQueue = new LinkedList<>();
	private String key = "Z3JwNTU6aGROZzhUaU5VbA==";
	
	public BussReader()  {

	}
	
	/**
	 * Checks all the signals
	 * @throws IOException 
	 */
	public void checkAllSignals () throws IOException, JSONException {
		checkStopState();
		checkDoorState();
		checkIndicatorState();
	}


	/**
	 * Opens a connection the the door sensor then reads and updates the eventQueue accordingly before closing the connection.
	 * @throws IOException
	 * @throws JSONException
	 */
	public void checkDoorState() throws IOException, JSONException {
		boolean currentDoorValue;
		long t2 = System.currentTimeMillis();
		long t1 = t2 - (1000 * 120);
		
		String doorUrl = "https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Opendoor&t1=" + t1 + "&t2=" + t2;
		URL doorRequestURL = new URL(doorUrl);
		HttpsURLConnection doorCon = (HttpsURLConnection) doorRequestURL.openConnection();
		doorCon.setRequestMethod("GET");
		doorCon.setRequestProperty("Authorization", "Basic " + key);
		BufferedReader doorReader = new BufferedReader(new InputStreamReader(doorCon.getInputStream()));
		JSONObject doorJson = new JSONObject(doorReader.readLine());
		currentDoorValue = doorJson.getBoolean("value");
		
		if (currentDoorValue && !doorFlag){
			eventQueue.add(SignalType.DOOR);
			doorReader.close();
			return;
		}
		
		if (!currentDoorValue && doorFlag){
			doorFlag = false;
		}
		
		doorReader.close();
	}
	
	/**
	 * Opens a connection to the indecator sensor and updates the eventQueue accordingly before closing the connection.
	 * @throws IOException
	 * @throws JSONException
	 */
	public void checkIndicatorState() throws IOException, JSONException {
		String currentIndicatorValue;
		long t2 = System.currentTimeMillis();
		long t1 = t2 - (1000 * 120);
		
		String indicatorUrl = "https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Turn_Signals&t1=" + t1 + "&t2=" + t2;
		URL indicatorRequestURL = new URL(indicatorUrl);
		HttpsURLConnection indicatorCon = (HttpsURLConnection) indicatorRequestURL.openConnection();
		indicatorCon.setRequestMethod("GET");
		indicatorCon.setRequestProperty("Authorization", "Basic " + key);
		BufferedReader indicatorReader = new BufferedReader(new InputStreamReader(indicatorCon.getInputStream()));
		JSONObject indicatorJson = new JSONObject(indicatorReader.readLine());
		currentIndicatorValue = indicatorJson.getString("value");
		
		switch (currentIndicatorValue) {
		case ("010"): {
			if(!indicatorFlag.equals(currentIndicatorValue)){
				eventQueue.add(SignalType.INDICATOR);
				indicatorFlag = "010";
				}
			}
		case ("000"): {
			if(!indicatorFlag.equals(currentIndicatorValue)){
				indicatorFlag = "000";
				}
		}
		default: {break;}
		}
		indicatorReader.close();
	}
	
	/**
	 * Opens a connection to the stop sensor then reads and updates the eventQueue accordingly before closing the connection
	 * @throws IOException
	 * @throws JSONException
	 */
	public void checkStopState() throws IOException, JSONException {
		boolean currentStopValue;
		long t2 = System.currentTimeMillis();
		long t1 = t2 - (1000 * 120);
		
		String stopUrl = "https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Stop_Pressed&t1=" + t1 + "&t2=" + t2;
		URL stopRequestURL = new URL(stopUrl);
		HttpsURLConnection stopCon = (HttpsURLConnection) stopRequestURL.openConnection();
		stopCon.setRequestMethod("GET");
		stopCon.setRequestProperty("Authorization", "Basic " + key);
		BufferedReader stopReader = new BufferedReader(new InputStreamReader(stopCon.getInputStream()));
		JSONObject stopJson = new JSONObject(stopReader.readLine());
		currentStopValue = stopJson.getBoolean("value");
		
		if (currentStopValue  && !stopFlag){
			eventQueue.add(SignalType.STOP);
			stopFlag = true;
			stopReader.close();
			return;
		}
		
		if (!currentStopValue && stopFlag){
			stopFlag = false;
			stopReader.close();
		}
	}
	
	public SignalType getEvent () {
		return !eventQueue.isEmpty() ? eventQueue.poll() : SignalType.EMPTY;
	}
}
