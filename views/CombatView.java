package views;

import AdventureModel.Combat.CombatState;
import AdventureModel.Combat.Reactions.FistAttackReaction;
import AdventureModel.Combat.Reactions.Reaction;
import AdventureModel.Combat.Reactions.SwordAttackReaction;

import java.util.List;

import AdventureModel.ColorCtx;
import AdventureModel.ITextScale;
import AdventureModel.Mortal;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CombatView extends GridPane implements ITextScale{
    private final CombatState state;
    private final AdventureGameView gameView;
    private final Label title = new Label("Battle!");
    private Label descLabel;
    private Button attackBtn;
    private Button fistSelect;
    private Button swordSelect;
    public CombatView(AdventureGameView v, CombatState s) {
        this.gameView = v;
        this.state = s;

        initUI();
    }

    private void initUI() {
        setPadding(new Insets(0));
        // Set size and style
        ColorCtx.makeDynamicColorWithStyle(this, "-fx-border-radius: 10px; -fx-border-color: gray; -fx-border-width: 5px;");
        setMaxSize(960,750);

        // Set the sizes of the rows
        getRowConstraints().add(new RowConstraints(250));
        getRowConstraints().add(new RowConstraints(200));
        getRowConstraints().add(new RowConstraints(50));
        getRowConstraints().add(new RowConstraints(50));

        // Set the size of the column.
        getColumnConstraints().add(new ColumnConstraints(400));

        addRow(0, this.title);
        this.title.setFont(new Font(30));
        this.title.setTextFill(Color.WHITE);
        setValignment(this.title, VPos.TOP);
        setHalignment(this.title, HPos.CENTER);
        ColorCtx.makeDynamicColor(this.title);
        

        this.descLabel = new Label(genInfo() + "\n\n");
        this.descLabel.setTextFill(Color.WHITE);
        this.descLabel.setFont(new Font(15));
        this.descLabel.setMaxWidth(400);
        this.descLabel.setWrapText(true);
        ColorCtx.makeDynamicColor(descLabel);

        setHalignment(this.descLabel, HPos.CENTER);
        addRow(1, this.descLabel);

        this.attackBtn = new Button("Attack");
        this.attackBtn.setMaxWidth(400);
        ColorCtx.makeDynamicColor(this.attackBtn);        

        HBox box = new HBox();
        this.fistSelect = new Button("Fist");
        this.fistSelect.setStyle("-fx-border-color: yellow; -fx-border-width: 2px;");     
        this.swordSelect = new Button("Sword");
        this.swordSelect.setStyle("-fx-border-color: black; -fx-border-width: 2px;");
        
        box.getChildren().add(this.fistSelect);
        box.getChildren().add(this.swordSelect);
        
        addRow(2, this.attackBtn);
        addRow(3, box);
        
        box.setSpacing(10);
        
        setHalignment(this.attackBtn, HPos.CENTER);
        setHalignment(box, HPos.CENTER);
        ColorCtx.makeDynamicColor(box);

        addAttackEvent();
        addFistEvent();
        addSwordEvent();
    }

    private String genInfo() {
        return "Your health: " + this.state.getPlayer().getHealth()
                + "\n" + this.state.getEnemy().getName() + " health: " + this.state.getEnemy().getHealth()
                + "\n" + this.state.getMortalToAttack().getName() + "'s turn.";
    }

    private void addAttackEvent() {
        this.attackBtn.setOnAction(e -> {
            this.state.playMove();
            this.descLabel.setText(genInfo() + "\n\n" + this.state.getMortalToAttack().getReactionText());
            if (this.state.gameOver()) {
                gameOver(true);
                return;
            }
            this.attackBtn.setDisable(true);
            PauseTransition pause = new PauseTransition(Duration.millis(1000));
            pause.setOnFinished(event -> {
                this.state.playMove();
                this.descLabel.setText(genInfo() + "\n\n" + this.state.getMortalToAttack().getReactionText());
                if (this.state.gameOver()) {
                    gameOver(false);
                    return;
                }
                this.attackBtn.setDisable(false);
            });
            pause.play();
        });

    }

    private void addFistEvent() {
        this.fistSelect.setOnAction(e -> {
            this.state.getEnemy().setReactionStrategy(new FistAttackReaction());
            ColorCtx.editStyle(this.fistSelect, "-fx-border-color: yellow; -fx-border-width: 2px;");
            ColorCtx.editStyle(this.swordSelect, "-fx-border-color: black; -fx-border-width: 2px;");
        });
    }

    private void addSwordEvent() {
        this.swordSelect.setOnAction(e -> {
            this.state.getEnemy().setReactionStrategy(new SwordAttackReaction());
            ColorCtx.editStyle(this.swordSelect, "-fx-border-color: yellow; -fx-border-width: 2px;");
            ColorCtx.editStyle(this.fistSelect, "-fx-border-color: black; -fx-border-width: 2px;");
        });
    }

    private void gameOver(boolean gameContinue) {
        if (gameContinue) {
            this.descLabel.setText("You are victorious!");

            //add 10 points for murdering a life form
            gameView.model.player.getNumPoints().addPoints(10);

            PauseTransition pause = new PauseTransition(Duration.millis(2000));
            pause.setOnFinished(event -> {
                this.gameView.mainStackPane.getChildren().remove(this);
                this.gameView.gridPane.setDisable(false);
                this.gameView.updateScene("");
                this.gameView.updateItems();
            });
            pause.play();
        }
        else {
            this.descLabel.setText("You have been killed...");
            PauseTransition pause = new PauseTransition(Duration.millis(2000));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        }
    }

    @Override
    public List<Label> getScalableText() {
        return List.of(this.descLabel, this.title);
    }
}
