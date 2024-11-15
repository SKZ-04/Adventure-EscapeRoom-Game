package views;

import java.util.List;

import AdventureModel.*;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

import com.sun.glass.ui.View;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

public class LeaderboardView extends GridPane implements ITextScale{

    AdventureGame model; //model of the game
    private AdventureGameView adventureGameView;
    private Button closeWindowButton;
    private GridPane leaderboardGridPane;
    private Label leaderboardLabel;
    private ListView<String> ScoreList;
    private List<Label> scalableText = new ArrayList<>();

    private LeaderboardView instance;

    /**
     * View for the Leaderboard Page
     * @param adventureGameView
     * @param model
     */
    public LeaderboardView(AdventureGameView adventureGameView, AdventureGame model) {

        //note that the buttons in this view are not accessible!!
        this.adventureGameView = adventureGameView;
        this.model = model;

        this.ScoreList = new ListView<>();

        setStyle("-fx-background-color: green;");
        setMaxSize(500,500);

        ColumnConstraints column1 = new ColumnConstraints(60);
        ColumnConstraints column2 = new ColumnConstraints(400);
        ColumnConstraints column3 = new ColumnConstraints(60);


        // Row constraints for SettingsView
        RowConstraints row1 = new RowConstraints(60);
        RowConstraints row2 = new RowConstraints( 400);
        RowConstraints row3 = new RowConstraints(60);


        getColumnConstraints().addAll( column1 , column2 , column3 );
        getRowConstraints().addAll( row1 , row2 , row3 );

        //Settings Title
        Label leaderboardLabel = new Label("Leaderboard");
        leaderboardLabel.setFont(new Font(30));

        setHalignment(leaderboardLabel, HPos.CENTER);
        setValignment(leaderboardLabel, VPos.CENTER);

        add(leaderboardLabel, 1, 0);


        //X button for leaving Settings
        Button exitLeaderboardButton = new Button("X");
        exitLeaderboardButton.setFont(new Font(30));
        add(exitLeaderboardButton, 3, 0);
        exitLeaderboardButton.setOnAction(e -> {
            adventureGameView.gridPane.toFront();
            adventureGameView.gridPane.setDisable(false);
        });


        //Leaderboard GridPane
        leaderboardGridPane = new GridPane();
        leaderboardGridPane.setAlignment(Pos.CENTER);
        leaderboardGridPane.setStyle("-fx-background-color: white;");

//        ColumnConstraints settingsCol1 = new ColumnConstraints(300);
//        leaderboardGridPane.getColumnConstraints().add( settingsCol1 );


        ColumnConstraints leaderboardCol1 = new ColumnConstraints(150);
        ColumnConstraints leaderboardCol2 = new ColumnConstraints(150);
        leaderboardCol1.setHalignment(javafx.geometry.HPos.CENTER);
        leaderboardCol2.setHalignment(javafx.geometry.HPos.CENTER);
        leaderboardGridPane.getColumnConstraints().addAll( leaderboardCol1 , leaderboardCol2 );

//        // Row constraints
//        RowConstraints settingsRow1 = new RowConstraints(100);
//        RowConstraints settingsRow2 = new RowConstraints(100);
//        RowConstraints settingsRow3 = new RowConstraints(100);
//        RowConstraints settingsRow4 = new RowConstraints(100);
//
//        leaderboardGridPane.getRowConstraints().addAll( settingsRow1 , settingsRow2 , settingsRow3, settingsRow4 );



        //Leaderboard ScrollPane
        ScrollPane leaderboardScrollPane = new ScrollPane(leaderboardGridPane);

        leaderboardScrollPane.setFitToWidth(true);
        leaderboardScrollPane.setFitToHeight(true);

        add(leaderboardScrollPane, 1,1);


    }

    /**
     * returns a list of Scores
     * @return
     */
    public ListView<String> getScoreList() {
        return ScoreList;
    }

    /**
     * leaderboard behavior for when the game ends
     * @param player
     */
    public void endGame(Player player) {
        addScore(player.getNumPoints());
        saveScore();
        updateLeaderboard();
    }


    /**
     *
     * Adds score to the leaderboard
     * @param entry
     */
    public void addScore(Points entry) {
        String str = "";
        str += entry.getPlayer().getName() + " " + entry.getPoints();
        int i = 0;
        for (String x : getScoreList().getItems()){
            String s = x.split(" ")[1];
            if (Integer.parseInt(s) <= entry.getPoints()){
                break;
            }
            i += 1;
        }
        getScoreList().getItems().add(i, str);


    }

    /**
     * Saves Score to a txt file for future access
     */
    public void saveScore() {

        // Specify the file path
        String directoryPath = "Games\\Saved\\Scores";
        String filePath = directoryPath + "\\scores.txt";


        File file = new File(filePath);

        // Check of scores.txt exists or not. If it does, delete it and create a new one. If it doesnt,
        // then create a new one.
        if (file.exists()) {
            if (file.delete()) {

                System.out.println("Existing file deleted successfully.");
            } else {
                System.out.println("Failed to delete the existing file.");
            }
        }


        // Create the file
        try {
            if (file.createNewFile()) {
                System.out.println("File created successfully.");
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the file.");
            e.printStackTrace();
        }


        // Write the ScoreList to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String score : getScoreList().getItems()) {
                writer.write(score);
                writer.newLine(); // Write each element on a new line
            }
            System.out.println("Scores have been written to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing the scores to the file.");
        }



    }

    /**
     * adds all new labels to the leaderboard and reads a score file
     */
    public void updateLeaderboard() {


        // DELETE ANY LABELS THAT MAY ALREADY EXIST IN THE Leaderboard Grid Pane
        int rowToDelete = 0; // Row index to delete
        for (int i = 0; i < leaderboardGridPane.getRowConstraints().size(); i++) {

            // Iterate through the children of the GridPane to find nodes in the specified row and column
            List<Node> nodesToRemove = new ArrayList<>();
            for (Node node : leaderboardGridPane.getChildren()) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);

                if (rowIndex == rowToDelete && colIndex == 0) {
                    nodesToRemove.add(node);
                }
                if (rowIndex == rowToDelete && colIndex == 1) {
                    nodesToRemove.add(node);
                }
            }
            leaderboardGridPane.getChildren().removeAll(nodesToRemove);
            rowToDelete += 1;
        }



        // Specify the file path
        String directoryPath = "Games\\Saved\\Scores";
        String filePath = directoryPath + "\\scores.txt";

        // Create the directory if it doesn't exist
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Directory created successfully.");
            } else {
                System.out.println("Failed to create the directory.");
                return;
            }
        }

        File file = new File(filePath);




        // Check if scores.txt exists or not. If it does, populate the ScoreList attribute.
        if (file.exists()) {
            // After clearing up the grid pane, update the grid pane by reading from the scores.txt file and filling
            // out a row constraint for each entry in scores.txt
            try (BufferedReader reader = new BufferedReader(new FileReader("Games\\Saved\\Scores\\scores.txt"))) {
                String line;
                int row = 0;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        String label1Text = line.split(" ")[0];
                        String label2Text = line.split(" ")[1];

                        Label label1 = new Label(label1Text);
                        Label label2 = new Label(label2Text);


                        label1.setFont(new Font(20));
                        label2.setFont(new Font(20));
                        getScoreList().getItems().add(line);
                        if (row == leaderboardGridPane.getRowConstraints().size()) {
                            RowConstraints newRow = new RowConstraints(100);
                            leaderboardGridPane.getRowConstraints().add(newRow);
                            newRow.setValignment(javafx.geometry.VPos.CENTER);
                        }
                        leaderboardGridPane.add(label1, 0, row);
                        leaderboardGridPane.add(label2, 1, row);

                        scalableText.add(label1);
                        scalableText.add(label2);
                        row++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public LeaderboardView geinstance() {
        return instance;
    }


    @Override
    public List<Label> getScalableText() {
        return scalableText;
    }

}
