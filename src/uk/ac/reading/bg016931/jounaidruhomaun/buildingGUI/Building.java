package uk.ac.reading.bg016931.jounaidruhomaun.buildingGUI;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Building {
	/**
	 * The building in which there are various rooms and objects Its size is defined
	 * by xSize,ySize Variables are used for actual rooms
	 * 
	 */
	private int xSize = 10; // size of building
	private int ySize = 10;
	public ArrayList<Room> allRooms; // array of rooms
	public ArrayList<Person> occupants; // Array of people
	public ArrayList<roomObject> Objects; // array of objects
	public Random ranGen; // for generating random numbers
	private String buildingInput; // Value of original building string

	/**
	 * Construct a building
	 */
	public Building(String bs) {
		allRooms = new ArrayList<Room>(); // Initialise all arrays of 'things'
		occupants = new ArrayList<Person>();
		Objects = new ArrayList<roomObject>();
		ranGen = new Random(); // create object for generating random numbers
		setBuilding(bs); // now set building using string bs
		buildingInput = bs; // Store this string
	}

	/**
	 * set up the building, as defined in string
	 * 
	 * @param bS of form xS,yS;x1 y1 x2 y2 xd yd ds; etc xS,yS define size, and for
	 *           each room have locations of opposite corners, door and door size
	 */
	public void setBuilding(String bS) {
		allRooms.clear();
		StringSplitter bSME = new StringSplitter(bS, ";"); // split strings by ;
		StringSplitter bSz = new StringSplitter(bSME.getNth(0, "5 5"), " "); // split first by space
		xSize = bSz.getNthInt(0, 5); // get first of the first string, being xsize
		ySize = bSz.getNthInt(1, 5);
		for (int ct = 1; ct < bSME.numElement(); ct++) // remaining strings define rooms
			allRooms.add(new Room(bSME.getNth(ct, ""))); // add each in turn
		// now add a person
	}

	/**
	 * On arena size
	 * 
	 * @return size in x direction of robot arena
	 */
	public int getXSize() {
		return xSize;
	}

	/**
	 * On arena size
	 * 
	 * @return size in y direction of robot arena
	 */
	public int getYSize() {
		return ySize;
	}

	/**
	 * Get current room and target room, then calculate the pathing for the occupant
	 * 
	 * @param occupant
	 */
	void setNewRoom(Person occupant) {
		// at this stage all this does is
		int cRoom = whichRoom(occupant.getXY());
		int dRoom = cRoom;
		while (dRoom == cRoom)
			dRoom = ranGen.nextInt(allRooms.size()); // get another room randomlt

		occupant.setPathing(this, cRoom, dRoom); // sets the occupants pathing
	}

	/**
	 * calculate a random room number
	 * 
	 * @return number in range 0.. number of rooms
	 */
	public int randRoom() {
		return ranGen.nextInt(allRooms.size());
	}

	/**
	 * create new person and set path for it to follow
	 */
	public void addPerson(Person occupant) {
		occupants.add(occupant);
		setNewRoom(occupants.get(occupants.size() - 1));
	}
	/**
	 * create new object and add it to the builds Objects array
	 */
	public void addObject(roomObject object) {
		Objects.add(object);
	}

	/**
	 * show all the building's rooms and person in the interface
	 * 
	 * @param bi the interface
	 */
	public void showBuilding(BuildingGUI bi) {
		for (Room r : allRooms)
			r.showRoom(bi);
		// loop through array of all rooms, displaying each
	}

	public void showPeople(BuildingGUI bi) {
		for (Person p : occupants)
			p.showPerson(bi);
		// loop through array of all occupants, displaying each
	}

	public void showObjects(BuildingGUI bi, Building b) {
		for (roomObject o : Objects)
			o.showObject(bi, b);
		// loop through array of all objects, displaying each
	}

	/**
	 * method to update the building Here it just deals with the occupant since objects do not update.
	 */
	public void update() {
		for (Person p : occupants) {
			if (p.getStopped())
				setNewRoom(p);
			else
				p.update();
		}
	}

	/**
	 * method to determine which room position x,y is in
	 * 
	 * @param xy
	 * @return n, the number of the room or -1 if in corridor
	 */
	public int whichRoom(Point xy) {
		int ans = -1;
		for (int ct = 0; ct < allRooms.size(); ct++)
			if (allRooms.get(ct).isInRoom(xy))
				ans = ct;
		return ans;
	}

	/**
	 * method to return information bout the building as a string
	 */
	public String toString() {
		String s = "Building size " + getXSize() + "," + getYSize() + "\n";
		for (Room r : allRooms)
			s = s + r.toString() + "\n";
		for (Person p : occupants)
			s = s + p.toString() + "\n";
		for (roomObject o : Objects)
			s = s + o.toString() + "\n";
		return s;
	}
	/**
	 * method to return original building input string
	 */
	public String returnInputString() {
		return buildingInput;
	}
}
