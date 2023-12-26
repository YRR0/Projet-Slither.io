package fr.uparis.informatique.cpoo5.projet.reseau;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur implements Runnable {
    private int playerIdCounter = 1;
    private final int PORT = 12345;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private Game game;

    public Serveur(Game game) {
        this.game = game;
        try {
            serverSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.println("Serveur en attente de connexions...");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nouvelle connexion: " + clientSocket);

                // Créer un gestionnaire de client pour gérer la communication avec ce client
                ClientHandler clientHandler = new ClientHandler(this,clientSocket, game);
                clients.add(clientHandler);
                System.out.println("On a assigné un serpent au client  ");

                synchronized (game) {
                    clientHandler.assignUniqueSnake();
                }
                playerIdCounter++;

                //new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void broadcastMessage(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public  int getServeurId(){
        return this.playerIdCounter;
    }
}
