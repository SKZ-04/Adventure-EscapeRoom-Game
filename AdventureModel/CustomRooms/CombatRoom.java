package AdventureModel.CustomRooms;

import java.io.BufferedReader;

import AdventureModel.Player;
import AdventureModel.Room;
import javafx.scene.control.Label;

// refer to Rooms class for meaning of methods
public class CombatRoom extends Room {
    

    @Override
    public String lookHook(){ 
        // Custom Look msg goes here
        // return ""; 
        throw new UnsupportedOperationException("Unimplemented method 'lookHook'");
    }

    
    @Override
    public boolean commandHook(Label label){ 
        // custom commands go here
        // return true; 
        throw new UnsupportedOperationException("Unimplemented method 'commandHook'");
    }

    @Override
    public boolean submitEventHook(String input){ 
        // code to handle text based input.
        // change to implement custom commands
        // Return true if original submitEvent should run otherwise return false
        // should at some point return false to revert text commands back to normal
        // return false; 
        throw new UnsupportedOperationException("Unimplemented method 'submitEventHook'");
    }

    @Override
    public void ParseUniqueData(BufferedReader reader) {
        // parse data from file, format how ever you like
       throw new UnsupportedOperationException("Unimplemented method 'ParseUniqueData'");
    }

    @Override
    public void OnEnter(Player player) {
        // enter new context for combat
       throw new UnsupportedOperationException("Unimplemented method 'OnEnter'");
    }

    @Override
    public void OnExit(Player player) {
        // leave combat context
       throw new UnsupportedOperationException("Unimplemented method 'OnExit'");
    }
    
}
