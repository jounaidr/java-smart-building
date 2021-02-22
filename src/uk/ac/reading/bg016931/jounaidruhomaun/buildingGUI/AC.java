package uk.ac.reading.bg016931.jounaidruhomaun.buildingGUI;

import java.awt.Point;
import java.awt.Rectangle;

public class AC extends roomObject {
	/**
	 * initialise AC parameters
	 * 
	 * @param startPoint
	 * @Param room
	 */
	AC(Point startPoint, int room) {
		super(startPoint, room);
	}

	/**
	 * Override showObject() method from roomObject to turn AC on when threshold (>3
	 * occupants in room) is met. This is shown by change in colour to orange
	 * 
	 * @param bi
	 * @Param b
	 */
	@Override
	public void showObject(BuildingGUI bi, Building b) {

		bi.showItem(xy.x + 3, xy.y, 4, 'b');
		if (b.allRooms.get(currentRoom).occupantsInRoom(b) > 3) {
			bi.showItem(xy.x + 3, xy.y, 4, 'o');
		}
	}

}
