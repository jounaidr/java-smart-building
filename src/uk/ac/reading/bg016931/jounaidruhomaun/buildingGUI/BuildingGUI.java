package uk.ac.reading.bg016931.jounaidruhomaun.buildingGUI;

import java.awt.Graphics;

import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
//import javafx.scene.effect.Light.Point;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class BuildingGUI extends Application {

	/**
	 * @param args
	 */
	private Stage stagePrimary;

	private Building theBuilding; // set up the building
	private VBox rtPane; // vertical box for putting info
	private GraphicsContext gc; // graphics context for drawing it
	private AnimationTimer timer; // timer used for animation
	private int whichColor = 0; // Occupants colour selector

	/**
	 * Function to show a message,
	 * 
	 * @param TStr title of message block
	 * @param CStr content of message
	 */
	private void showMessage(String TStr, String CStr) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(TStr);
		alert.setHeaderText(null);
		alert.setContentText(CStr);

		alert.showAndWait();
	}

	private void showWelcome() {
		showMessage("About", "Jounaid Ruhomaun's BuildingGUI");
	}

	/**
	 * set up the menu of commands for the GUI
	 * 
	 * @return the menu bar
	 */
	MenuBar setMenu() {
		// initially set up the file chooser to look for cfg files in current directory
		MenuBar menuBar = new MenuBar(); // create main menu

		Menu mFile = new Menu("File"); // add File main menu
		Menu mConfig = new Menu("Configure");
		MenuItem mExit = new MenuItem("Exit"); // whose sub menu has Exit
		mExit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) { // action on exit
				timer.stop(); // stop timer
				System.exit(0); // exit program
			}
		});
		MenuItem mReadFile = new MenuItem("Open file");
		mReadFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				String fileName = JOptionPane.showInputDialog("Input File Name: ");
				FileIO input = new FileIO();
				theBuilding = new Building(input.readFile(fileName));
				drawBuilding();
			}
		});
		MenuItem mWriteFile = new MenuItem("Save Building");
		mWriteFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				String fileName = JOptionPane.showInputDialog("Save As: ");
				FileIO input = new FileIO();
				input.writeFile(fileName, theBuilding.returnInputString());
			}
		});
		MenuItem mConstuctBuilding = new MenuItem("Construct Building");
		mConstuctBuilding.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				String inputString = JOptionPane.showInputDialog("Input Building String: ");
				theBuilding = new Building(inputString);
				drawBuilding();
			}
		});

		mFile.getItems().addAll(mExit); // Add exit, read and write to mFile
		mFile.getItems().addAll(mReadFile);
		mFile.getItems().addAll(mWriteFile);

		mConfig.getItems().addAll(mConstuctBuilding); // Add exit, read and write to mConfig

		Menu mHelp = new Menu("Help"); // create Help menu
		MenuItem mWelcome = new MenuItem("About"); // add Welcome sub menu item
		mWelcome.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent actionEvent) {
				showWelcome(); // whose action is to give welcome message
			}
		});
		mHelp.getItems().addAll(mWelcome); // add Welcome and About to Run main item

		menuBar.getMenus().addAll(mFile, mHelp, mConfig); // set main menu with File, Config, Run, Help
		return menuBar; // return the menu
	}

	/**
	 * set up the horizontal box for the bottom with relevant buttons
	 * 
	 * @return
	 */
	private HBox setButtons() {

		Button btnNewBuild = new Button("Defualt Building"); // Loads the file, 'Defult.txt'.
		btnNewBuild.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				theBuilding = new Building(buildingString());
				drawBuilding(); // then redraw arena
			}
		});

		Button btnStart = new Button("Start");
		btnStart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				timer.start(); // Start timer
			}
		});

		Button btnStop = new Button("Pause");
		btnStop.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				timer.stop(); // Stop timer
			}
		});

		Button btnNewPerson = new Button("Add Person"); // Add person object to frame
		btnNewPerson.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				whichColor = 1 - whichColor; // Alternate colours for male/ female
				Person occupant = new Person(
						theBuilding.allRooms.get(theBuilding.randRoom()).getRandom(theBuilding.ranGen));
				occupant.colorChange = whichColor;
				theBuilding.addPerson(occupant);
				passPerson(occupant);

			}
		});

		Button btnNewRoomba = new Button("Add Roomba"); // Add Roomba occupant (automatic hoover)
		btnNewRoomba.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int roomNo = Integer.parseInt(JOptionPane.showInputDialog("Input Room Number (0-*): "));
				Roomba occupant = new Roomba(theBuilding.allRooms.get(roomNo).getRandom(theBuilding.ranGen));
				theBuilding.addPerson(occupant);
				passPerson(occupant);

			}
		});

		Button btnNewLamp = new Button("Add Lamp"); // Add lamp object to a room
		btnNewLamp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int roomNo = Integer.parseInt(JOptionPane.showInputDialog("Input Room Number (0-*): "));

				Point[] options = new Point[] { theBuilding.allRooms.get(roomNo).getTopLeftCorner(),
						theBuilding.allRooms.get(roomNo).getTopRightCorner(),
						theBuilding.allRooms.get(roomNo).getBottomLeftCorner(),
						theBuilding.allRooms.get(roomNo).getBottomRightCorner() }; // User can place lamp in these four
																					// room corners
				int response = JOptionPane.showOptionDialog(null,
						"Please selct the room corner (top left,top right,bottomleft, bottom right)", "Select Corner",
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

				lamp newLamp = new lamp(options[response], roomNo);
				theBuilding.addObject(newLamp);
				passObject(newLamp);

			}
		});

		Button btnNewAC = new Button("Add A/C"); // Add an A/C object to a room
		btnNewAC.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int roomNo = Integer.parseInt(JOptionPane.showInputDialog("Input Room Number (0-*): "));
				AC newAC = new AC(theBuilding.allRooms.get(roomNo).getLeftMiddleWall(), roomNo); // AC will always be on
																									// left middle room
																									// wall.
				theBuilding.addObject(newAC);
				passObject(newAC);

			}
		});

		Button btnClearPerson = new Button("Clear All Occupants"); // Clears the occupants arraylist
		btnClearPerson.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				theBuilding.occupants.clear();
				drawBuilding();
			}
		});
		Button btnClearObjects = new Button("Clear All Objects"); // Clears the Objects arraylist
		btnClearObjects.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				theBuilding.Objects.clear();
				drawBuilding();
			}
		});

		// now add these buttons + labels to a HBox
		HBox hbox = new HBox(new Label("Config: "), btnNewBuild, btnNewPerson, btnNewRoomba, btnClearPerson, btnNewLamp,
				btnNewAC, btnClearObjects, new Label("Run: "), btnStart, btnStop);
		return hbox;
	}

	/**
	 * function to convert char c to actual colour used
	 * 
	 * @param c
	 * @return Color
	 */
	Color colFromChar(char c) {
		Color ans = Color.BLACK;
		switch (c) {
		case 'y':
			ans = Color.YELLOW;
			break;
		case 'r':
			ans = Color.RED;
			break;
		case 'g':
			ans = Color.GREEN;
			break;
		case 'b':
			ans = Color.BLUE;
			break;
		case 'k':
			ans = Color.BLACK;
			break;
		case 'o':
			ans = Color.ORANGE;
			break;
		case 'p':
			ans = Color.PINK;
			break;
		case 'c':
			ans = Color.CYAN;
			break;
		}
		return ans;
	}

	/**
	 * show a Line from first xy point to second xy point, with given width and
	 * colour
	 * 
	 * @param xy    is xy1[0] is x, xy1[1] is y
	 * @param xy2
	 * @param width
	 * @param b
	 */
	void showLine(Point xy, Point xy2, int width, char col) {
		gc.setStroke(colFromChar(col)); // set the stroke colour
		gc.setLineWidth(width);
		gc.strokeLine(xy.getX(), xy.getY(), xy2.getX(), xy2.getY()); // draw line
	}

	/**
	 * Draw a rectangle of size 10x10, start top left corner at (x,y)
	 * 
	 * @param x
	 * @param y
	 * @param col
	 */
	public void drawRectangle(int x, int y, char col) {
		Rectangle rect = new Rectangle(10, 10, x, y);
		gc.setFill(colFromChar(col));
		gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
	}

	/**
	 * Draw a wall from (x1,y1) to (x2,y2)
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void showWall(int x1, int y1, int x2, int y2) {
		gc.setStroke(colFromChar('k')); // set the stroke colour
		gc.setLineWidth(3);
		gc.strokeLine(x1, y1, x2, y2); // draw line
	}

	/**
	 * Show the entity of given size in the interface at position x,y Do so by
	 * drawing a circle in the colour specified by character c
	 * 
	 * @param x
	 * @param y
	 * @param size
	 * @param col
	 */
	public void showItem(int x, int y, int size, char col) {
		gc.setFill(colFromChar(col)); // set the fill colour
		gc.fillArc(x - size, y - size, size * 2, size * 2, 0, 360, ArcType.ROUND);
	} // fill 360 degree arc

	/**
	 * draw the arena and its contents
	 */
	void drawBuilding() {
		gc.setFill(Color.BEIGE);
		gc.fillRect(0, 0, theBuilding.getXSize(), theBuilding.getYSize()); // clear the canvas
		theBuilding.showBuilding(this); // draw all items
		theBuilding.showPeople(this);
		theBuilding.showObjects(this, theBuilding);

		String s = theBuilding.toString();
		rtPane.getChildren().clear(); // clear rtpane
		Label l = new Label(s); // turn string to label
		rtPane.getChildren().add(l); // add label

	}

	void passPerson(Person occupant) { // class used to draw occupant on 'this' BuildingGUI object
		occupant.showPerson(this);
		theBuilding.showPeople(this);
	}

	void passObject(roomObject newObject) { // class used to draw occupant on 'this' BuildingGUI object
		newObject.showObject(this, theBuilding);
		theBuilding.showObjects(this, theBuilding);
	}

	/**
	 * return the default building file
	 * 
	 *
	 */
	public String buildingString() {
		FileIO input = new FileIO();
		return input.readFile("defult.txt"); // Read 'defult.txt' file
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		stagePrimary = primaryStage;
		stagePrimary.setTitle("Jounaid's attempt at Intelligent Building");
		BorderPane bp = new BorderPane();
		bp.setPadding(new Insets(10, 20, 10, 20));

		bp.setTop(setMenu()); // put menu at the top

		Group root = new Group(); // create group with canvas
		Canvas canvas = new Canvas(500, 500);
		root.getChildren().add(canvas);
		bp.setCenter(root); // load canvas to left area

		gc = canvas.getGraphicsContext2D(); // context for drawing

		timer = new AnimationTimer() { // set up timer
			public void handle(long currentNanoTime) {
				theBuilding.update();
				drawBuilding();
			}
		};

		rtPane = new VBox(); // set vBox on right to list items
		rtPane.setAlignment(Pos.TOP_LEFT);
		rtPane.setPadding(new Insets(5, 75, 75, 5));
		bp.setRight(rtPane);

		bp.setBottom(setButtons()); // set bottom pane with buttons

		Scene scene = new Scene(bp, 800, 600); // set overall scene
		bp.prefHeightProperty().bind(scene.heightProperty());
		bp.prefWidthProperty().bind(scene.widthProperty());

		primaryStage.setScene(scene);
		primaryStage.show();
		// whichBuild = 0;
		theBuilding = new Building(buildingString());
		showWelcome(); // set welcome message
		drawBuilding();

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args); // launch the GUI

	}

}
