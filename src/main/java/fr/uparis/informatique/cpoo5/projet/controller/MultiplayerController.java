package fr.uparis.informatique.cpoo5.projet.controller;

import fr.uparis.informatique.cpoo5.projet.model.MultiplayerGame;
import fr.uparis.informatique.cpoo5.projet.view.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public final class MultiplayerController {
    SplitScreenMultiplayerGamePane gamePane;
    private MultiplayerGame game;

    public MultiplayerController(MultiplayerGame game, SplitScreenMultiplayerGamePane gamePane) {
        this.game = game;
        this.gamePane = gamePane;
    }

    public void update() {
        // Mettre à jour les jeux et les contrôleurs de serpent pour chaque joueur
        game.update();
        gamePane.render();
    }

    public void handleKeyPress(KeyEvent keyEvent) {
        // Gérer les événements de pression de touche pour chaque joueur
        KeyCode keyCode = keyEvent.getCode();

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
            case Z:
                game.setDirectionPlayer2(0, -1);
                break;
            case S:
                game.setDirectionPlayer2(0, 1);
                break;
            case Q:
                game.setDirectionPlayer2(-1, 0);
                break;
            case D:
                game.setDirectionPlayer2(1, 0);
                break;
            case P:
                gamePane.drawPause();
                game.togglePause();
                break;
            case SPACE:
                // Mettre en pause ou reprendre le jeu lorsque la touche "P" est pressée
                gamePane.drawPause();
                game.togglePause();
                break;
            default:
                break;
        }
    }
}
