package AdventureModel.CustomRooms;

import java.io.BufferedReader;
import java.io.IOException;

import AdventureModel.Player;
import AdventureModel.Room;

/**
 * This class contains the information about a 
 * room in the Adventure Game.
 */
public class DamageRoom extends Room  {
    
    public static final String RoomName = "Damage";
    private String cause;
    private int damageAmount;

    private int _damageTaken;

    @Override
    public void ParseUniqueData(BufferedReader buff) throws IOException{
        this.cause = buff.readLine();
        this.damageAmount = Integer.parseInt(buff.readLine());
    }

    @Override
    public String lookHook(){ 
        return this.cause + ". you took " + this._damageTaken + " damage.";
    }

    @Override
    public void OnEnter(Player player) {
        int health = player.getHealth();
        this._damageTaken = (this.damageAmount > health)
            ? _damageTaken = health - 1
            : this.damageAmount;
        player.takeDamage(this._damageTaken);
    }
}
