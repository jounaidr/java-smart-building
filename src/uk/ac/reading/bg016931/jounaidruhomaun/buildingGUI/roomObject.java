package uk.ac.reading.bg016931.jounaidruhomaun.buildingGUI;

import java.awt.Point;

public class roomObject {
	public Point xy; // location of object in room
	protected int currentRoom; // room object is in

	/**
	 * initialise roomObject parameters
	 * 
	 * @param startPoint
	 * @Param room
	 */
	roomObject(Point startPoint, int room) {
		xy = startPoint;
		currentRoom = room;
	}

	/**
	 * generic method (to be inherited) that shows object on BuildingGUI bi
	 * 
	 * @param bi BuildingGUI
	 * @Param b Building
	 */
	public void showObject(BuildingGUI bi, Building b) {

	}

	/**
	 * return info about roomObject as string
	 * 
	 * @return the string
	 */
	public String toString() {
		return "Object at " + (int) xy.getX() + ", " + (int) xy.getY();
	}

}
