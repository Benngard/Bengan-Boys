package com.example.hyperion;

import com.example.hyprion.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Playfield Fragment, initializes GUI for Playfield; Left- Right, Fire Button.
 * 
 * @author 		Mattias Benngard
 * @version		1.0
 * @since		2015-09-30
 */

public class Playfield extends Fragment
{
	private Bus bus = new Bus ();
	
	private static final int BUTTON_LEFT = R.id.button1;
	private static Button buttonLeft;
	
	private static final int BUTTON_RIGHT = R.id.button1;
	private static Button buttonRight;
	
	private final static int BUTTON_FIRE = R.id.button1;
	private static Button buttonFire;	
	
	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		
		container.addView(bus.getPowerComponent().getView(getActivity()));
		
		final View view = inflater.inflate(R.layout.fragment_playfield, container, false);
				
		buttonLeft = (Button) view.findViewById(BUTTON_LEFT);
		buttonLeft.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				left ();
			}
		});
		
		buttonRight = (Button) view.findViewById(BUTTON_RIGHT);
		buttonRight.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				right ();
			}
		});
		
		buttonFire = (Button) view.findViewById(BUTTON_FIRE);
		buttonFire.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				fire ();
			}
		});
		
		return view;
	}
	
	/**
	 * Public getter for bus.
	 * 
	 * @return - instance of bus.
	 */
	public Bus getBus() {
		return bus;
	}
	
	/**
	 * Action Listener for Fire Button. Tries to fire lightning bolts, if the power is enough for the bus.
	 */
	private void fire () {
		if (bus.getPowerComponent().firePower()) {
			// fire lightning
		}
	}
	
	/**
	 * Action Listener for Left Button. Tries to move left, if there is a lane there.
	 */
	private void left () {
		// move left
	}
	
	/**
	 * Action Listener for Right Button. Tries to move right, if there is a lane there.
	 */
	private void right () {
		// move right
	}	
}