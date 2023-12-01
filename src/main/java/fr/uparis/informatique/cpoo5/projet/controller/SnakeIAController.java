package fr.uparis.informatique.cpoo5.projet.controller;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.view.GamePane;

public class SnakeIAController extends SnakeController {

    public SnakeIAController(Game game, GamePane gamePane) {
        super(game, gamePane);
    }

    public void move(){
        getGame().moveIA();
    }
    
}
