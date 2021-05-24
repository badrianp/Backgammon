import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Board {

    /*
                            /--> black's home board
                           /
                 ,,,,,,,(,,,,,,) ->> array start  === (places[0])
                 |      |      |
 blacks L[24]<<- |----->[]     |
                 | __ __|__ __ |
 whites L[25]<<- |----->[]     |
                 |      |      |
                 ```````{``````} ->> array end    === (places[23])
                            \
                             \--> white's home board


     */
    public static final String blacksDirectionOfTravel = "clockwise";
    public static final String whitesDirectionOfTravel = "counter-clockwise";

    /*
       #0 <-> #23 === the board itself
       #24 --> White's kicked checkers
       #25 --> Black's kicked checkers
       #26 --> white's home
       #27 --> black's home
   */
    public static final int size = 28;

    /*
        List of checkers' stacks
     */
    private final List<Stack<Checker>> places = new ArrayList<>();

    public Board() {
        initBoard();
    }

    public List<Stack<Checker>> getList() {
        return this.places;
    }

    private void initBoard() {

        for (int i = 0; i < size; i++) {
            this.places.add(i, new Stack<Checker>());
            switch (i) {
                case 0 -> addCheckers(i, 2, Color.WHITE);
                case 5, 12 -> addCheckers(i, 5, Color.BLACK);
                case 7 -> addCheckers(i, 3, Color.BLACK);
                case 11, 18 -> addCheckers(i, 5, Color.WHITE);
                case 16 -> addCheckers(i, 3, Color.WHITE);
                case 23 -> addCheckers(i, 2, Color.BLACK);
                default -> addCheckers(i, 0, null);
            }
        }


    }

    private void addCheckers(int position, int num, Color color) {
        while (num != 0) {
            num--;
            places.get(position).push(new Checker(color));
        }
    }

    public void moveCheckers(Move move) {
        places.get(move.getToPosition()).push(places.get(move.getFromPosition()).pop());
    }

    public void moveMoreCheckers(Move move, int num) {
        places.get(move.getToPosition()).push(places.get(move.getFromPosition()).pop());
    }

    public void printBoard() {

        for (int i = 0; i < places.size(); i++) {
            System.out.println(i + ": " + (places.get(i).isEmpty() ? "free" : places.get(i).size() + "*" + places.get(i).peek().getColor()));
        }

    }

    public Color checkWinner() {
        if (places.get(26).size() == 15)
            return Color.WHITE;

        if (places.get(27).size() == 15)
            return Color.BLACK;

        return null;
    }



}
