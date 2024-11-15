package AdventureModel.Combat.Reactions;

public class FistAttackReaction extends Reaction{

    public FistAttackReaction() {
        this.damage = 1;
    }
    @Override
    public String react(String name) {
        return name + " is infuriated.";
    }
}
