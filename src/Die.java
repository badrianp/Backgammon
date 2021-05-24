import java.util.Random;

public class Die implements Comparable<Die> {
    private int value;

    public Die() {
        rollDie();
    }

    public void rollDie() {
        setValue(roll());
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

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public int compareTo(Die o) {
        return Integer.compare(this.getValue(), o.getValue());
    }
}
