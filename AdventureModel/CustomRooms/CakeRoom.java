package AdventureModel.CustomRooms;

import AdventureModel.Player;
import AdventureModel.Room;

public class CakeRoom extends Room {
    
    public static final String RoomName = "Cake";
    private boolean ateCake;
    
    @Override
    public String lookHook(){ 
        return this.ateCake 
            ? "There is no more cake. you are sad"
            : "There is a cake. You ate the cake";
    }

    @Override
    public void OnEnter(Player player) {
        if (!this.ateCake){
            player.takeDamage(-5);
        }
    }

    @Override
    public void OnExit(Player player){
        this.ateCake = true;
    }

}
