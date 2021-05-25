package gameplay;

import java.util.Locale;

public class Player {
    private Mode mode;
    private Color color;
    private String name;
    private boolean isComputer;
    private boolean turn;
    private boolean isWinner;

    public Player(String name, boolean isComputer, Color color) {
        setName(name);
        setComputer(isComputer);
        setColor(color);
        setTurn(false);
        setWinner(false);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setWinner(boolean winner) {
        isWinner = winner;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComputer() {
        return isComputer;
    }

    public void setComputer(boolean computer) {
        isComputer = computer;
    }

    @Override
    public String toString() {
        return "gameplay.Player "
                + name + " ( "
                + color.toString().toUpperCase(Locale.ROOT) + " )"
                + (isComputer ? " [computer]" : "" );
    }
}
