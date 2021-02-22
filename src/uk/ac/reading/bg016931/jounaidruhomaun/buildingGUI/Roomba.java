package uk.ac.reading.bg016931.jounaidruhomaun.buildingGUI;

import java.awt.Point;

public class Roomba extends Person {
	/**
	 * Initialise Roomba to Point xys
	 * 
	 * @param xys
	 */
	Roomba(Point xys) {
		super(xys);
	}

	/**
	 * Override Person showPerson() method with size 3 and colour char g
	 * 
	 * @param bi
	 */
	@Override
	public void showPerson(BuildingGUI bi) {
		bi.showItem((int) xy.getX(), (int) xy.getY(), 3, 'g');
	}

	/**
	 * Override Person toString() method to display Roomba position
	 * 
	 */
	@Override
	public String toString() {
		return "Roomba at " + (int) xy.getX() + ", " + (int) xy.getY();
	}

	/**
	 * Override Person setPathing() method. Roomba object stays in current room
	 * cRoom and moves to a random point in current room cRoom
	 * 
	 * @param b
	 * @param cRoom
	 * @param dRoom
	 */
	@Override
	public void setPathing(Building b, int cRoom, int dRoom) {
		clearPath();
		setPath(b.allRooms.get(cRoom).getRandom(b.ranGen));
		setStopped(false);
	}

}
