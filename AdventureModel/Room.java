package AdventureModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javafx.scene.control.Label;
/*
 * Base class for creating custom rooms 
 * which will be found in the game.
 * 
 * Note:
 * Custom rooms with forced movement should handle forced direction
 * When creating a new subclass, update roomFactory
 */
public class Room implements Serializable {

    // private final String adventureName;
    public static final String RoomName = "Regular";
    /**
     * The number of the room.
     */
    protected int roomNumber;

    /**
     * The name of the room.
     */
    protected String roomName;

    /**
     * The description of the room.
     */
    protected String roomDescription;

    /**
     * The passage table for the room.
     */
    protected PassageTable motionTable = new PassageTable();

    /**
     * The list of objects in the room.
     */
    public ArrayList<AdventureObject> objectsInRoom = new ArrayList<AdventureObject>();

    /**
     * A boolean to store if the room has been visited or not
     */
    protected boolean isVisited;

    // Stores the enemy; initialized to null.
    private Enemy enemyInRoom = null;

    // Allocates memory for Room, use InitFromFile to initialize an instance. 
    public Room(){}

    /*
     * Behavior to be ran immediately upon entering a room 
     */
    public void OnEnter(Player player){}

    /*
     * Behavior to be ran immediately upon exiting a room 
     */
    public void OnExit(Player player){}

    /*
     * initialize subclass data from file
     */
    public void ParseUniqueData(BufferedReader reader) throws IOException {}

    /*
     * Hook to implement custom behavior for text based input
     * Return true if original submitEvent should run otherwise return false
     */
    public boolean submitEventHook(String input){ return false; }
    
    /*
     * Hook to add additional information for 
     * when player uses the "look" command  
     */
    public String lookHook(){ return ""; }

    /*
     * Hook to set the result when the player uses the "command" command.
     * Return true if original showCommands should be prepended to custom commands
     */
    public boolean commandHook(Label label){ return true; }

    /*
     * initializes the data common of all rooms, ie (exit directions, objects, etc)
     */
    private void InitBase(BufferedReader buff) throws IOException{
        String currRoom = buff.readLine(); // first line is the number of a room

        this.roomNumber = Integer.parseInt(currRoom); //current room number

        // now need to get room name
        String roomName = buff.readLine();

        // now we need to get the description
        String roomDescription = "";
        String line = buff.readLine();
        while (!line.equals("-----")) {
            roomDescription += line + "\n";
            line = buff.readLine();
        }
        roomDescription += "\n";

        // now we make the room object
        this.roomName = roomName;
        this.roomDescription = roomDescription;

        // now we make the motion table
        line = buff.readLine(); // reads the line after "-----"
        while (line != null && !line.equals("")) {
            String[] part = line.split(" \s+"); // have to use regex \\s+ as we don't know how many spaces are between the direction and the room number
            String direction = part[0];
            String dest = part[1];
            if (dest.contains("/")) {
                String[] blockedPath = dest.split("/");
                String dest_part = blockedPath[0];
                String object = blockedPath[1];
                Passage entry = new Passage(direction, dest_part, object);
                this.getMotionTable().addDirection(entry);
            } else {
                Passage entry = new Passage(direction, dest);
                this.getMotionTable().addDirection(entry);
            }
            line = buff.readLine();
        }
    }
    
    /*
     * main way to instantiate a room object. called by roomFactory
     */
    public final void InitFromFile(BufferedReader buff) throws IOException{
        this.ParseUniqueData(buff);
        String maybeErrMsg = buff.readLine();
        if (!maybeErrMsg.equals("-----")) throw new IOException("error occurred while parsing, found: " + maybeErrMsg);
        this.InitBase(buff);
    }
    
    /**
     * Returns a comma delimited list of every
     * object's description that is in the given room,
     * e.g. "a can of tuna, a beagle, a lamp".
     *
     * @return delimited string of object descriptions
     */
    public String getObjectString() {
        StringBuilder allObjectDesc = new StringBuilder();

        for (AdventureObject object: objectsInRoom) {
            allObjectDesc.append(object.getDescription() + ",");
        }
        return allObjectDesc.toString().replaceAll("^,|,$", "");
    }

    public String getEnemyString() {
        if (enemyInRoom == null) {
            return "There is no enemy in this room";
        }
        else if (enemyInRoom.isAlive()) {
            return "There is an enemy in this room:\n" + enemyInRoom.getInfo();
        }
        else {
            return "A " + enemyInRoom.getName() + " lays dead on the ground.";
        }
    }

    /**
     * Returns a comma delimited list of every
     * move that is possible from the given room,
     * e.g. "DOWN, UP, NORTH, SOUTH".
     *
     * @return delimited string of possible moves
     */
    public String getCommands() {
        StringBuilder possibleMoves = new StringBuilder();

        for (Passage passage: motionTable.passageTable) {
            possibleMoves.append(passage.getDirection() + ",");
        }

        possibleMoves.delete(possibleMoves.length()-1, possibleMoves.length());
        return possibleMoves.toString();
    }

    /**
     * This method adds a game object to the room.
     *
     * @param object to be added to the room.
     */
    public void addGameObject(AdventureObject object){
        this.objectsInRoom.add(object);
    }

    public void addEnemy(Enemy e) {this.enemyInRoom = e;}

    /**
     * This method removes a game object from the room.
     *
     * @param object to be removed from the room.
     */
    public void removeGameObject(AdventureObject object){
        this.objectsInRoom.remove(object);
    }

    /**
     * This method checks if an object is in the room.
     *
     * @param objectName Name of the object to be checked.
     * @return true if the object is present in the room, false otherwise.
     */
    public boolean checkIfObjectInRoom(String objectName){
        for(int i = 0; i<objectsInRoom.size();i++){
            if(this.objectsInRoom.get(i).getName().equals(objectName)) return true;
        }
        return false;
    }

    /**
     * Sets the visit status of the room to true.
     */
    public void visit(){
        isVisited = true;
    }

    /**
     * Getter for returning an AdventureObject with a given name
     *
     * @param objectName: Object name to find in the room
     * @return: AdventureObject
     */
    public AdventureObject getObject(String objectName){
        for(int i = 0; i<objectsInRoom.size();i++){
            if(this.objectsInRoom.get(i).getName().equals(objectName)) return this.objectsInRoom.get(i);
        }
        return null;
    }

    /**
     * Getter method for the number attribute.
     *
     * @return: number of the room
     */
    public int getRoomNumber(){
        return this.roomNumber;
    }

    /**
     * Getter method for the description attribute.
     *
     * @return: description of the room
     */
    public String getRoomDescription(){
        return this.roomDescription.replace("\n", " ");
    }


    /**
     * Getter method for the name attribute.
     *
     * @return: name of the room
     */
    public String getRoomName(){
        return this.roomName;
    }

    public Enemy getEnemy() {return this.enemyInRoom; }


    /**
     * Getter method for the visit attribute.
     *
     * @return: visit status of the room
     */
    public boolean getVisited(){
        return this.isVisited;
    }


    /**
     * Getter method for the motionTable attribute.
     *
     * @return: motion table of the room
     */
    public PassageTable getMotionTable(){
        return this.motionTable;
    }

}
