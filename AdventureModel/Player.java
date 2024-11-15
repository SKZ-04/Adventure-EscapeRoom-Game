package AdventureModel;

import java.io.Serializable;
import java.util.ArrayList;

import AdventureModel.Room;

/**
 * This class keeps track of the progress
 * of the player in the game.
 */
public class Player extends Mortal {
    /**
     * The current room that the player is located in.
     */
    private Room currentRoom;


    /**
     * The number of points that player has.
     */
    private Points numPoints;

    /**
     * The name of the player.
     */
    private String name;

    /**
     * The list of items that the player is carrying at the moment.
     */
    public ArrayList<AdventureObject> inventory;

    /**
     * Adventure Game Player Constructor
     */
    public Player(Room currentRoom) {
        super(4, "Player");
        this.inventory = new ArrayList<AdventureObject>();
        this.currentRoom = currentRoom;
        this.numPoints = new Points(this);
        this.name = null;
    }

    /**
     * This method adds an object into players inventory if the object is present in
     * the room and returns true. If the object is not present in the room, the method
     * returns false.
     *
     * @param object name of the object to pick up
     * @return true if picked up, false otherwise
     */
    public boolean takeObject(String object){
        if(this.currentRoom.checkIfObjectInRoom(object)){
            AdventureObject object1 = this.currentRoom.getObject(object);
            this.currentRoom.removeGameObject(object1);
            this.addToInventory(object1);
            return true;
        } else {
            return false;
        }
    }


    /**
     * checkIfObjectInInventory
     * __________________________
     * This method checks to see if an object is in a player's inventory.
     *
     * @param s the name of the object
     * @return true if object is in inventory, false otherwise
     */
    public boolean checkIfObjectInInventory(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) return true;
        }
        return false;
    }


    /**
     * This method drops an object in the players inventory and adds it to the room.
     * If the object is not in the inventory, the method does nothing.
     *
     * @param s name of the object to drop
     */
    public void dropObject(String s) {
        for(int i = 0; i<this.inventory.size();i++){
            if(this.inventory.get(i).getName().equals(s)) {
                this.currentRoom.addGameObject(this.inventory.get(i));
                this.inventory.remove(i);
            }
        }
    }

    /**
     * Setter method for the current room attribute.
     *
     * @param currentRoom The location of the player in the game.
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Getter method for the name attribute.
     *
     * @return name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for the name attribute.
     *
     * @param name The name of the player in the game.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter method for the numPoints attribute.
     *
     * @return number of points the player has.
     */
    public Points getNumPoints() {
        return numPoints;
    }

    /**
     * This method adds an object to the inventory of the player.
     *
     * @param object Prop or object to be added to the inventory.
     */
    public void addToInventory(AdventureObject object) {
        this.inventory.add(object);
    }


    /**
     * Getter method for the current room attribute.
     *
     * @return current room of the player.
     */
    public Room getCurrentRoom() {
        return this.currentRoom;
    }

    /**
     * Getter method for string representation of inventory.
     *
     * @return ArrayList of names of items that the player has.
     */
    public ArrayList<String> getInventory() {
        ArrayList<String> objects = new ArrayList<>();
        for (int i = 0; i < this.inventory.size(); i++) {
            objects.add(this.inventory.get(i).getName());
        }
        return objects;
    }
}
