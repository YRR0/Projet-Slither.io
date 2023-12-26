package fr.uparis.informatique.cpoo5.projet.reseau;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class ClientHandler implements Runnable {
    private Serveur serveur;
    private Socket clientSocket;
    private Game game;
    private int playerId;
    private BufferedReader input;
    private PrintWriter output;

    public ClientHandler(Serveur s, Socket clientSocket, Game game) {
        this.clientSocket = clientSocket;
        this.game = game;
        this.serveur = s;
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
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            while (true) {
                message = input.readLine();
                if(  message != null) {
                    System.out.println("Received message from client: " + message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void assignUniqueSnake() {
            game.assignSnakeToPlayer(serveur.getServeurId());
            // Envoyer l'ID du joueur au client
            output.println("PLAYER_ID " + serveur.getServeurId());
    }

    public void sendMessage(String message) {
        output.println(message);
    }
}
