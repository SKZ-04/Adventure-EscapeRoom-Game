package AdventureModel;

import java.io.Serializable;

import AdventureModel.Combat.Reactions.Reaction;

public class Mortal implements Serializable {
    private int health;
    private String name;
    private Reaction reactionStrategy;

    private String reactionText;
    public Mortal(int health, String name){
        this.health = health;
        this.name = name;
    }
    public int getHealth(){
        return health;
    }

    public boolean isAlive(){
        return health > 0;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) {
            health = 0;
        }
    }

    public String getName() {return name;}

    public void setReactionStrategy(Reaction reactionStrategy) {
        this.reactionStrategy = reactionStrategy;
    }

    public void reactToAttack() {
        reactionText = reactionStrategy.react(name);
        health -= reactionStrategy.getDamage();
    }

    public String getReactionText() {
        return reactionText;
    }

    public String getInfo() {
        return "Name: " + name + "\nHealth: " + health;
    }
}
