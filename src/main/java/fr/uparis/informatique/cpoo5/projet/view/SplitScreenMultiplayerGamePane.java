package fr.uparis.informatique.cpoo5.projet.view;

import fr.uparis.informatique.cpoo5.projet.model.*;
import fr.uparis.informatique.cpoo5.projet.model.element.Food;
import fr.uparis.informatique.cpoo5.projet.model.segment.SnakeSegment;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.scene.text.TextAlignment;
import javafx.scene.shape.Line;

import java.util.List;

public final class SplitScreenMultiplayerGamePane extends HBox {
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
        if (!(game.getgameOver() || game.getgameOver2())) {
            renderPlayerCanvas(canvasPlayer1, game.getSnake(), game.getSnakePlayer2());
            renderPlayerCanvas(canvasPlayer2, game.getSnakePlayer2(), game.getSnake());
        } else {
            if (game.getgameOver()) {
                GraphicsContext gc1 = canvasPlayer1.getGraphicsContext2D();
                drawGameOverScreen(gc1);
            } else {
                GraphicsContext gc2 = canvasPlayer2.getGraphicsContext2D();
                drawGameOverScreen(gc2);
            }
        }
    }

    private void renderPlayerCanvas(Canvas canvas, SnakeBody snake, SnakeBody snake2) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        if (!snake.getSnakeBody().isEmpty()) {
            // Calculer la différence pour centrer la vue
            double offsetX = WIDTH / 2 - snake.getSnakeBody().get(0).getX();
            double offsetY = HEIGHT / 2 - snake.getSnakeBody().get(0).getY();

            // Afficher les éléments décalés par la différence calculée
            renderFood(gc, offsetX, offsetY, game.getFoodList());
            renderSnake(gc, offsetX, offsetY, snake.getSnakeBody());

            renderSnake(gc, offsetX, offsetY, snake2.getSnakeBody());

            drawScore(gc, snake);
        }
    }

    private void renderFood(GraphicsContext gc, double offsetX, double offsetY, List<Food> foodList) {
        for (Food food : foodList) {
            if (!food.food_or_deadFood()) {
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

    public void drawPause() {
        GraphicsContext gc = canvasPlayer1.getGraphicsContext2D();
        GraphicsContext gc2 = canvasPlayer2.getGraphicsContext2D();

        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc2.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc2.fillRect(0, 0, WIDTH, HEIGHT);

        // Afficher le texte de pause
        gc.setFill(Color.WHITE);
        gc2.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 40));
        gc2.setFont(new Font("Arial", 40));
        gc.setTextAlign(TextAlignment.CENTER);
        gc2.setTextAlign(TextAlignment.CENTER);

        gc.fillText("Pause", WIDTH / 2, HEIGHT / 2);
        gc2.fillText("Pause", WIDTH / 2, HEIGHT / 2);

    }

    private void drawScore(GraphicsContext gc, SnakeBody snake) {
        double scoreX = WIDTH / 2;
        double scoreY = 50;

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 50));

        int snakeSize = snake.getSnakeBody().size();
        gc.fillText("" + snakeSize, scoreX, scoreY);
    }

    private void drawGameOverScreen(GraphicsContext gc) {
        // Afficher un message Game Over au centre de l'écran
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 40));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Game Over", WIDTH / 2, HEIGHT / 2 - 40);

        // Afficher le score
        int snakeSize = game.getSnake().getSnakeBody().size();
        gc.setFont(Font.font("Arial", 20));
        gc.fillText("Taille du serpent : " + snakeSize, WIDTH / 2, HEIGHT / 2);
    }
}
