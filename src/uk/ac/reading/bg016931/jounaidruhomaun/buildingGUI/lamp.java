package uk.ac.reading.bg016931.jounaidruhomaun.buildingGUI;

import java.awt.Point;

public class lamp extends roomObject {
	/**
	 * initialise lamp parameters
	 * 
	 * @param startPoint
	 * @Param room
	 */
	lamp(Point startPoint, int room) {
		super(startPoint, room);
	}

	/**
	 * Override showObject() method from roomObject to change lamp colour to yellow
	 * if there is currently and occupant in the room
	 * 
	 * @param bi
	 * @Param b
	 */
	@Override
	public void showObject(BuildingGUI bi, Building b) {

		if (b.allRooms.get(currentRoom).isRoomEmpty(b)) {
			bi.showItem((int) xy.getX(), (int) xy.getY(), 6, 'k');
		} else {
			bi.showItem((int) xy.getX(), (int) xy.getY(), 6, 'y');
		}
	}

	/**
	 * return info about lamp as string
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		return "Lamp at " + (int) xy.getX() + ", " + (int) xy.getY();
	}
}
