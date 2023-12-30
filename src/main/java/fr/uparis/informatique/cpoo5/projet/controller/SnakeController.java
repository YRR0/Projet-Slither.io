package fr.uparis.informatique.cpoo5.projet.controller;

import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegment;
import fr.uparis.informatique.cpoo5.projet.view.GamePane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


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

        // Calculer la direction directe vers la position de la souris
        double directionX = mouseX - head.getX();
        double directionY = mouseY - head.getY();

        // Normaliser la direction pour obtenir une unité de vecteur
        double magnitude = Math.sqrt(directionX * directionX + directionY * directionY);
        if (magnitude > 0) {
            directionX /= magnitude;
            directionY /= magnitude;
        }

        // Mettre à jour la direction avec la nouvelle orientation
        game.setDirection(directionX, directionY);
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
                gamePane.drawPause();
                game.togglePause(); break;
            case R:
                if(game.getgameOver()){
                    game.reset();
                }
                break;
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