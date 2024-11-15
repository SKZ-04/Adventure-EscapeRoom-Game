package AdventureModel.Combat.Reactions;

public abstract class Reaction {
    protected int damage;

    public int getDamage() {
        return damage;
    }

    abstract public String react(String name);
}
