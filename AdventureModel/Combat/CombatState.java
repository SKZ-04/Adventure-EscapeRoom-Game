package AdventureModel.Combat;
import AdventureModel.Combat.Reactions.FistAttackReaction;
import AdventureModel.Combat.Reactions.GeneralEnemyAttackReaction;
import AdventureModel.Player;
import AdventureModel.Enemy;
import AdventureModel.Mortal;
public class CombatState {

    private final Player player;
    private final Enemy enemy;
    private Mortal turnToBeAttacked;
    public CombatState(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.turnToBeAttacked = enemy;
        this.enemy.setReactionStrategy(new FistAttackReaction());
        this.player.setReactionStrategy(new GeneralEnemyAttackReaction());
    }

    private void toggleTurn() {
        if (this.turnToBeAttacked.equals(this.player)) {
            this.turnToBeAttacked = this.enemy;
        }
        else {
            this.turnToBeAttacked = this.player;
        }
    }

    public void playMove() {
        this.turnToBeAttacked.reactToAttack();
        toggleTurn();
    }

    public boolean gameOver() {
        // Switch to the attacking fighter
        toggleTurn();
        // Check if it is alive
        boolean temp = this.turnToBeAttacked.isAlive();
        // Switch back
        toggleTurn();
        return !temp;
    }

    public Mortal getMortalToAttack() {
        if (this.turnToBeAttacked.equals(this.player)) {
            return this.enemy;
        }
        else {
            return this.player;
        }
    }

    public Mortal getPlayer() {
        return this.player;
    }

    public Mortal getEnemy() {
        return this.enemy;
    }
}
