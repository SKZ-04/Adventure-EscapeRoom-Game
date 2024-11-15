package AdventureModel;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import views.AdventureGameView;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;


public class ColorCtx {
    private String background;
    private String textFill;
    private static int styleMode = 0;
    private static ArrayList<Node> items = new ArrayList<Node>();
    private static AdventureGameView game; 
    private static HashMap<Node, String> additionalStyles = new HashMap<>();
    public final static ColorCtx[] Button = new ColorCtx[]{
        new ColorCtx("#17871b", "white"),        
        new ColorCtx("#17871b", "black"),
    };
    private final static ColorCtx[] Label = new ColorCtx[]{
        new ColorCtx(null, "white"),
        new ColorCtx(null, "black"),
    };
    private final static ColorCtx[] Container = new ColorCtx[]{
        new ColorCtx("#121212", "white"),
        new ColorCtx("white", "#121212"),
    };
    private final static ColorCtx[] TextField = new ColorCtx[]{
        new ColorCtx("#000000", "gray"),
        new ColorCtx("gray", "#000000"),
    };
    private ColorCtx(String background, String textFill){
        this.background = background;
        this.textFill = textFill;
    }

    public static void init_game(AdventureGameView view){
        game = view;
    }

    public static void setLightMode(){
        ColorCtx.styleMode = 1;
        UpdateColors();
    }
    public static void setDarkMode(){
        ColorCtx.styleMode = 0;
        UpdateColors();
    }
    private static void UpdateColors(){
        for (Node e: ColorCtx.items){
            colorize(e);
        }
        if (game != null) game.updateColor();
    }

    public String toStyle(){
        String background = (this.background == null)
            ? "" 
            : "-fx-background-color: " + this.background;
        String textFill = (this.textFill == null)
            ? "" 
            : "-fx-text-fill: " + this.textFill;
        return background + "; " + textFill + ";";
    }

    public static void makeDynamicColor(Node e){
        colorize(e);
        ColorCtx.items.add(e);
    }
    public static void makeDynamicColorWithStyle(Node e, String style){
        additionalStyles.put(e, style);
        colorize(e);
        ColorCtx.items.add(e);
    }

    public static void editStyle(Node e, String style){
        additionalStyles.put(e, style);
        colorize(e);
    }

    public static void colorize(Node e){
        ColorCtx[] kind;
        if (e instanceof Button){
            kind = ColorCtx.Button;
        } else if (e instanceof Label){
            kind = ColorCtx.Label;
        } else if (e instanceof TextField){
            kind = ColorCtx.TextField;
        } else if (e instanceof ScrollPane){
            Node node = ((ScrollPane)e).lookup(".viewport");
            if (node != null) ColorCtx.colorize(node);
            return; 
        } else {
            kind = ColorCtx.Container;
        }
        String style = kind[ColorCtx.styleMode].toStyle();
        String additional = additionalStyles.get(e);
        if (additional != null){
            style += " " +additional;
        }
        e.setStyle(style);   
    }
}
