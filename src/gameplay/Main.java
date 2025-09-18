package gameplay;

public class Main {
    public static void main(String[] args) throws Exception {

        Player player1 = new Player("adrian", false, Color.WHITE);
        Player player2 = new Player("petru", false, Color.BLACK);
        Board board = new Board();
        Game game = new Game(player1, player2, board);
        game.playGame();
    }
}
