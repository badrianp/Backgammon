import java.util.ArrayList;
import java.util.List;

public class ValidMoves {
    private List<Move> moveList = new ArrayList<>();
    boolean hasKicked;

    public ValidMoves() {
//        moveList = null;
    }

    public List<Move> getMoveList(Board board, Color color, Die die) throws Exception {
        if (die.getValue() == 0) {
            throw new Exception("roll your die first");
        }
        initValidMoves(board, color, die);
        return moveList;
    }

    private void initValidMoves(Board board, Color color, Die die) {
        moveList.clear();
        int dieVal = die.getValue();
        boolean canExtract = true;
        /*
            choose color
         */
        switch (color){

            case WHITE:
            /*
                check kicked checkers
             */
                if (board.getList().get(25).isEmpty()) {
                    hasKicked = false;  //no kicked checkers
                    for (int fromPosition = 0; fromPosition < 23-(canExtract ? 0 : dieVal); fromPosition++) {
                        if (!board.getList().get(fromPosition).isEmpty()) {
                            if (board.getList().get(fromPosition).peek().getColor().equals(Color.WHITE) ) {
                                if (fromPosition < 18)
                                    canExtract = false;
                                if( board.getList().get(fromPosition + dieVal).size() <=1
                                        || board.getList().get(fromPosition + dieVal).peek().getColor() != Color.BLACK )
                                    moveList.add(new Move(fromPosition, (fromPosition+dieVal > 23 ? 26 : fromPosition+dieVal)));
                            }
                        }
                    }


                } else {
                    hasKicked = true;   // it has kicked checkers
                    /*
                        check for move with die1
                     */
                    if ( (board.getList().get(0 + dieVal - 1).size()<=1 )                                   //free or only one enemy checker
                            || (board.getList().get(0 + dieVal - 1).peek().getColor() != Color.BLACK) ) {   //my checkers' color
                        moveList.add(new Move(25, 0+dieVal-1));
                    }

                }
                break;

            case BLACK:
            /*
                check kicked checkers
            */
                if (board.getList().get(24).isEmpty()) {
                    hasKicked = false;  //no kicked checkers
                    for (int fromPosition = 23; fromPosition >= (canExtract ? 0 : dieVal); fromPosition--) {
                        if (!board.getList().get(fromPosition).isEmpty()) {
                            if (board.getList().get(fromPosition).peek().getColor().equals(Color.BLACK)) {
                                if (fromPosition > 5)
                                    canExtract = false;
                                if (board.getList().get(fromPosition - dieVal).size() <= 1
                                        || board.getList().get(fromPosition - dieVal).peek().getColor() != Color.WHITE)
                                    moveList.add(new Move(fromPosition, (fromPosition - dieVal < 0 ? 27 : fromPosition - dieVal)));
                            }
                        }
                    }
                } else {
                    hasKicked = true;   // it has kicked checkers
                    /*
                        check for move with die1
                     */
                    if ( (board.getList().get(23 - dieVal + 1).size()<=1 )                                   //free or only one enemy checker
                            || (board.getList().get(23 - dieVal + 1).peek().getColor() != Color.WHITE) ) {   //my checkers' color
                        moveList.add(new Move(24, 23 - dieVal + 1));
                    }
                }
                break;
        }

    }
}
