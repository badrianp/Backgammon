import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {

        Board myBoard = new Board();
//        myBoard.moveCheckers(new Move(0,25), 1);
        myBoard.printBoard();

        Dice dice = new Dice();
//        dice.getDie1().setValue(5);
        System.out.println( "die1: " + dice.getDie1().getValue());

        ValidMoves validMoves = new ValidMoves();

        System.out.println(validMoves.getMoveList(myBoard,Color.WHITE,dice.getDie1()) +" size - " + validMoves.getMoveList(myBoard,Color.WHITE,dice.getDie1()).size());
        Move move1 = validMoves.getMoveList(myBoard,Color.WHITE,dice.getDie1()).get(new Random().nextInt(validMoves.getMoveList(myBoard,Color.WHITE,dice.getDie1()).size()));

        myBoard.moveCheckers(move1, 1);
        System.out.println("move1 white made = " + move1);
        myBoard.printBoard();

        System.out.println( "die2: " + dice.getDie2().getValue());

        System.out.println(validMoves.getMoveList(myBoard,Color.WHITE,dice.getDie2()) +" size - " + validMoves.getMoveList(myBoard,Color.WHITE,dice.getDie2()).size());
        Move move2 = validMoves.getMoveList(myBoard,Color.WHITE,dice.getDie2()).get(new Random().nextInt(validMoves.getMoveList(myBoard,Color.WHITE,dice.getDie2()).size()));

        myBoard.moveCheckers(move2, 1);
        System.out.println("move2 white made = " + move2);
        myBoard.printBoard();

//        myBoard.printBoard();
    }
}
