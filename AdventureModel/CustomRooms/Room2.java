package AdventureModel.CustomRooms;

import java.io.BufferedReader;
import AdventureModel.Player;
import AdventureModel.Room;

/**
 * This class contains the information about a 
 * room in the Adventure Game.
 */
public class Room2 extends Room  {
    /**
     * AdvGameRoom constructor.
     *
     * @param roomName: The name of the room.
     * @param roomNumber: The number of the room.
     * @param roomDescription: The description of the room.
     */

    @Override
    public void OnEnter(Player player) {}

    @Override
    public void OnExit(Player player) {}

    @Override
    public void ParseUniqueData(BufferedReader reader) {}


}
