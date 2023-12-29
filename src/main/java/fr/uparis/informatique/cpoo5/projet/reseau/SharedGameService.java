package fr.uparis.informatique.cpoo5.projet.reseau;
import fr.uparis.informatique.cpoo5.projet.model.Game;
public class SharedGameService {

    private static Game gameInstance;
    public static synchronized Game getGameInstance() {
        if (gameInstance == null) {
            gameInstance = new Game();
        }
        return gameInstance;
    }
}
