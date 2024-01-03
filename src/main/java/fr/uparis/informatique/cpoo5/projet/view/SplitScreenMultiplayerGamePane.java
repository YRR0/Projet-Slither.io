package fr.uparis.informatique.cpoo5.projet.view;
import fr.uparis.informatique.cpoo5.projet.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Line;

import java.util.List;

public class SplitScreenMultiplayerGamePane extends HBox {
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth() / 2;
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
    private MultiplayerGame game;
    private Canvas canvasPlayer1;
    private Canvas canvasPlayer2;

    public SplitScreenMultiplayerGamePane(MultiplayerGame game) {
        this.game = game;

        this.canvasPlayer1 = new Canvas(WIDTH, HEIGHT);
        this.canvasPlayer2 = new Canvas(WIDTH, HEIGHT);

        getChildren().addAll(canvasPlayer1, createDividerLine(), canvasPlayer2);
    }

    private Line createDividerLine() {
        Line dividerLine = new Line();
        dividerLine.setStartX(WIDTH);
        dividerLine.setStartY(0);
        dividerLine.setEndX(WIDTH);
        dividerLine.setEndY(HEIGHT);
        dividerLine.setStroke(Color.WHITE);
        dividerLine.setStrokeWidth(2); // Épaisseur de la barre de séparation

        return dividerLine;
    }

    public void render() {
        renderPlayerCanvas(canvasPlayer1, game.getSnake());
        renderPlayerCanvas(canvasPlayer2, game.getSnakePlayer2());
    }

    private void renderPlayerCanvas(Canvas canvas, SnakeBody snake) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Calculer la différence pour centrer la vue
        double offsetX = WIDTH / 2 - snake.getSnakeBody().get(0).getX();
        double offsetY = HEIGHT / 2 - snake.getSnakeBody().get(0).getY();

        // Afficher les éléments décalés par la différence calculée
        renderFood(gc, offsetX, offsetY, game.getFoodList());
        renderSnake(gc, offsetX, offsetY, snake.getSnakeBody());

        drawScore(gc, snake);
    }

    private void renderFood(GraphicsContext gc, double offsetX, double offsetY, List<Food> foodList) {
        for (Food food : foodList) {
            if(!food.food_or_deadFood()) {
                double adjustedX = (food.getX() + offsetX + WIDTH) % WIDTH;
                double adjustedY = (food.getY() + offsetY + HEIGHT) % HEIGHT;

                gc.setFill(food.getColor());
                gc.fillOval(adjustedX, adjustedY, food.getSize(), food.getSize());
            }
        }
    }

    private void renderSnake(GraphicsContext gc, double offsetX, double offsetY, List<SnakeSegment> snake) {
        int numSegments = snake.size();
        for (int i = 0; i < numSegments; i++) {
            SnakeSegment segment = snake.get(i);
            double adjustedX = (segment.getX() + offsetX + WIDTH) % WIDTH;
            double adjustedY = (segment.getY() + offsetY + HEIGHT) % HEIGHT;

            Color segmentColor = getColorForSegment(i, numSegments);

            double growthFactor = 1.0 + numSegments * 0.000005;

            if (game.getSnake().hasPower()) {
                gc.setFill(Color.WHITE);
            } else {
                gc.setFill(segmentColor);
            }
            gc.fillOval(adjustedX, adjustedY, SnakeSegment.SIZE * growthFactor, SnakeSegment.SIZE * growthFactor);
        }
    }

    private Color getColorForSegment(int segmentIndex, int numSegments) {
        float hue = (float) segmentIndex / numSegments;
        return Color.hsb(hue * 360, 1.0, 1.0);
    }

    private void drawScore(GraphicsContext gc, SnakeBody snake) {
        double scoreX = WIDTH / 2;
        double scoreY = 50;

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 50));

        int snakeSize = snake.getSnakeBody().size();
        gc.fillText("" + snakeSize, scoreX, scoreY);
    }
}
