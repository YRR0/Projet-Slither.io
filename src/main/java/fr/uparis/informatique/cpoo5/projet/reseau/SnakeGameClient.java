package fr.uparis.informatique.cpoo5.projet.reseau;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;

public class SnakeGameClient {
        private Socket socket;
        private PrintWriter output;
        private Game game;

        public SnakeGameClient(Game game) {
            this.game = game;
            try {
                this.socket = new Socket("localhost", 12345);
                this.output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Game getGame(){
            return this.game;
        }

        public void sendPlayerInfo() {
            double directionX = game.getSnake().get(0).getX();
            double directionY = game.getSnake().get(0).getY();
            String message = "DIRECTION " + directionX + " " + directionY;
            output.println(message);
            System.out.println("Sent message to server: " + message);

        }

        public void receiveGame(byte[] gameBytes) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(gameBytes);
                ObjectInputStream ois = new ObjectInputStream(bais);

                Game receivedGame = (Game) ois.readObject();

                // Update your local game state with the receivedGame
                game.updateGameState(receivedGame);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
}
