package uk.ac.reading.bg016931.jounaidruhomaun.buildingGUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class FileIO {

	String output = new String(); // String stores write stream from file to be output

	/**
	 * Streams the first line of file specified by fileName and stores it in output
	 * string. This string is then returned.
	 * 
	 * @param fileName
	 */
	public String readFile(String fileName) {
		File file = new File(fileName);
		try {
			Scanner input = new Scanner(file);
			String building = input.nextLine();
			output = building;
		} catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "No file by this name");
		}
		return output;
	}

	/**
	 * Creates a new/ writes over file specified by fileName with input string
	 * building
	 * 
	 * @param fileName
	 * @param building
	 */
	public void writeFile(String fileName, String building) {
		File file = new File(fileName);
		try {
			PrintWriter outputStream = new PrintWriter(file);
			outputStream.println(building);
			outputStream.close();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "ERROR: %s\n");
		}
	}
}
