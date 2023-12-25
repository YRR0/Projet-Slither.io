package fr.uparis.informatique.cpoo5.projet.reseau;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private Game game;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Socket clientSocket, Game game) {
        this.clientSocket = clientSocket;
        this.game = game;

        try {
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                processCommand(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processCommand(String command) {
        // Traiter la commande du client
        switch (command) {
            case NetworkMessage.MOVE_UP:
                game.setDirection(0, -1);
                break;
            case NetworkMessage.MOVE_DOWN:
                game.setDirection(0, 1);
                break;
            case NetworkMessage.MOVE_LEFT:
                game.setDirection(-1, 0);
                break;
            case NetworkMessage.MOVE_RIGHT:
                game.setDirection(1, 0);
                break;
            case NetworkMessage.READY:

                break;
            case NetworkMessage.START_GAME:

                break;
            // Ajoutez d'autres commandes si nécessaire
        }
    }

    private void handlePlayerReady() {

    }

    private void handleStartGame() {

        if (allPlayersReady()) {

        } else {

        }
    }

    private boolean allPlayersReady() {
        return true;
    }
}
