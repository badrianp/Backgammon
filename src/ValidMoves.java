import java.util.ArrayList;
import java.util.List;

public class ValidMoves {

    private Board board;
    private Color color;
    private Die die1;
    private Die die2;
    private List<Move> moveList1 = new ArrayList<>();
    private List<Move> moveList2 = new ArrayList<>();
    private List<Move> moveList = new ArrayList<>();

    public List<Move> getMoveList(int dieNum) {
        moveList.addAll(moveList1);
        moveList.addAll(moveList2);
//        if (dieNum == 1) {
//            return moveList1;
//        }
//        return moveList2;
        return moveList;
    }

    public ValidMoves(Board board, Player player, Dice dice) {
        setBoard(board);
        setColor(player.getColor());
        setDie1(dice.getDie1());
        setDie2(dice.getDie2());
        validate();
    }

    private void initValidMoves() {
        setMoveList1(initValidMovesForDie(die1, this.board));
        setMoveList2(initValidMovesForDie(die2, this.board));
    }

    public void validate() {

        initValidMoves();

        if (!moveList1.isEmpty() && !moveList2.isEmpty()) {

            // if there are valid moves for both dice
            if (die1.compareTo(die2) < 0) {     // if the smaller die's move blocks the other die's  than that move is illegal
                moveList1.removeIf(move -> checkBlocks(move, die2));
                moveList2.removeIf(move -> checkBlocks(move, die1));
            } else {
                moveList2.removeIf(move -> checkBlocks(move, die1));
                moveList1.removeIf(move -> checkBlocks(move, die2));
            }


        }
        else if (moveList1.isEmpty() && !moveList2.isEmpty()) {  //if for die 1 there is no move
            for (Move move: moveList2
                 ) {
                if (checkUnblocks(move, die1)) {    //if there are moves with die 2 that unblocks a move for die 1 then those moves are the only legal ones
                    moveList2.removeIf(move2 -> !checkUnblocks(move2, die1));       //and remove the others
                }
            }
        }
        else if (!moveList1.isEmpty()) {   //if for die 2 there is no move
            for (Move move: moveList1
            ) {
                if (checkUnblocks(move, die2)) {    //if there are moves with die 1 that unblocks a move for die 2 then those moves are the only legal ones
                    moveList1.removeIf(move1 -> !checkUnblocks(move1, die1));       //and remove the others
                }
            }
        }
//        return 1;
    }

    private List<Move> initValidMovesForDie(Die die, Board board) {

        List<Move> moveList = new ArrayList<>();
        int dieVal = die.getValue();

        boolean canExtract = true;
        /*
            choose color
         */
        switch (color){

            case WHITE -> {

                /*
                    check kicked checkers
                 */
                if (board.getList().get(25).isEmpty()) {
//                    hasKicked = false;  //no kicked checkers
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
//                    hasKicked = true;   // it has kicked checkers
                    /*
                        check for move with dieVal
                     */
                    if ( (board.getList().get(dieVal - 1).size()<=1 )                                   //free or only one enemy checker
                            || (board.getList().get(dieVal - 1).peek().getColor() != Color.BLACK) ) {   //my checkers' color
                        moveList.add(new Move(25, dieVal -1));
                    }

                }
            }
            case BLACK -> {

                /*
                    check kicked checkers
                */
                if (board.getList().get(24).isEmpty()) {
//                    hasKicked = false;  //no kicked checkers
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
//                    hasKicked = true;   // it has kicked checkers
                    /*
                        check for move with dieVal
                     */
                    if ((board.getList().get(23 - dieVal + 1).size() <= 1)                                   //free or only one enemy checker
                            || (board.getList().get(23 - dieVal + 1).peek().getColor() != Color.WHITE)) {   //my checkers' color
                        moveList.add(new Move(24, 23 - dieVal + 1));
                    }
                }
            }

        }   // ->end switch

        return moveList;
    }

    public boolean checkBoth(Move move) {


        return true;
    }

    public boolean checkBlocks(Move moveToCheck, Die remainingDie) {

        Board auxBoard = board;

        auxBoard.moveCheckers(moveToCheck);
        return initValidMovesForDie(remainingDie, auxBoard).isEmpty();
    }

    public boolean checkUnblocks(Move moveToCheck, Die remainingDie) {

        Board auxBoard = board;

        auxBoard.moveCheckers(moveToCheck);
        return !initValidMovesForDie(remainingDie, auxBoard).isEmpty();
    }

    public boolean validateMove(Move move, int dieNum) {
        return (dieNum == 1 ? moveList1 : moveList2).contains(move);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Move> getMoveList1() {
        return moveList1;
    }

    public void setMoveList1(List<Move> moveList1) {
        this.moveList1 = moveList1;
    }

    public List<Move> getMoveList2() {
        return moveList2;
    }

    public void setMoveList2(List<Move> moveList2) {
        this.moveList2 = moveList2;
    }

    public Die getDie1() {
        return die1;
    }

    public void setDie1(Die die1) {
        this.die1 = die1;
    }

    public Die getDie2() {
        return die2;
    }

    public void setDie2(Die die2) {
        this.die2 = die2;
    }

    public Board getBoard() {
        return board;
    }

    public Color getColor() {
        return color;
    }
}
