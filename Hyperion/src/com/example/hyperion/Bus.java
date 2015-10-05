package com.example.hyperion;

public class Bus
{
	private final PowerComponent powerComponent = new PowerComponent ();

	/**
	 * Public getter for power component.
	 * @return - instance of power component.
	 */
	public PowerComponent getPowerComponent() {
		return powerComponent;
	}
}
