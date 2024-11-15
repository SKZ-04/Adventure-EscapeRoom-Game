package AdventureModel.Combat.Reactions;

public class GeneralEnemyAttackReaction extends Reaction{

    public GeneralEnemyAttackReaction() {
        this.damage = 1;
    }
    @Override
    public String react(String name) {
        return name + " is struck by the enemy";
    }
}
