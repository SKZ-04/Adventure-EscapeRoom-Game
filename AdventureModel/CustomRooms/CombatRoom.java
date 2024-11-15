package AdventureModel.CustomRooms;

import java.io.BufferedReader;
import java.io.IOException;

import AdventureModel.Player;
import AdventureModel.Room;
import javafx.scene.control.Label;

// refer to Rooms class for meaning of methods
public class CombatRoom extends Room {
    public static final String RoomName = "Combat";
    
    @Override
    public boolean commandHook(Label label){ 
        label.setText("Type Fight or f to enter combat with enemy");
        return true;
    }
}
