package gameplay;

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
    private List<Stack<Checker>> places = new ArrayList<>();

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


//        for (int i = 0; i < size; i++) {
//            this.places.add(i, new Stack<gameplay.Checker>());
//            switch (i) {
//                case 1,2,4 -> addCheckers(i, 1, gameplay.Color.BLACK);
//                case 19,21,22 -> addCheckers(i, 1, gameplay.Color.WHITE);
//                case 26 -> addCheckers(i, 12, gameplay.Color.BLACK);
//                case 27 -> addCheckers(i, 12, gameplay.Color.WHITE);
//                default -> addCheckers(i, 0, null);
//            }
//        }
    }

    private void addCheckers(int position, int num, Color color) {
        while (num != 0) {
            num--;
            places.get(position).push(new Checker(color));
        }
    }

    public void moveCheckers(Move move) {
        if ( places.get(move.getToPosition()).size() == 1) {
            if (places.get(move.getFromPosition()).peek().getColor() != places.get(move.getToPosition()).peek().getColor())
            places.get((places.get(move.getToPosition()).peek().getColor() == Color.BLACK ? 24 : 25)).push(places.get(move.getToPosition()).pop());
        }
        places.get(move.getToPosition()).push(places.get(move.getFromPosition()).pop());
    }

    /*
    method used to move checkers for blocking/ unblocking test. It does not kick opponent's vulnerable checker
     */
    public void testMoveCheckers(Move move) {
        places.get(move.getToPosition()).push(places.get(move.getFromPosition()).pop());
    }

    public void printBoard() {

        System.out.print("|-----------------------------------------------------------------------------------------------------------------------------------------------|{-----------------------------------------------------[  BLACK's    home   board  ]-------------------------------------------------------}|\n");

        System.out.print("|");
        for (int i = 11; i >= 0; i--) {
            System.out.print("\t|place["+i+"]|\t");
            if (i == 6) {
                System.out.print("|");
            }
        }
        System.out.print("|\n|-----------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|\n|");
        for (int i = 11; i >= 0; i--) {
            System.out.print((places.get(i).isEmpty() ? "\t   [ ]   \t" : "\t" + places.get(i).peek().getColor()  + " (" + places.get(i).size() + ")\t"));
            if (i == 6)
                System.out.print("|");
        }
        System.out.print("|\n|\t                                                                                                                                        |                                                                                                                                           |\n|\t                                                                                                                                        |                                                                                                                                           |\n|-----------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|\n|\t                                                                                                                                        |                                                                                                                                           |\n|\t                                                                                                                                        |                                                                                                                                           |\n|\t                                                                                                                                        |                                                                                                                                           |\n|");
        for (int i = 12; i < 24; i++) {
            System.out.print((places.get(i).isEmpty() ? "\t   [ ]   \t" :  "\t" + places.get(i).peek().getColor()  + " ( " + places.get(i).size() + " )\t"));
            if (i == 17)
                System.out.print("|");
        }
        System.out.print("|\n|-----------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------|\n|");
        for (int i = 12; i < 24; i++) {
            System.out.print("\t|place["+i+"]|\t");
            if (i == 17) {
                System.out.print("|");
            }
        }
        System.out.print("|\n|-----------------------------------------------------------------------------------------------------------------------------------------------|{-----------------------------------------------------[  WHITE's    home   board  ]-------------------------------------------------------}|\n");
        System.out.println("\n[24]BLACK's kicked: " + (places.get(24).isEmpty() ? "\t\tnone\t" : "\t" + places.get(24).size() + "*" + places.get(24).peek().getColor()));
        System.out.println("[25]WHITE's kicked: " + (places.get(25).isEmpty() ? "\t\tnone\t" : "\t" + places.get(25).size() + "*" + places.get(25).peek().getColor()));

        System.out.println("[26]BLACK's home: " + (places.get(26).isEmpty() ? "\t\tnone\t" : "\t" + places.get(26).size() + "*" + places.get(26).peek().getColor()));
        System.out.println("[27]WHITE's home: " + (places.get(27).isEmpty() ? "\t\tnone\t" : "\t" + places.get(27).size() + "*" + places.get(27).peek().getColor()) + "\n");
    }

    public Color checkWinner() {
        if (places.get(27).size() == 15 )
            return Color.WHITE;

        if (places.get(26).size() == 15)
            return Color.BLACK;

        return martTechnic();
    }

    public Color martTechnic() {
        if (places.get(26).size() == 3) {
            boolean mart = true;
            for (int index = 5; index >=0; index --) {
                if (places.get(index).size() != 2) {
                    mart = false;
                    break;
                }
            }
            if (mart) return Color.BLACK;
        }
        if (places.get(27).size() == 3) {
            boolean mart = true;
            for (int index = 18; index <= 23; index ++) {
                if (places.get(index).size() != 2) {
                    mart = false;
                    break;
                }
            }
            if (mart) return Color.WHITE;
        }
        return null;
    }

    public List<Stack<Checker>> getPlaces() {
        return places;
    }

    public void setPlaces(List<Stack<Checker>> places) {
        this.places.clear();
        this.places.addAll(places);
    }

}
