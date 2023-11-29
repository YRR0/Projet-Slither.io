package fr.uparis.informatique.cpoo5.projet.view;

import fr.uparis.informatique.cpoo5.projet.model.Food;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegment;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import fr.uparis.informatique.cpoo5.projet.model.Game;

// La classe qui représente le panneau de jeu
public class GamePane extends StackPane {
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
    private Game game;
    private Canvas canvas;

    public GamePane(Game game) {
        this.game = game;
        this.canvas = new Canvas(WIDTH, HEIGHT);
        getChildren().add(canvas);
    }

    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        //On affiche tous les éléments de la liste
        for (Food food : game.getFoodList()) {
            gc.setFill(Color.RED);
            gc.fillOval(food.getX(), food.getY(), food.getSize(), food.getSize());
        }

        // Dessiner le serpent
        for (SnakeSegment segment : game.getSnake()) {
            gc.setFill(Color.GREEN);
            gc.fillOval(segment.getX(), segment.getY(), SnakeSegment.SIZE, SnakeSegment.SIZE);
        }
    }


    public void handleMouseMove(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        SnakeSegment head = game.getSnake().get(0);
        double angle = Math.atan2(mouseY - head.getY(), mouseX - head.getX());
        game.setDirection(Math.cos(angle), Math.sin(angle));
    }
}
