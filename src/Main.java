import java.util.Random;

public class Main {
    public static void main(String[] args) throws Exception {

        Player player1 = new Player("adrian", false, Color.WHITE);
        Player player2 = new Player("petru", false, Color.WHITE);
        Game game = new Game(player1, player2);
        game.playGame();
    }
}
