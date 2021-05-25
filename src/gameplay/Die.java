package gameplay;

import java.util.Random;

public class Die implements Comparable<Die> {
    private int value;

    public Die() {
        rollDie();
    }

    public Die(int value) {
        setValue(value);
    }

    public void rollDie() {
        int newVal = roll();
        setValue(newVal);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Die o) {
        if ( o == null) return 1;
        return Integer.compare(this.getValue(), o.getValue());
    }

    public int roll() {
        return new Random().nextInt(6) + 1;
    }
}
