package uk.ac.reading.bg016931.jounaidruhomaun.buildingGUI;

import java.awt.Point;
import java.util.ArrayList;

public class Person {
	public Point xy; // person's position
	private ArrayList<Point> path; // path it follows .. a series of xy points moves between
	private boolean stopped; // is it moving
	private int pathCount = 0; // integer to keep track of the path points
	public int colorChange = 0; // used to alternate colours

	/**
	 * create person at the given xy position
	 * 
	 * @param xys position
	 */
	Person(Point xys) {
		xy = xys;
		path = new ArrayList<Point>(); // create empty path
		stopped = true; // by default not moving
	}

	/**
	 * get the person's position
	 * 
	 * @return the position
	 */
	public Point getXY() {
		return xy;
	}

	/**
	 * get x coordinate of person
	 * 
	 * @return x
	 */
	public int getX() {
		return (int) xy.getX();
	}

	/**
	 * get y coordinate of person
	 * 
	 * @return y
	 */
	public int getY() {
		return (int) xy.getY();
	}

	/**
	 * set the person's position
	 * 
	 * @param pxy new position
	 */
	public void setXY(Point pxy) {
		xy = pxy;
	}

	/**
	 * set person as being stopped or not
	 * 
	 * @param isStopped
	 */
	public void setStopped(boolean isStopped) {
		stopped = isStopped;
	}

	/**
	 * Is person stopped
	 * 
	 * @return if so
	 */
	public boolean getStopped() {
		return stopped;
	}

	/**
	 * show person in the given building interface
	 * 
	 * @param bi
	 */
	public void showPerson(BuildingGUI bi) {
		if (colorChange == 0) {
			bi.showItem((int) xy.getX(), (int) xy.getY(), 4, 'r');
		} else {
			bi.showItem((int) xy.getX(), (int) xy.getY(), 4, 'c');
		}
	}

	/**
	 * return info about person as string
	 * 
	 * @return the string
	 */
	public String toString() {
		return "Person at " + (int) xy.getX() + ", " + (int) xy.getY();
	}

	/**
	 * clear the path the person has to follow
	 */
	public void clearPath() {
		path.clear();
		pathCount = 0;
	}

	/**
	 * add new xy to path
	 * 
	 * @param xyp new position
	 */
	public void setPath(Point xyp) {
		path.add(xyp);
	}

	/**
	 * Sets up the pathing for Person object. Person will move towards cRoom front
	 * of door (15 away), then will move to door, then will move 15 into the
	 * hallway, then will move 15 away from dRoom door, then will enter dRoom, then
	 * will go to random spot in dRoom
	 * 
	 * @param b     Building
	 * @param cRoom current room
	 * @param dRoom target room
	 */
	public void setPathing(Building b, int cRoom, int dRoom) {
		clearPath();
		setPath(b.allRooms.get(cRoom).getByDoor(-15));
		setPath(b.allRooms.get(cRoom).getByDoor(0));
		setPath(b.allRooms.get(cRoom).getByDoor(+15));
		setPath(b.allRooms.get(dRoom).getByDoor(+15));
		setPath(b.allRooms.get(dRoom).getByDoor(0));
		setPath(b.allRooms.get(dRoom).getByDoor(-15));
		setPath(b.allRooms.get(dRoom).getRandom(b.ranGen));
		setStopped(false);
		// say person can move
	}

	/**
	 * is person at the specified position?
	 * 
	 * @param pathXY
	 * @return
	 */
	private boolean equalXY(Point pathXY) {
		return ((int) pathXY.getX() == (int) xy.getX()) && ((int) pathXY.getY() == (int) xy.getY());
	}

	/**
	 * move one step towards the given position
	 * 
	 * @param pathXY
	 */
	private void moveTowards(Point pathXY) {
		int dx = 0; // amount by which it will move in x .. and y, set to -1, 0 or 1
		int dy = 0;
		if (xy.getX() < pathXY.getX())
			dx = 1;
		else if (xy.getX() > pathXY.getX())
			dx = -1;
		if (xy.getY() < pathXY.getY())
			dy = 1;
		else if (xy.getY() > pathXY.getY())
			dy = -1;
		xy.translate(dx, dy); // now move
	}

	/**
	 * attempt to move person unless it is stopped
	 */
	public void update() {
		if (stopped) {
			moveTowards(path.get(pathCount));
		} // do now
		else if (equalXY(path.get(pathCount))) { // if at next point on path
			if (pathCount < path.size() - 1)
				pathCount += 1;
			else
				setStopped(true);
		} else
			moveTowards(path.get(pathCount)); // move closer to next destination
	}
}
