// ServerLauncher.java
package fr.uparis.informatique.cpoo5.projet.reseau;
import fr.uparis.informatique.cpoo5.projet.model.Game;

public class ServerLauncher {
    public static void main(String[] args) {
        Game game = SharedGameService.getGameInstance();
        Server server = new Server(game);
        new Thread(server).start();
    }
}
