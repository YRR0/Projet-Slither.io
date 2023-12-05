package fr.uparis.informatique.cpoo5.projet.controller;

// Package fr.uparis.informatique.cpoo5.projet.model
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegment;
import fr.uparis.informatique.cpoo5.projet.view.GamePane;
import fr.uparis.informatique.cpoo5.projet.view.PausePane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.event.MouseEvent;

public class SnakeController {
    private static final double SPEED_INCREASE_FACTOR = 1.5; // Vous pouvez ajuster ce facteur selon vos besoins

    private Game game;
    private GamePane gamePane;

    private PausePane pause;
    public SnakeController(Game game, GamePane gamePane) {
        this.game = game;
        this.gamePane = gamePane;

    }

    public void togglePause() {
        game.setPaused(!game.isPaused());
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

        // Vérifier si la souris est toujours à l'intérieur des limites de l'écran
        if (mouseX >= 0 && mouseX < game.getWidth() && mouseY >= 0 && mouseY < game.getHeight()) {
            // Calculer la direction vers la position de la souris
            double directionX = mouseX - head.getX();
            double directionY = mouseY - head.getY();

            // Normaliser le vecteur direction
            double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);

            // Vérifier si la magnitude est suffisamment grande pour éviter la division par zéro
            if (magnitude > 1e-8) {  // Utilisez un seuil approprié
                // Ajuster la direction en fonction de la position de la souris
                game.setDirection(directionX / magnitude, directionY / magnitude);
            }
        }
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();

        // Définir la direction en fonction de la touche pressée
        switch (keyCode) {
            case UP:
                game.setDirection(0, -1);
                break;
            case DOWN:
                game.setDirection(0, 1);
                break;
            case LEFT:
                game.setDirection(-1, 0);
                break;
            case RIGHT:
                game.setDirection(1, 0);
                break;
            case  P: // Mettre en pause ou reprendre le jeu lorsque la touche "P" est pressée
                game.togglePause(); break;
            case SPACE:
                // Augmenter la vitesse lorsque la barre d'espace est pressée
                game.increaseSpeed();
                break;
        }
    }

    public void handleKeyRelease(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();

        if (keyCode == KeyCode.SPACE) {
            // Rétablir la vitesse par défaut lors du relâchement de la barre d'espace
            game.decreaseSpeed();
        }
    }
}