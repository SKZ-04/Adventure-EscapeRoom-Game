package AdventureModel;

/*The Enemy class; represents the enemies in the game*/
public class Enemy extends Mortal {
    private String name;
    public Enemy(int health, String name) {
        super(health, name);
    }
}
