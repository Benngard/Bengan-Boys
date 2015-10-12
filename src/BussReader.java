package signal;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.DatatypeConverter;

/*
 * @author Anton Andrén
 * @version 0.3
 * @since 2015-09-27
 * 
 * BussReader
 * BussReader is responsible for connecting to the buss, reading the busses signals
 * and updating the eventQueue based on the signals
 */

public class BussReader 
{
	
	private DataInputStream stopReader;
	private boolean stopFlag = false;
	private final LinkedList<SignalType> eventQueue = new LinkedList<>();
	
	public BussReader() throws IOException {
		long t2 = System.currentTimeMillis();
		long t1 = t2 - (1000 * 120);

		String key = "grp55:hdNg8TiNUl";
		String url = "https://ece01.ericsson.net:4443/ecity?dgw=Ericsson$Vin_Num_001&sensorSpec=Ericsson$Stop_Pressed&t1=" + t1 + "&t2=" + t2;

		URL requestURL = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) requestURL.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", "Basic " + DatatypeConverter.printBase64Binary(key.getBytes()));

		stopReader = new DataInputStream(con.getInputStream());
	}
	
	/*
	 * Reads the value of the stop button
	 * Returns true if it has been pressed, els false.
	 */
	public Boolean getStopState() throws IOException {
		return new Boolean(stopReader.readBoolean());
	}
	
	/*
	 * Checks if stop has been pressed and if so adds it to the eventQueue
	 * Only adds one entry for every time stop has been pressed
	 */
	public void checkStopState() throws IOException {
		
		boolean currentStopValue;
		
		if (currentStopValue = stopReader.readBoolean() && !stopFlag){
			eventQueue.add(SignalType.STOP);
			return;
		}
		
		if (!currentStopValue && stopFlag){
			stopFlag = false;
		}
	}
	
	/*
	 * Disconnects the inputStream
	 */
	public void disconnect() throws IOException {
		stopReader.close();
	}
	
	public SignalType getEvent () {
		return eventQueue.poll();
	}
}
