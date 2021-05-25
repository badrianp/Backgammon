package gameplay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

public class Game {

    private final Board board;
    private Dice dice = new Dice();
    private Player player1, player2;
    private int round = 1;
    private final BufferedReader command = new BufferedReader(
            new InputStreamReader(System.in));

    public Game(Player player1, Player player2, Board board) {
        this.board = board;
        setPlayer1(player1);
        setPlayer2(player2);
    }

    public void playGame() throws Exception {

        Die player1StartDie = new Die();
        Die player2StartDie = new Die();
        System.out.println(player1.getName() + "'s start die = " + player1StartDie + "\n"
                + player2.getName() + "'s start die = " + player2StartDie);

        while (player1StartDie.compareTo(player2StartDie) == 0) {
            System.out.println("It's a tie. Roll again!");
            player1StartDie.rollDie();
            player2StartDie.rollDie();
            System.out.println(player1.getName() + "'s start die = " + player1StartDie + "\n"
                    + player2.getName() + "'s start die = " + player2StartDie);
        }

        dice.setDie1(player1StartDie);
        dice.setDie2(player2StartDie);

        switch (player1StartDie.compareTo(player2StartDie)) {
            case 1 -> {
                System.out.println("\n" + player1.getName() + " opens the game with color = " + player1.getColor() + " and dice: " + dice + "\n");
                if (player1.getColor().equals(player2.getColor()))
                    player2.setColor(player1.getColor() == Color.BLACK ? Color.WHITE : Color.BLACK );
                player1.setTurn(true);
            }
            case -1 -> {
                System.out.println("\n" + player2.getName() + " opens the game with color = " + player2.getColor() + " and dice: " + dice + "\n");
                if (player1.getColor().equals(player2.getColor()))
                    player1.setColor(player2.getColor() == Color.BLACK ? Color.WHITE : Color.BLACK );
                player2.setTurn(true);
            }
        }

        while (board.checkWinner() == null) {

            if (player1.isTurn()) { // player 1 --> playTurn()
                playTurn(player1, this.dice, round);
            } else { // player 2 --> playTurn()
                playTurn(player2, this.dice, round);
            }
            nextTurn();
            round++;
        }
        if (player1.getColor() == board.checkWinner()) {
            player1.setWinner(true);
            System.err.println( player1 + " Won! ");

        } else {
            player2.setWinner(true);
            System.err.println( player2 + " Won! ");

        }
    }

    private void playTurn(Player player, Dice dice, int round) throws Exception {
        if (!player.isComputer()) {

            if (round > 1) {              //    if it's 1st round then dice is starting dice. else roll dice

                System.out.println("Roll your dice, " + player.getName() + " (" + player.getColor() + ") !");
                String request = command.readLine();

                while (!request.trim().toUpperCase(Locale.ROOT).equals("ROLL")) {
                    System.out.println("Roll your dice, " + player.getName() + "!");
                    request = command.readLine();
                }
                dice.rollDice();
            }

            ValidMoves validMoves = new ValidMoves(this.board, player, dice);

            if (dice.isDouble()) {
                System.out.println("Hooray! You got a double of " + dice.getDie1() + " <-> " + dice.getDie2() + ". You have 4 moves for that die ... ");

                int hands = 4;
                validMoves.getBoard().printBoard();

                while (hands > 0) {
                    System.out.println("Moves left = " + hands);
                    Move moveMade = playDice(player,dice, validMoves);

                    if (moveMade != null) {
                        validMoves.updateMoveList(moveMade);
                        if (validMoves.getDicePlayed() == 2) {
                            validMoves.setDice(dice);
                        }
                        validMoves.getBoard().printBoard();
                        hands--;

                    } else {
                        if (board.checkWinner() == null)
                            System.out.println("No more valid moves ! Maybe next time ... ");
                        hands = 0;
                    }
                }

            } else {
                int hands = 2;
                validMoves.getBoard().printBoard();
                while (hands > 0) {
                    System.out.println("Moves left = " + hands);
                    Move moveMade = playDice(player,dice, validMoves);
                    if ( moveMade != null) {
                        validMoves.updateMoveList(moveMade);
                        validMoves.getBoard().printBoard();
                        hands--;
                    } else {
                        if (board.checkWinner() == null)
                            System.out.println("No more valid moves ! Maybe next time ... ");
                        hands = 0;
                    }
                }
            }
        } else {

        }
    }

    public Move playDice(Player player, Dice dice, ValidMoves validMoves) throws IOException {

        List<Move> validMovesList = validMoves.getMoveList();
        System.out.println( player.getName() + "( " + player.getColor() + " )'s " +
                "dice = { " + (validMoves.getDie1() != null ? validMoves.getDie1() + ", " : "")
                            + (validMoves.getDie2() != null ? validMoves.getDie2() + " }" : " }"));

        if (validMovesList.isEmpty()) {
            return null;
        } else {
            System.out.println(
                    "Choose your move for dice = " + dice
                    + " as 'x , y' ( move checker from position 'x' to position 'y' ). "
                    + "\nValid moves are: " + validMovesList);
            String request;

            while (true) {

                request = command.readLine();
                String[] movePositions = request.split(",");
                if (movePositions.length != 2 || !isInteger(movePositions[0])  || ! isInteger(movePositions[1])) {      // check for input format exception
                    System.out.println(
                            "Invalid move format '" + request + "', "  + player.getName() + " ( " + player.getColor() + " ).\n"
                            + "Choose your move for dice = " + dice + " as 'x , y' ( move checker from position 'x' to position 'y' )."
                            + "\nValid moves are: " + validMovesList + "\n");
                } else {                                                                //valid input format ->> validate move

                    int fromPosition = Integer.parseInt(movePositions[0].trim());
                    int toPosition = Integer.parseInt(movePositions[1].trim());

                    Die die = getPlayedDie(dice, player, fromPosition, toPosition);

                    Move move = new Move(fromPosition, toPosition, die);
                    System.out.println("move received: " + fromPosition + " --> " + toPosition + " with die = " + die );

                    if (!validMoves.validateMove(move)) {
                        System.out.println("Your move is invalid, " + player.getName() + " ( " + player.getColor() + " ) !\n" +
                                "dice = " + (validMoves.getDie1() != null ? "{ [" + validMoves.getDie1() + "]"  : "{ ") + (validMoves.getDie2() != null ? "[" + validMoves.getDie2() + "] }" : " }") +
                                "\nValid moves are:\n" + validMovesList + "\n");
                    } else {
                        System.out.println( player.getName() + " ( " + player.getColor() + " ) made move -> " + move);
                        return move;
                    }
                }
            }
        }
    }

    private void nextTurn() {
        if (getPlayer1().isTurn()) {
            getPlayer1().setTurn(false);
            getPlayer2().setTurn(true);
        } else {
            getPlayer1().setTurn(true);
            getPlayer2().setTurn(false);
        }
    }

    public boolean isInteger(String input) {
        try {
            Integer.parseInt(input);
            return true;
        }
        catch(Exception e) {
            return false;
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Die getPlayedDie(Dice dice, Player player, int fromPosition, int toPosition) {

        int dieVal = (player.getColor().equals(Color.WHITE) ? toPosition - fromPosition : fromPosition - toPosition);
        int minDie = (dice.getDie1().compareTo(dice.getDie2()) > 0 ? dice.getDie2().getValue() : dice.getDie1().getValue());
        int maxDie = (dice.getDie1().compareTo(dice.getDie2()) < 0 ? dice.getDie2().getValue() : dice.getDie1().getValue());

        if (dice.getDie1() == null) {
            return dice.getDie2();
        }else if (dice.getDie2() == null) {
            return dice.getDie1();
        }else {

            if (fromPosition == 25) {           //kicked white got back
                dieVal = toPosition + 1;
            } else if (fromPosition == 24) {    //kicked black got back
                dieVal = fromPosition - toPosition;
            } else if (toPosition == 27) {      //white in the house

                if (24 - fromPosition == minDie) {
                    dieVal = minDie;
                } else if (24 - fromPosition == maxDie) {
                    dieVal = maxDie;
                } else if (24 - fromPosition > minDie) {
                    dieVal = maxDie;
                } else {
                    dieVal = maxDie;
                }
            } else if (toPosition == 26) {      //black in the house

                if (fromPosition + 1 == minDie) {
                    dieVal = minDie;
                } else if (fromPosition + 1 == maxDie) {
                    dieVal = maxDie;
                } else if (fromPosition + 1 > minDie) {
                    dieVal = maxDie;
                } else {
                    dieVal = maxDie;
                }
            }
        }
        return new Die(dieVal);
    }
}