package gameplay;

public class Dice {

    private Die die1;
    private Die die2;

    public Dice() {
        die1 = new Die();
        die2 = new Die();
    }

    public void rollDice() {
        die1 = new Die();
        die2 = new Die();
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
        return (die1.getValue() == die2.getValue());
    }

    @Override
    public String toString() {
        return  "die1 = '" + die1 +
                "' and die2 = '" + die2 + "'";
    }
}
