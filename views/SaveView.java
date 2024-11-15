package views;

import AdventureModel.AdventureGame;
import AdventureModel.ColorCtx;
import AdventureModel.ITextScale;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Class SaveView.
 *
 * Saves Serialized adventure games.
 */
public class SaveView implements ITextScale {

    static String saveFileSuccess = "Saved Adventure Game!!";
    static String saveFileExistsError = "Error: File already exists";
    static String saveFileNotSerError = "Error: File must end with .ser";
    private Label saveFileErrorLabel = new Label("");
    private Label saveGameLabel = new Label(String.format("Enter name of file to save"));
    private TextField saveFileNameTextField = new TextField("");
    private Button saveGameButton = new Button("Save Game");
    private Button closeWindowButton = new Button("Close Window");

    private AdventureGameView adventureGameView;

    /**
     * Constructor
     */
    public SaveView(AdventureGameView adventureGameView) {
        this.adventureGameView = adventureGameView;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        ColorCtx.makeDynamicColor(dialogVbox);

        saveGameLabel.setId("SaveGame"); // DO NOT MODIFY ID
        ColorCtx.makeDynamicColor(saveGameLabel);

        saveFileErrorLabel.setId("SaveFileErrorLabel");
        ColorCtx.makeDynamicColor(saveFileErrorLabel);

       

        saveGameLabel.setStyle("-fx-text-fill: #e8e6e3;");
        saveGameLabel.setFont(new Font(16));
        ColorCtx.makeDynamicColor(saveGameLabel);

        saveFileErrorLabel.setStyle("-fx-text-fill: #e8e6e3;");
        saveFileErrorLabel.setFont(new Font(16));
        ColorCtx.makeDynamicColor(saveFileErrorLabel);

        saveFileNameTextField.setId("SaveFileNameTextField");

        saveFileNameTextField.setStyle("-fx-text-fill: #000000;");
        saveFileNameTextField.setFont(new Font(16));

        String gameName = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()) + ".ser";
        saveFileNameTextField.setText(gameName);
        ColorCtx.makeDynamicColor(saveFileNameTextField);


        saveGameButton = new Button("Save board");
        saveGameButton.setId("SaveBoardButton"); // DO NOT MODIFY ID
        saveGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        saveGameButton.setPrefSize(200, 50);
        saveGameButton.setFont(new Font(16));
        AdventureGameView.makeButtonAccessible(saveGameButton, "save game", "This is a button to save the game", "Use this button to save the current game.");
        saveGameButton.setOnAction(e -> saveGame());
        ColorCtx.makeDynamicColor(saveGameButton);

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the save game window", "Use this button to close the save game window.");
        ColorCtx.makeDynamicColor(closeWindowButton);

        VBox saveGameBox = new VBox(10, saveGameLabel, saveFileNameTextField, saveGameButton, saveFileErrorLabel, closeWindowButton);
        saveGameBox.setAlignment(Pos.CENTER);
        ColorCtx.makeDynamicColor(saveGameBox);

        dialogVbox.getChildren().add(saveGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Saves the Game
     * Save the game to a serialized (binary) file.
     * Get the name of the file from saveFileNameTextField.
     * Files will be saved to the Games/Saved directory.
     * If the file already exists, set the saveFileErrorLabel to the text in saveFileExistsError
     * If the file doesn't end in .ser, set the saveFileErrorLabel to the text in saveFileNotSerError
     * Otherwise, load the file and set the saveFileErrorLabel to the text in saveFileSuccess
     */
    private void saveGame() {

        AdventureGame savedGame = adventureGameView.model;
        File newSave = new File(saveFileNameTextField.getText());
        File directory = new File("Games\\Saved");
        savedGame.saveModel(newSave);

        //check if newSave is a .ser file
        String[] saveFileTokens = newSave.getName().split("\\.");
        System.out.println(Arrays.toString(saveFileTokens));
        if (saveFileTokens.length == 1) {
            saveFileErrorLabel.setText(saveFileNotSerError);
            return;
        } else if (!saveFileTokens[saveFileTokens.length - 1].equals("ser")) {
            saveFileErrorLabel.setText(saveFileNotSerError);
            return;
        }

        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                System.out.println("Directory created successfully.");
            } else {
                System.err.println("Failed to create the directory.");
                return;
            }
        }

        // Create the file within the directory
        File file = new File(directory, newSave.getName());

        try {
            if (file.createNewFile()) {
                saveFileErrorLabel.setText("File created successfully.");
                System.out.println("File created successfully.");
            } else {
                saveFileErrorLabel.setText(saveFileExistsError);
                System.err.println("File already exists.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Label> getScalableText() {
        return List.of(this.saveGameLabel, this.saveFileErrorLabel);
    }

}

