package fr.uparis.informatique.cpoo5.projet.controller;

// Package fr.uparis.informatique.cpoo5.projet.model
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegment;
import fr.uparis.informatique.cpoo5.projet.view.GamePane;

import java.awt.event.MouseEvent;

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

    // Méthode pour bien controler le jeu
    public void handleMouseMove(javafx.scene.input.MouseEvent mouseEvent) {
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();

        SnakeSegment head = game.getSnake().get(0);
        double angle = Math.atan2(mouseY - head.getY(), mouseX - head.getX());
        game.setDirection(Math.cos(angle), Math.sin(angle));
    }

    public Game getGame(){
        return this.game;
    }
}