package fr.uparis.informatique.cpoo5.projet.controller;

// Package fr.uparis.informatique.cpoo5.projet.model
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.view.GamePane;

public class SnakeController {
    private Game game;
    private GamePane gamePane;

    public SnakeController(Game game, GamePane gamePane) {
        this.game = game;
        this.gamePane = gamePane;
    }

    public void update() {
        game.update();
        gamePane.render();
    }

    /*public void handleMouseMove(double mouseX, double mouseY) {
        game.handleMouseMove(mouseX, mouseY);
    }*/

    public Game getGame(){
        return this.game;
    }
}