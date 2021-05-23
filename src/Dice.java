import java.util.Random;

public class Dice {

    private Die die1;
    private Die die2;
    private boolean isDouble;


    public Dice() {
        die1 = new Die();
        die2 = new Die();
    }

    public void rollDice() {
        die1 = new Die();
        die2 = new Die();
        setDouble(die1.getValue()==die2.getValue());
    }

    public Die getDie1() {
        return this.die1;
    }

    public void setDie1(Die die1) {
        this.die1 = die1;
    }

    public Die getDie2() {
        return this.die2;
    }

    public void setDie2(Die die2) {
        this.die2 = die2;
    }

    public boolean isDouble() {
        return isDouble;
    }

    public void setDouble(boolean aDouble) {
        isDouble = aDouble;
    }
}
