package fr.uparis.informatique.cpoo5.projet.controller;

import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegment;
import fr.uparis.informatique.cpoo5.projet.view.GamePane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.geom.Point2D;


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
        SnakeSegment head = game.getSnake().get(0);
        // Utiliser directement les coordonnées x et y de la souris
        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();

        // Définir la taille de la zone morte
        double deadZone = 20.0;

        // Calculer la distance entre la tête du serpent et la position de la souris
        double distanceToMouse = Math.hypot(mouseX - head.getX(), mouseY - head.getY());

        // Activer le suivi de la souris uniquement si la souris se déplace au-delà de la zone morte
        if (distanceToMouse > deadZone) {
            // Ajouter une constante pour définir la vitesse de rotation maximale (en radians par étape)
            double maxRotationSpeed = 0.05;

            // Calculer l'angle entre la tête du serpent et la position de la souris
            double targetAngle = Math.atan2(mouseY - head.getY(), mouseX - head.getX());

            // Obtenir l'angle actuel du serpent
            double currentAngle = Math.atan2(game.getDirectionY(), game.getDirectionX());

            // Lisser le mouvement en ajustant progressivement l'angle vers la position de la souris
            double deltaAngle = targetAngle - currentAngle;
            if (deltaAngle > Math.PI) {
                deltaAngle -= 2 * Math.PI;
            } else if (deltaAngle < -Math.PI) {
                deltaAngle += 2 * Math.PI;
            }

            double smoothDeltaAngle = Math.max(-maxRotationSpeed, Math.min(maxRotationSpeed, deltaAngle));
            double newAngle = currentAngle + smoothDeltaAngle;

            // Mettre à jour la direction du serpent avec le nouvel angle
            game.setDirection(Math.cos(newAngle), Math.sin(newAngle));
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