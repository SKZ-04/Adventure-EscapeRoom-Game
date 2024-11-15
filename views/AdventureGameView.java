package views;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.ColorCtx;
import AdventureModel.ITextScale;
import AdventureModel.SettingsManager;
import AdventureModel.Combat.CombatState;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyEvent; //you will need these!
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.event.EventHandler; //you will need this too!
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * VIDYARD LINK: https://share.vidyard.com/watch/nSkWsfrpWK1PUGTfe3T2VQ?
 * PASSWORD: No Password - it is a public video
 */
public class AdventureGameView implements ITextScale {

    AdventureGame model; //model of the game
    Stage stage; //stage on which all is rendered
    Button settingsButton, leaderboardButton, playAgainButton;
    Boolean helpToggle = false; //is help on display?

    //jamie added this: create new Stack pane to stack stuff on top of each other
    StackPane mainStackPane = new StackPane();
    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input

    SettingsView settingsView;
    LeaderboardView leaderboardView;

    Label commandLabel;
    Label pointsLabel;
    ScrollPane scO;
    ScrollPane scI;

    private MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    private List<Label> scalableText = new ArrayList<>();
    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {


        // setting up the stage
        this.stage.setTitle("<hujamie1>'s Adventure Game"); //Replace <YOUR UTORID> with your UtorID

        LeaderboardView leaderboardView = new LeaderboardView(this, model);
        this.leaderboardView = leaderboardView;
        leaderboardView.setAlignment(Pos.CENTER);
        leaderboardView.updateLeaderboard();


        // instantiation of the settingView Gridpane, which is a part of this classes UI
        SettingsView settingsView = new SettingsView(this);
        this.settingsView = settingsView;
        settingsView.setAlignment(Pos.CENTER);

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        ColorCtx.makeDynamicColor(objectsInInventory);
        
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);
        ColorCtx.makeDynamicColor(objectsInRoom);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));
        ColorCtx.makeDynamicColor(gridPane);

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );
        
        // Buttons
        settingsButton = new Button("Settings");
        settingsButton.setId("Settings");
        customizeButton(settingsButton, 100, 50);
        makeButtonAccessible(settingsButton, "Settings Button", "This button accesses settings.", "This button accesses the settings page. Click it in order be able to save and load the game, as well as change the volume and turn on colorblind settings.");
        addSettingsEvent();
        ColorCtx.makeDynamicColor(settingsButton);


        leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setId("Leaderboard");
        customizeButton(leaderboardButton, 100, 50);
        makeButtonAccessible(leaderboardButton, "Leaderboard Button", "This button displays the leaderboard.", "This button displays the leaderboard. Click it in order to view all previous scores, so you can gauge each performance accordingly.");
        addLeaderboardEvent();
        ColorCtx.makeDynamicColor(leaderboardButton);

        leaderboardButton = new Button("Leaderboard");
        leaderboardButton.setId("Leaderboard");
        customizeButton(leaderboardButton, 140, 50);
        makeButtonAccessible(leaderboardButton, "Leaderboard Button", "This button displays the leaderboard.", "This button displays the leaderboard. Click it in order to view all previous scores, so you can gauge each performance accordingly.");
        addLeaderboardEvent();


        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(settingsButton, leaderboardButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);
        ColorCtx.makeDynamicColor(topButtons);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);
        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field
        ColorCtx.makeDynamicColor(inputTextField);

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setFont(new Font("Arial", 16));
        scalableText.add(objLabel);
        ColorCtx.makeDynamicColor(objLabel);
    

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setFont(new Font("Arial", 16));
        scalableText.add(invLabel);
        ColorCtx.makeDynamicColor(invLabel);


        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

//        Label commandLabel = new Label("What would you like to do?");
        commandLabel = new Label("What is your name?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));
        scalableText.add(commandLabel);
        ColorCtx.makeDynamicColor(commandLabel);

        pointsLabel = new Label("Number of Points: " + model.player.getNumPoints().getPoints());
        pointsLabel.setStyle("-fx-text-fill: white;");
        pointsLabel.setFont(new Font("Arial", 16));

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        scalableText.add(this.roomDescLabel);

        // adding the text area and submit button to a VBox
        VBox textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(pointsLabel, commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 0, 2, 3, 1 );
        ColorCtx.makeDynamicColor(textEntry);

        //add everything to the mainStackPane
        mainStackPane.getChildren().addAll(leaderboardView, settingsView, gridPane);
        ColorCtx.makeDynamicColor(mainStackPane);
        ColorCtx.makeDynamicColor(roomDescLabel);

        scO = new ScrollPane(objectsInRoom);
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);
        ColorCtx.makeDynamicColor(scO);
        scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        gridPane.add(scI,2,1);
        ColorCtx.makeDynamicColor(scI);


        // Render everything
        var scene = new Scene( mainStackPane,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();
        ColorCtx.init_game(this);
        ColorCtx.setDarkMode();
    }

    public void updateColor(){
        updateColorInner(this.gridPane.getChildren());
    }

    public static void updateColorInner(ObservableList<Node> lst){
        for (Node e: lst){
            ColorCtx.colorize(e);
            if (e instanceof Pane){
                updateColorInner(((Pane)e).getChildren());
            } else if (e instanceof ScrollPane){
                Node node = ((ScrollPane)e).lookup(".viewport");
                if (node != null) ColorCtx.colorize(node);
            }
        }
    }

    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute 
     *
     * Your event handler should respond when users 
     * hits the ENTER or TAB KEY. If the user hits 
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped 
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus 
     * of the scene onto any other node in the scene 
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        inputTextField.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                String inputStrip = inputTextField.getText().strip();
                inputStrip = inputStrip.strip(); //get rid of white space
                stopArticulation(); //if speaking, stop
                
                // custom text input hook
                if (!this.model.player.getCurrentRoom().submitEventHook(inputStrip)){
                    submitEvent(inputStrip);
                }
                inputTextField.clear();
            }

            if (event.getCode() == KeyCode.TAB) {
                gridPane.requestFocus();
            }
        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
    */
    private void submitEvent(String text) {

        if (commandLabel.getText().equals("What is your name?")){
            this.model.interpretAction(text); //process the command!
            commandLabel.setText("What would you like to do?");
        }
        else {
            if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
                String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription();
                String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
                String enemyDescription = this.model.getPlayer().getCurrentRoom().getEnemyString();
                String roomText = "";
                if (!objectString.isEmpty()) roomText += roomDesc + "\n\nObjects in this room:\n" +
                        objectString + "\n\n" + enemyDescription;
                else roomText += roomDesc + "\n\n" + enemyDescription;
                roomText += "\n\n" + this.model.getPlayer().getCurrentRoom().lookHook();
                roomDescLabel.setText(roomText);
                articulateRoomDescription(); //all we want, if we are looking, is to repeat description.
                return;
            } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
                showInstructions();
                return;
            } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
                
                if (this.model.getPlayer().getCurrentRoom().commandHook(roomDescLabel)){
                    String customCommand = roomDescLabel.getText();
                    showCommands(); //this is new!  We did not have this command in A1
                    roomDescLabel.setAlignment(Pos.TOP_CENTER);
                    roomDescLabel.setText(roomDescLabel.getText().strip()  + "\n\n" + customCommand);
                } else {
                    showCommands(); 
                }
                return;
            } else if (text.equalsIgnoreCase("F") || text.equalsIgnoreCase("FIGHT")) {
                if (this.model.getPlayer().getCurrentRoom().getEnemy() == null ||
                        !this.model.getPlayer().getCurrentRoom().getEnemy().isAlive()) {
                    roomDescLabel.setText("There is no enemy to fight...");
                }
                else {
                    CombatState combat =
                            new CombatState(this.model.getPlayer(), this.model.getPlayer().getCurrentRoom().getEnemy());
                    CombatView cv = new CombatView(this, combat);
                    cv.setId("Combat");
                    stopArticulation(); //if speaking, stop
                    cv.toFront();
                    cv.requestFocus();
                    cv.setAlignment(Pos.CENTER);
                    mainStackPane.getChildren().add(cv);
                    gridPane.setDisable(true);
                }
                return;
            }


            //try to move!
            String output = this.model.interpretAction(text); //process the command!

            if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
                updateScene(output);
                updateItems();
            } else if (output.equals("GAME OVER")) {
                leaderboardView.endGame(model.player);
                updateScene("");
                updateItems();
                PauseTransition pause = new PauseTransition(Duration.seconds(10));
                pause.setOnFinished(event -> {
                    Platform.exit();
                });
                pause.play();
            } else if (output.equals("FORCED")) {
                //write code here to handle "FORCED" events!
                //Your code will need to display the image in the
                //current room and pause, then transition to
                //the forced room.
                updateScene("");
                updateItems();
                PauseTransition pause = new PauseTransition(Duration.seconds(5));
                pause.setOnFinished(event -> {

                    inputTextField.setDisable(false);
                    objectsInRoom.setDisable(false);
                    objectsInInventory.setDisable(false);
                    String commands = model.player.getCurrentRoom().getCommands();
                    ArrayList listOfCommands = new ArrayList(List.of(commands.split(",")));
                    if (listOfCommands.contains("FORCED")) {
                        submitEvent("FORCED");
                    } else {
                        updateScene("");
                        updateItems();
                    }

                    try {
                        model.movePlayer("FORCED");
                    } catch (Exception e) {
                        System.out.println("You won!");
                    }

                });
                pause.play();
                inputTextField.setDisable(true);
                objectsInInventory.setDisable(true);
                objectsInRoom.setDisable(true);

                }
        }
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the 
     * current room.
     */
    private void showCommands() {
        String commands = this.model.getPlayer().getCurrentRoom().getCommands();
        roomDescLabel.setAlignment(Pos.BOTTOM_CENTER);
        roomDescLabel.setText("Here is all the commands in this room: " + commands);
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be displayed
     * below the image.
     * 
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {

        getRoomImage(); //get the image of the current room
        formatText(textToDisplay); //format the text to display
        pointsLabel.setText("Number of Points: " + model.player.getNumPoints().getPoints());
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        VBox roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");
        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();
        ColorCtx.colorize(roomPane);
        updateColor();
        //finally, articulate the description
        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
    }

    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     * 
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            String enemyDescription = this.model.getPlayer().getCurrentRoom().getEnemyString();
            String roomText = "";
            if (!objectString.isEmpty()) {
                roomText += roomDesc + "\n\nObjects in this room:\n" + objectString + "\n\n" + enemyDescription;
            } else {
                roomText += roomDesc + "\n\n" + enemyDescription;
            }
            roomText += "\n\n" + this.model.getPlayer().getCurrentRoom().lookHook();
            roomDescLabel.setText(roomText);
            roomText = roomDesc + "\n";
            // String roomText = roomDesc + "\n";
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setFont(new Font("Arial", 16));
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place 
     * it in the roomImageView 
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     * 
     * Images of each object are in the assets 
     * folders of the given adventure game.
     */
    public void updateItems() {

        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";

        ArrayList<AdventureObject> currentRoomObjects = this.model.getPlayer().getCurrentRoom().objectsInRoom;
        ArrayList<AdventureObject> playerInventory = this.model.getPlayer().inventory;

        //VERY IMPORTANT: clear the Vboxes to prevent duplication
        objectsInRoom.getChildren().clear();
        objectsInInventory.getChildren().clear();

        for (AdventureObject object : currentRoomObjects) {
            String objectName = object.getName();
            String objectImageFile = model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";
            //image view shenanigans
            ImageView objectImageView = new ImageView(objectImageFile);
            objectImageView.setAccessibleText(object.getDescription());
            objectImageView.setFitWidth(100);

            //creating object button and more shenanigans
            Button button = new Button(objectName, objectImageView);
            button.setStyle("-fx-content-display: top;");
            makeButtonAccessible(button, objectName + " Button.", "This is a button",
                    "This is the button description: " + object.getDescription());

            //adding it to the Vbox
            objectsInRoom.getChildren().add(button);

            //once button is pressed
            button.setOnAction(event -> {
                // Handle button click for the corresponding object
                currentRoomObjects.remove(object);
                playerInventory.add(object);
                objectsInRoom.getChildren().remove(button);

                updateItems();
            });
        }
        for (AdventureObject InvObject : playerInventory) {
            String objectName = InvObject.getName();
            String objectImageFile = model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";

            ImageView objectImageView = new ImageView(objectImageFile);
            objectImageView.setAccessibleText(InvObject.getDescription());
            objectImageView.setFitWidth(100);

            Button button = new Button(objectName, objectImageView);
            button.setStyle("-fx-content-display: top;");
            makeButtonAccessible(button, objectName + " Button.", "This is a button",
                    "This is the button description: " + InvObject.getDescription());

            objectsInInventory.getChildren().add(button);

            button.setOnAction(event -> {
                // Handle button click for the corresponding object
                playerInventory.remove(InvObject);
                currentRoomObjects.add(InvObject);
                objectsInInventory.getChildren().remove(button);

                updateItems();
            });
        }
        updateColor();
    }

    /*
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        String helpText = model.getInstructions();
        if (!helpToggle) {
            gridPane.getChildren().remove(1,1);
            formatText(helpText);
            Label helpTextBox = new Label(helpText);
            helpTextBox.setFont(new Font("Arial", 14));
            helpTextBox.setPrefWidth(500);
            helpTextBox.setPrefHeight(500);
            helpTextBox.setTextOverrun(OverrunStyle.CLIP);
            helpTextBox.setWrapText(true);
            scalableText.add(helpTextBox);
            ColorCtx.colorize(helpTextBox);
            VBox helpPane = new VBox(helpTextBox);
            helpPane.setPadding(new Insets(10));
            helpPane.setAlignment(Pos.CENTER);
            helpPane.setStyle("-fx-background-color: #000000;");
            gridPane.add(helpPane, 1, 1);
            ColorCtx.colorize(helpPane);
            stage.sizeToScene();

            helpToggle = true;

        } else {
            gridPane.getChildren().remove(1,1);
            updateScene("");
            helpToggle = false;
        }
    }

    /**
     * This method handles the event related to the
     * settings button.
     */
    public void addSettingsEvent() {
        settingsButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            gridPane.requestFocus();
            settingsView.toFront();
            gridPane.setDisable(true);

        });
    }


    public void addLeaderboardEvent() {
        leaderboardButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            gridPane.requestFocus();
            leaderboardView.toFront();
            gridPane.setDisable(true);
        });
    }


//    /**
//     * This method handles the event related to the
//     * help button.
//     */
//    public void addInstructionEvent() {
//        helpButton.setOnAction(e -> {
//            stopArticulation(); //if speaking, stop
//            showInstructions();
//        });
//    }
//
//    /**
//     * This method handles the event related to the
//     * save button.
//     */


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);

        //sets volume of audio before playing
        mediaPlayer.setVolume(SettingsManager.getInstance().getVolume());

        mediaPlayer.play();
        mediaPlaying = true;

    }

    /**
     * This method stops articulations 
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }

    @Override
    public List<Label> getScalableText() {
        return scalableText;
    }

}
