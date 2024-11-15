package AdventureModel;

import java.io.Serializable;

public class Points implements Serializable {

    private int points;

    private Player player;

    public Points(Player player) {
        this.player = player;
        this.points = 0;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }

}
