package AdventureModel.CustomRooms;

import java.io.BufferedReader;
import java.io.IOException;

import AdventureModel.Player;
import AdventureModel.Room;
import javafx.scene.control.Label;

/**
 * This class contains the information about a 
 * room in the Adventure Game.
 */
public class TestRoomTemplate extends Room  {
    
    public static final String RoomName = "TEST";

    @Override
    public String lookHook(){ 
        throw new UnsupportedOperationException("Unimplemented method 'lookHook'");
    }

    
    @Override
    public boolean commandHook(Label label){ 
        throw new UnsupportedOperationException("Unimplemented method 'commandHook'");
    }

    @Override
    public boolean submitEventHook(String input){ 
        throw new UnsupportedOperationException("Unimplemented method 'submitEventHook'");
    }

    @Override
    public void ParseUniqueData(BufferedReader reader) throws IOException{
       throw new UnsupportedOperationException("Unimplemented method 'ParseUniqueData'");
    }

    @Override
    public void OnEnter(Player player) {
       throw new UnsupportedOperationException("Unimplemented method 'OnEnter'");
    }

    @Override
    public void OnExit(Player player) {
       throw new UnsupportedOperationException("Unimplemented method 'OnExit'");
    }

}
