package fr.uparis.informatique.cpoo5.projet.controller;

import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import fr.uparis.informatique.cpoo5.projet.model.SnakeModel;
import fr.uparis.informatique.cpoo5.projet.view.SnakeView;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class SnakeController {

    private SnakeModel model;
    private SnakeView view;

    public SnakeController(SnakeModel model, SnakeView view) {
        this.model = model;
        this.view = view;

        view.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                handleKeyPress(event.getCode());
            }
        });


        // Ajouter le gestionnaire d'événements pour les clics de souris
        view.getScene().setOnMouseClicked(this::handleMouseClick);
    }

    private void handleKeyPress(KeyCode code) {
        if (code == KeyCode.RIGHT || code == KeyCode.D) {
            if (model.getCurrentDirection() != 1) {
                model.setCurrentDirection(0);
            }
        } else if (code == KeyCode.LEFT || code == KeyCode.A) {
            if (model.getCurrentDirection() != 0) {
                model.setCurrentDirection(1);
            }
        } else if (code == KeyCode.UP || code == KeyCode.W) {
            if (model.getCurrentDirection() != 3) {
                model.setCurrentDirection(2);
            }
        } else if (code == KeyCode.DOWN || code == KeyCode.S) {
            if (model.getCurrentDirection() != 2) {
                model.setCurrentDirection(3);
            }
        }
    }

    private void handleMouseClick(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        double headX = model.getSnakeHead().getX() * model.getSquareSize();
        double headY = model.getSnakeHead().getY() * model.getSquareSize();

        double deltaX = mouseX - headX;
        double deltaY = mouseY - headY;

        // Comparer les différences relatives pour déterminer la direction
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            // Mouvement horizontal plus important, donc déplacer vers la gauche ou la droite
            model.setCurrentDirection(deltaX > 0 ? 0 : 1);
        } else {
            // Mouvement vertical plus important, donc déplacer vers le haut ou le bas
            model.setCurrentDirection(deltaY > 0 ? 3 : 2);
        }
    }

    public void run() {
        /*if (model.isGameOver()) {
            view.displayGameOver();
            return;
        }*/

        for (int i = model.getSnakeBody().size() - 1; i >= 1; i--) {
            model.getSnakeBody().get(i).x = model.getSnakeBody().get(i - 1).x;
            model.getSnakeBody().get(i).y = model.getSnakeBody().get(i - 1).y;
        }

        switch (model.getCurrentDirection()) {
            case 0:
                model.moveRight();
                break;
            case 1:
                model.moveLeft();
                break;
            case 2:
                model.moveUp();
                break;
            case 3:
                model.moveDown();
                break;
        }

        //model.gameOver();
        model.eatFood();

        view.update();
    }
}