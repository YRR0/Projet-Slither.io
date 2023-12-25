// ServerLauncher.java
package fr.uparis.informatique.cpoo5.projet.reseau;
import fr.uparis.informatique.cpoo5.projet.model.Game;

public class ServerLauncher {
    public static final Game game = new Game() ;

    public static void main(String[] args) {
        Serveur server = new Serveur(game);
        new Thread(server).start();
    }
}
