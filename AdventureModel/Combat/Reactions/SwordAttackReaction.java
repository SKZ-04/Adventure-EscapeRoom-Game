package AdventureModel.Combat.Reactions;

public class SwordAttackReaction extends Reaction{

    public SwordAttackReaction() {
        this.damage = 2;
    }
    @Override
    public String react(String name) {
        return name + " is cut and is bleeding out.";
    }
}
