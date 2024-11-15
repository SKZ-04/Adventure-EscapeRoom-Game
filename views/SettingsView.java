package views;

import AdventureModel.ColorCtx;
import AdventureModel.ITextScale;
import AdventureModel.SettingsManager;
import com.sun.glass.ui.View;
import com.sun.prism.paint.Color;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventType;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class SettingsView extends GridPane implements ITextScale{

    private AdventureGameView adventureGameView;

    private SettingsManager settingsManager;
    private GridPane settingsGridPane;
    private Label saveGameLabel;
    private Button saveGameButton;
    private Label loadGameLabel;
    private Button loadGameButton;
    private Label volumeLabel;
    private Slider volumeSlider;
    private VBox buttonContainer;
    private Button toggleDarkMode;
    private boolean darkMode = true;

    private List<ITextScale> scalableUi;
    private List<Label> scalableText = new ArrayList<>();


    /**
     * PLEASE READ: This class is the constructor for SettingsView, which is a Gridpane.
     * This class is then instantiated inside the AdventureGameView class, where it becomes
     * part of the visual UI that AdventureGameView creates.
     *
     * Please click "1 usage" below to transport you to the instantiation in AdventureGameView
     *
     */
    public SettingsView(AdventureGameView adventureGameView) {

        //note that the buttons in this view are not accessible!!
        this.adventureGameView = adventureGameView;

        this.settingsManager = SettingsManager.getInstance();

        this.scalableUi = new ArrayList<>(4);
        setStyle("-fx-background-color: green;");
        setMaxSize(500,500);

        ColumnConstraints column1 = new ColumnConstraints(60);
        ColumnConstraints column2 = new ColumnConstraints(400);
        ColumnConstraints column3 = new ColumnConstraints(60);


        // Row constraints for SettingsView
        RowConstraints row1 = new RowConstraints(60);
        RowConstraints row2 = new RowConstraints( 400 );
        RowConstraints row3 = new RowConstraints(60);


        getColumnConstraints().addAll( column1 , column2 , column3 );
        getRowConstraints().addAll( row1 , row2 , row3 );

        //Settings Title
        Label settingsLabel = new Label("Settings");
        settingsLabel.setFont(new Font(30));
        
        setHalignment(settingsLabel, HPos.CENTER);
        setValignment(settingsLabel, VPos.CENTER);

        add(settingsLabel, 1, 0);
        scalableText.add(settingsLabel);
        ColorCtx.makeDynamicColor(settingsLabel);


        //X button for leaving Settings
        Button exitSettingsButton = new Button("X");
        exitSettingsButton.setFont(new Font(30));
        add(exitSettingsButton, 3, 0);
        exitSettingsButton.setOnAction(e -> {
            adventureGameView.gridPane.toFront();
            adventureGameView.gridPane.setDisable(false);
        });

        //Settings GridPane
        GridPane settingsGridPane = new GridPane();
        ColorCtx.makeDynamicColor(settingsGridPane);

        settingsGridPane.setAlignment(Pos.CENTER);
        settingsGridPane.setStyle("-fx-background-color: white;");

        ColumnConstraints settingsCol1 = new ColumnConstraints(150);
        ColumnConstraints settingsCol2 = new ColumnConstraints(150);

        // Row constraints
        RowConstraints settingsRow1 = new RowConstraints(80);
        RowConstraints settingsRow2 = new RowConstraints(80);
        RowConstraints settingsRow3 = new RowConstraints(80);
        RowConstraints settingsRow4 = new RowConstraints(80);
        RowConstraints settingsRow5 = new RowConstraints(80);

        settingsGridPane.getColumnConstraints().addAll( settingsCol1 , settingsCol2 );
        settingsGridPane.getRowConstraints().addAll( settingsRow1 , settingsRow2 , settingsRow3, settingsRow4, settingsRow5 );

        //First row: Save File

        saveGameLabel = new Label("Save Game");
        ColorCtx.makeDynamicColor(saveGameLabel);

        saveGameLabel.setFont(new Font(20));
        saveGameButton = new Button("Save");
        customizeButton(saveGameButton, 150, 50);
        settingsGridPane.add(saveGameLabel, 0,0);
        settingsGridPane.add(saveGameButton, 1,0);
        addSaveEvent();
        scalableText.add(saveGameLabel);

        //Second row: Load File
        loadGameLabel = new Label("Load Game");
        ColorCtx.makeDynamicColor(loadGameLabel);

        loadGameLabel.setFont(new Font(20));
        loadGameButton = new Button("Load");
        customizeButton(loadGameButton, 150, 50);

        settingsGridPane.add(loadGameLabel, 0,1);
        settingsGridPane.add(loadGameButton, 1,1);
        addLoadEvent();
        scalableText.add(loadGameLabel);

        
        //Third row: Change Volume
        volumeLabel = new Label("Change Volume");
        ColorCtx.makeDynamicColor(volumeLabel);
        scalableText.add(volumeLabel);
        volumeLabel.setFont(new Font(20));
        volumeSlider = new Slider(0, 1, SettingsManager.getInstance().getVolume());
        customizeSlider(volumeSlider, 150, 50);
        settingsGridPane.add(volumeLabel, 0,2);
        settingsGridPane.add(volumeSlider, 1,2);
        changeVolumeEvent();

        //Settings ScrollPane
        ScrollPane settingsScrollPane = new ScrollPane(settingsGridPane);
        ColorCtx.makeDynamicColor(settingsScrollPane);

        settingsScrollPane.setFitToWidth(true);
        settingsScrollPane.setFitToHeight(true);

        add(settingsScrollPane, 1,1);


        // text scale 
        Label TextLabel = new Label("Text Size");
        TextLabel.setFont(new Font(20));
        scalableText.add(TextLabel);
        ColorCtx.makeDynamicColor(TextLabel);
        
        Slider slider = new Slider(0.7, 1.3, 1);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.1f);
        slider.setBlockIncrement(0.01f);

        makeAccessible(
            slider, 
            AccessibleRole.SLIDER, 
            "Slider", 
            "Slider to change the size of text", 
            "This changes how small or large the text in the game is"
        );

        settingsGridPane.add(TextLabel, 0,3);
        settingsGridPane.add(slider, 1,3);

        
        scalableUi.add(this);
        scalableUi.add(adventureGameView);
        scalableUi.add(adventureGameView.leaderboardView);
        // scalableUi.add(adventureGameView.leaderboardView);
        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            for (ITextScale view: scalableUi){
                for (Label text: view.getScalableText()){
                    text.setScaleX(new_val.doubleValue());
                    text.setScaleY(new_val.doubleValue());
                }
            }
        });


        toggleDarkMode = new Button("Dark Mode");
        customizeButton(toggleDarkMode, 300, 50);
        makeAccessible(
            toggleDarkMode, 
            AccessibleRole.BUTTON, 
            "Button", 
            "Button to toggle dark mode", 
            "This changes the theme of the ui to dark mode or light mode"
        );
        settingsGridPane.add(toggleDarkMode, 0, 4, 2, 2);
        toggleDarkMode.setOnAction(e -> {
            if (darkMode){
                toggleDarkMode.setText("Light Mode");
                ColorCtx.setLightMode();
            } else {
                toggleDarkMode.setText("Dark Mode");
                ColorCtx.setDarkMode();
            }
            darkMode = !darkMode;
        });
        scalableText.add(loadGameLabel);
        ColorCtx.setDarkMode();
    }

    /*
     * Generic make accessible function
     */
    private static void makeAccessible(Node node, AccessibleRole role,  String name, String help, String desc){
        node.setAccessibleRole(role);
        node.setAccessibleHelp(help);
        node.setAccessibleRoleDescription(desc);
        node.setAccessibleText(name);
        node.setFocusTraversable(true);
    }
    /**
     * This method is used for coloring, resizing and styling the buttons
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        // inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        ColorCtx.makeDynamicColor(inputButton);

    }

    /**
     * This method is used for coloring, resizing and styling the buttons
     */
    private void customizeSlider(Slider inputSlider, int w, int h) {
        inputSlider.setBlockIncrement(10);
        inputSlider.setPrefSize(w, h);
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
    saveGameButton.setOnAction(e -> {
        SaveView saveView = new SaveView(adventureGameView);
        scalableUi.add(saveView);
    });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadGameButton.setOnAction(e -> {
            LoadView loadView = new LoadView(adventureGameView);
                    scalableUi.add(loadView);
        });
    }

    /**
     * This method handles the volume changes based on the volume slider
     */
    public void changeVolumeEvent() {

        // Adding Listener to value property.
        volumeSlider.valueProperty().addListener(
                new ChangeListener<Number>() {

                    public void changed(ObservableValue<? extends Number >
                                                observable, Number oldValue, Number newValue)
                    {
                        SettingsManager.getInstance().setVolume((Double) newValue);
                    }
                });
    }

    @Override
    public List<Label> getScalableText() {
       return scalableText;
    }
    
}
