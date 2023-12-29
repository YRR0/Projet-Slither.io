package fr.uparis.informatique.cpoo5.projet.reseau;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;

public class Server implements Runnable {
    private int playerIdCounter = 1;
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private Game game;

    // Création d'un ThreadPool avec une taille fixe (par exemple, 10 threads)
    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public Server(Game game) {
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
                    broadcastGame();
                }

                System.out.println("Taille de notre Liste : " + game.getAllSnakes().size());
                playerIdCounter++;

                //new Thread(clientHandler).start();
                threadPool.execute(clientHandler);
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

    public Game getServerGame(){
        return this.game;
    }

    public  int getServeurId(){
        return this.playerIdCounter;
    }

    public void broadcastGame() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(game);

            byte[] gameBytes = baos.toByteArray();

            for (ClientHandler client : clients) {
                client.sendGame(gameBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
