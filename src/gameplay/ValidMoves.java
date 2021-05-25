package gameplay;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ValidMoves {

    private Board board;
    private Color color;
    private Die die1;
    private Die die2;
    private int dicePlayed = 0;
    private List<Move> moveList1 = new ArrayList<>();
    private List<Move> moveList2 = new ArrayList<>();
    private List<Move> moveList = new ArrayList<>();

    public List<Move> getMoveList() {
        validate();
        moveList.clear();
        if (moveList1 != null) {
            moveList.addAll(moveList1);
        }
        if (moveList2 != null) {
            moveList.addAll(moveList2);
        }

        return moveList;
    }

    public ValidMoves(Board board, Player player, Dice dice) {
        setBoard(board);
        setColor(player.getColor());
        setDie1(dice.getDie1());
        setDie2(dice.getDie2());
        validate();
    }

    public void validate() {

        initValidMoves();

        if (die1 != null && die2 != null) { // if no moves were made, check for legal moves

            if (!moveList1.isEmpty() && !moveList2.isEmpty()) {

                // if there are valid moves for both dice
                if (die1.compareTo(die2) < 0) {     // if the smaller die's move blocks the bigger die, that move is illegal -> removed
                    moveList1.removeIf(move -> checkBlocks(move, die2));
                    moveList2.removeIf(move -> checkBlocks(move, die1));
                } else {
                    moveList2.removeIf(move -> checkBlocks(move, die1));
                    moveList1.removeIf(move -> checkBlocks(move, die2));
                }


            } else if (moveList1.isEmpty() && !moveList2.isEmpty()) {  //if there is no move for die 1
                for (Move move : moveList2
                ) {
                    if (checkUnblocks(move, die1)) {    //if there are moves with die 2 that unblocks a move for die 1 then those moves are the only legal ones
                        moveList2.removeIf(move2 -> !checkUnblocks(move2, die1));       //and remove the others
                    }
                }
            } else if (!moveList1.isEmpty()) {   //if there is no move for die 2
                for (Move move : moveList1
                ) {
                    if (checkUnblocks(move, die2)) {    //if there are moves with die 1 that unblocks a move for die 2 then those moves are the only legal ones
                        moveList1.removeIf(move1 -> !checkUnblocks(move1, die1));       //and remove the others
                    }
                }
            }
        }
    }

    private void initValidMoves() {

        if (die1 != null) {
            setMoveList1(initValidMovesForDie(die1));
        } else {
            setMoveList1(null);
        }

        if (die2 != null) {
            setMoveList2(initValidMovesForDie(die2));
        } else {
            setMoveList2(null);
        }
    }

    private List<Move> initValidMovesForDie(Die die) {

        List<Move> moveList = new ArrayList<>();
        int dieVal = die.getValue();
        List<Stack<Checker>> places = this.board.getPlaces();
        boolean canExtract = true;
        boolean firstToRemove = true;
        boolean strictExtract = false;

        /*
            choose color
         */
        switch (color){
            case WHITE -> {
                /*
                    check kicked checkers
                 */
                if (places.get(25).isEmpty()) {

                    for (int fromPosition = 0; fromPosition <= 23-(canExtract ? 0 : dieVal); fromPosition++) {  //iterate trough board
                        if (!places.get(fromPosition).isEmpty()) {
                            if (places.get(fromPosition).peek().getColor().equals(Color.WHITE) ) {  // If color checke found
                                if (fromPosition < 18)
                                    canExtract = false; //not all checkers are in home board
                                if (fromPosition + dieVal <= 23) {
                                    strictExtract = true;   // no forced extraction can be done. first move around
                                    if (places.get(fromPosition + dieVal).size() <= 1       //  only one checker on destination place => mine or remove enemy's => valid move
                                            || places.get(fromPosition + dieVal).peek().getColor() != Color.BLACK)      //checkers of mine => valid move
                                        moveList.add(new Move(fromPosition,fromPosition + dieVal , die));    //can move to that position <=> add valid move
                                } else if (canExtract) {
                                    if (fromPosition + dieVal == 24) {      //have exact extraction possibility
                                        moveList.add(new Move(fromPosition,27,die));
                                        firstToRemove = false;      //can't force extraction anymore. another solution found

                                    } else if (fromPosition + dieVal > 24 && firstToRemove && !strictExtract) {     //extraction can be forced
                                        moveList.add(new Move(fromPosition,27,die));
                                        firstToRemove = false;      //can't force extraction anymore. another solution found
                                    }
                                }
                            }
                        }
                    }
                } else {//          check for move with dieVal for kicked whites
                    if ( (places.get(dieVal - 1).size()<=1 )       //  only one checker on destination place => mine or remove enemy's => valid move
                            || (places.get(dieVal - 1).peek().getColor() != Color.BLACK) ) {      //checkers of mine => valid move
                        moveList.add(new Move(25, dieVal -1, die));
                    }

                }
            }
            case BLACK -> {
                /*
                    check kicked checkers
                */
                if (places.get(24).isEmpty()) {
                    for (int fromPosition = 23; fromPosition >= (canExtract ? 0 : dieVal); fromPosition--) {
                        if (!places.get(fromPosition).isEmpty()) {
                            if (places.get(fromPosition).peek().getColor().equals(Color.BLACK)) {
                                if (fromPosition > 5)
                                    canExtract = false;
                                if (fromPosition - dieVal >= 0) {
                                    strictExtract = true;
                                    if (places.get(fromPosition - dieVal).size() <= 1 || places.get(fromPosition - dieVal).peek().getColor() != Color.WHITE)
                                        moveList.add(new Move(fromPosition,fromPosition - dieVal , die));
                                } else if (canExtract) {
                                    if (fromPosition - dieVal == -1) {
                                        moveList.add(new Move(fromPosition,26,die));
                                        firstToRemove = false;
                                    } else if (fromPosition - dieVal < -1 && firstToRemove && !strictExtract) {
                                        moveList.add(new Move(fromPosition,26,die));
                                        firstToRemove = false;
                                    }
                                }
                            }
                        }
                    }
                } else {

                    if ((places.get(23 - dieVal + 1).size() <= 1)
                            || (places.get(23 - dieVal + 1).peek().getColor() != Color.WHITE)) {
                        moveList.add(new Move(24, 23 - dieVal + 1, die));
                    }
                }
            }
        }   // ->end switch
        return moveList;
    }

    /*
    function to check if a move blocks the other die's moves
     */
    public boolean checkBlocks(Move moveToCheck, Die remainingDie) {
        board.testMoveCheckers(moveToCheck);
        if (board.checkWinner() == null) {
            if (initValidMovesForDie(remainingDie).isEmpty()) {
                board.testMoveCheckers(moveToCheck.getRevertMove());
                return true;
            }
        }
        board.testMoveCheckers(moveToCheck.getRevertMove());
        return false;
    }
    /*
    function to check if a move unblocks at least one move for the other die
     */
    public boolean checkUnblocks(Move moveToCheck, Die remainingDie) {
        board.testMoveCheckers(moveToCheck);
        if (board.checkWinner() == null) {
            if (!initValidMovesForDie(remainingDie).isEmpty()) {
                board.testMoveCheckers(moveToCheck.getRevertMove());
                return true;
            }
        }
        board.testMoveCheckers(moveToCheck.getRevertMove());
        return false;
    }

    /*
        true <=> move is valid
     */
    public boolean validateMove(Move move) {
        int dieNum = (move.getDie().compareTo(die1) == 0 ? 1 :2);
        return (dieNum == 1 ? moveList1 : moveList2).contains(move);
    }

    /*
        update validator for the move made
     */
    public void updateMoveList(Move moveMade) {
        this.board.moveCheckers(moveMade);
        Die diePlayed = moveMade.getDie();
        if (diePlayed.compareTo(die1) == 0) {
            setMoveList1(null);
            setDie1(null);
        } else {
            setMoveList2(null);
            setDie2(null);
        }
        this.dicePlayed ++;
    }

    /*
        played dice count
     */
    public int getDicePlayed() {
        return dicePlayed;
    }

    public void setDice(Dice dice) {
        this.setDie1(dice.getDie1());
        this.setDie2(dice.getDie2());
    }

    public void setBoard(Board board) {
        this.board.setPlaces(board.getPlaces());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setMoveList1(List<Move> moveList1) {
        this.moveList1 = moveList1;
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

    public int hasUncovered() {
        int uncovered = 0;
        for (Stack<Checker> place : this.board.getPlaces()
             ) {
            if (place.size() == 1 && place.peek().getColor() == this.color)
                uncovered++;
        }
        return uncovered;
    }

    public List<Move> safeCoverMoves(List<Move> moveList) {
        int currentUncovered = this.hasUncovered();
        List<Move> coverMoves = new ArrayList<Move>();
        for (Move move: moveList
             ) {
            this.board.testMoveCheckers(move);
            if (this.hasUncovered() < currentUncovered) {
                coverMoves.add(move);
            }
            this.board.testMoveCheckers(move.getRevertMove());
        }
        return coverMoves;
    }

    public List<Move> kickMoves (List<Move> moveList) {
        List<Move> kickMoves = new ArrayList<Move>();
        for (Move move : moveList) {
            if (board.getPlaces().get(move.getToPosition()).size() == 1     //if only one checker
                    && board.getPlaces().get(move.getToPosition()).peek().getColor() == (this.color == Color.BLACK ? Color.WHITE : Color.BLACK))    //and enemy color
                kickMoves.add(move);
        }
        return kickMoves;
    }

    public boolean checkWall(Move move1, Move move2) {

        if (move1.getToPosition() == move2.getToPosition())
            if (move1.getFromPosition() == move2.getFromPosition())
                return true;
            else if (this.board.getPlaces().get(move1.getFromPosition()).size() >= 3
                    && board.getPlaces().get(move2.getFromPosition()).size() >= 3)
                return true;
            else if (this.board.getPlaces().get(move1.getFromPosition()).size() == 1
                    && board.getPlaces().get(move2.getFromPosition()).size() >= 3)
                return true;
            else if (this.board.getPlaces().get(move1.getFromPosition()).size() >= 3
                    && board.getPlaces().get(move2.getFromPosition()).size() == 1)
                return true;

            return false;
    }

}
