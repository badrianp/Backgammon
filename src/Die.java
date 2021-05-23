import java.util.Random;

public class Die {
    private int value;

    public Die() {
        rollDie();
    }

    private void rollDie() {
        this.value = roll();
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int roll() {
        return new Random().nextInt(6) + 1;
    }
}
