public class Move implements Comparable{
    private int fromPosition;
    private int toPosition;
    private int count;

    public Move(int fromPosition, int toPosition) {
        setFromPosition(fromPosition);
        setToPosition(toPosition);
        setCount(count);
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public int compareTo(Object o) {
        Move move = (Move) o;
        return this.getFromPosition() == ((Move) o).getFromPosition() && this.getToPosition() == move.getToPosition() ? 0 : 1;
    }

    @Override
    public String toString() {
        return "move: " + fromPosition +
                "-->" + toPosition;
    }
}
