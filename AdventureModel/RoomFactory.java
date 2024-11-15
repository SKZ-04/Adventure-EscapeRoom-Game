package AdventureModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import AdventureModel.CustomRooms.CakeRoom;
import AdventureModel.CustomRooms.CombatRoom;
import AdventureModel.CustomRooms.DamageRoom;
import AdventureModel.CustomRooms.TestRoomTemplate;
import AdventureModel.Room;

public class RoomFactory {

    /*
     * Factory method for creating rooms
     * The format should be 
     * 
     * RoomType
     * custom room data
     * ----------
     * regular room data
     * 
     * 
     * Example:
     * 
     * Regular
     * ----------
     * 1
     * Outside building
     * You are standing at a small brick building.
     * -----
     * WEST       2
     */
    public static Room parseRoom(String roomRepr) throws IOException {
        BufferedReader buff = new BufferedReader(new StringReader(roomRepr));
        String roomKind = buff.readLine().trim();
        Room room = switch (roomKind) {
            case CombatRoom.RoomName -> new CombatRoom();
            case TestRoomTemplate.RoomName -> new TestRoomTemplate();
            case CakeRoom.RoomName -> new CakeRoom();
            case DamageRoom.RoomName -> new DamageRoom();
            case Room.RoomName ->  new Room();
            default -> null;
        };

        if (room == null) {
            throw new IOException("invalid room: " + roomKind);
        }
        
        room.InitFromFile(buff);
        return room;
    }
}
