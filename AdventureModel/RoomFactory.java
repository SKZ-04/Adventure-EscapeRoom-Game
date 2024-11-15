package AdventureModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import AdventureModel.CustomRooms.CombatRoom;
import AdventureModel.CustomRooms.Room2;
import AdventureModel.Room;

public class RoomFactory {

    /*
     * Factory method for creating rooms
     * The format should be 
     * 
     * RoomType
     * ----------
     * custom room data
     * ----------
     * regular room data
     * ----------
     * 
     * 
     * Example:
     * 
     * Regular
     * ---------- 
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
            case "Combat" -> new CombatRoom();
            case "Regular" -> new Room2();
            default -> null;
        };

        if (room == null) {
            throw new IOException("invalid room: " + roomKind);
        }
        room.InitFromFile(buff);
        return room;
    }
}
