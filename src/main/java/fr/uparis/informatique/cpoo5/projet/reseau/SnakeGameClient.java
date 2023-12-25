package fr.uparis.informatique.cpoo5.projet.reseau;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import fr.uparis.informatique.cpoo5.projet.model.Game;
    public class SnakeGameClient {
        private Socket socket;
        private PrintWriter output;
        private Game game;

        public SnakeGameClient(Game game) {
            this.game = game;

            try {
                // Remplacez "localhost" et 12345 par l'adresse IP et le port de votre serveur
                this.socket = new Socket("localhost", 12345);
                this.output = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendPlayerInfo() {
            double directionX = game.getSnake().get(0).getX();
            double directionY = game.getSnake().get(0).getY();

            //String message = String.format("%s %f %f", NetworkMessage.PLAYER_INFO, directionX, directionY);
            //output.println(message);
        }
}
