import java.util.Objects;

public class Move implements Comparable<Move>{
    private int fromPosition;
    private int toPosition;

    public Move(int fromPosition, int toPosition) {
        setFromPosition(fromPosition);
        setToPosition(toPosition);
    }

    public Move() {
    }

    public int getFromPosition() {
        return fromPosition;
    }

    public void setFromPosition(int fromPosition) {
        this.fromPosition = fromPosition;
    }

    public int getToPosition() {
        return toPosition;
    }

    public void setToPosition(int toPosition) {
        this.toPosition = toPosition;
    }


    @Override
    public int compareTo(Move move) {
        return this.getFromPosition() == move.getFromPosition()
                && this.getToPosition() == move.getToPosition() ? 0 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return fromPosition == move.fromPosition && toPosition == move.toPosition;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromPosition, toPosition);
    }

    @Override
    public String toString() {
        return "move: " + fromPosition +
                "-->" + toPosition;
    }
}
