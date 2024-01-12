package fr.uparis.informatique.cpoo5.projet.view;

import fr.uparis.informatique.cpoo5.projet.model.element.Food;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.model.SnakeBody;
import fr.uparis.informatique.cpoo5.projet.model.element.Power;
import fr.uparis.informatique.cpoo5.projet.model.segment.SnakeSegment;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.scene.text.TextAlignment;
import java.awt.*;
import java.util.List;
import javafx.scene.image.Image;

public class GamePane extends StackPane {
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();

    private final Image powerImage = new Image(getClass().getResourceAsStream("/images/b2.png"));
    private final Image weakImage = new Image(getClass().getResourceAsStream("/images/w2.png"));
    private Game game;
    private Canvas canvas;

    public GamePane(Game game) {
        this.game = game;
        this.canvas = new Canvas(WIDTH, HEIGHT);
        getChildren().add(canvas);
    }

    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        if (!game.getgameOver()) {
            gc.clearRect(0, 0, WIDTH, HEIGHT);
            // Définir la couleur de fond comme noir
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, WIDTH, HEIGHT);

            // Trouver la tête du serpent
            SnakeSegment head = game.getSnake().getSnakeBody().get(0);
            // Calculer la différence pour centrer la vue
            double offsetX = WIDTH / 2 - head.getX();
            double offsetY = HEIGHT / 2 - head.getY();

            // Afficher tous les éléments décalés par la différence calculée
            for (Food food : game.getFoodList()) {
                if (!food.food_or_deadFood()) {
                    double adjustedX = (food.getX() + offsetX + WIDTH) % WIDTH;
                    double adjustedY = (food.getY() + offsetY + HEIGHT) % HEIGHT;

                    if(food.getPower() == null) {
                        gc.setFill(food.getColor());
                        gc.fillOval(adjustedX, adjustedY, food.getSize(), food.getSize());
                    }
                    else{
                        if(food.getPower() == Power.SHIELD){
                            gc.drawImage(powerImage, adjustedX, adjustedY, 30, 30);
                        }
                        else{
                            gc.drawImage(weakImage, adjustedX, adjustedY, 30, 30);
                        }

                    }
                }
            }

            // Dessiner le serpent
            int numSegments = game.getSnake().getSnakeBody().size();
            SnakeBody body = game.getSnake();
            List<SnakeSegment> snake = game.getSnake().getSnakeBody();
            for (int i = 0; i < numSegments; i++) {
                SnakeSegment segment = snake.get(i);
                double adjustedX = (segment.getX() + offsetX + WIDTH) % WIDTH;
                double adjustedY = (segment.getY() + offsetY + HEIGHT) % HEIGHT;

                Color segmentColor = getColorForSegment(i, numSegments);

                double growthFactor = 1.0 + numSegments * 0.000005;

                if (body.hasPower()) {
                    gc.setFill(segment.getColor());
                    if(body.getPower() == Power.SHIELD) {
                        gc.fillRect(adjustedX, adjustedY, SnakeSegment.SIZE * growthFactor, SnakeSegment.SIZE * growthFactor);
                    }
                    else{
                        double[] xPoints = {adjustedX, adjustedX + SnakeSegment.SIZE * growthFactor, adjustedX - SnakeSegment.SIZE * growthFactor};
                        double[] yPoints = {adjustedY, adjustedY + SnakeSegment.SIZE * growthFactor, adjustedY + SnakeSegment.SIZE * growthFactor};
                        gc.fillPolygon(xPoints, yPoints, 3);
                    }
                } else {
                    gc.setFill(segmentColor);
                    gc.fillOval(adjustedX, adjustedY, SnakeSegment.SIZE * growthFactor, SnakeSegment.SIZE * growthFactor);
                }
            }

            // Dessiner les IA
            for (SnakeBody ia : game.getIA()) {
                int numSegmentsIA = ia.getSnakeBody().size();
                for (int i = 0; i < numSegmentsIA; i++) {
                    SnakeSegment segmentIA = ia.getSnakeBody().get(i);
                    double adjustedX = (segmentIA.getX() + offsetX + WIDTH) % WIDTH;
                    double adjustedY = (segmentIA.getY() + offsetY + HEIGHT) % HEIGHT;

                    // Utilisation d'une palette de couleurs en fonction de l'indice du segment
                    Color segmentColorIA = getColorForSegment(i, numSegmentsIA);
                    if (ia.hasPower()) {
                        gc.setFill(segmentIA.getColor());
                        if(ia.getPower() == Power.SHIELD){
                            gc.fillRect(adjustedX, adjustedY, SnakeSegment.SIZE , SnakeSegment.SIZE );
                        }
                        else{
                            double[] xPoints = {adjustedX, adjustedX + SnakeSegment.SIZE , adjustedX - SnakeSegment.SIZE };
                            double[] yPoints = {adjustedY, adjustedY + SnakeSegment.SIZE , adjustedY + SnakeSegment.SIZE };
                            gc.fillPolygon(xPoints, yPoints, 3);
                        }
                    } else {
                        gc.setFill(segmentColorIA);
                        gc.fillOval(adjustedX, adjustedY, SnakeSegment.SIZE, SnakeSegment.SIZE);
                    }
                }
            }

            drawScore(gc);
        } else {
            drawGameOverScreen(gc);
        }
    }

    private void drawScore(GraphicsContext gc) {
        double scoreX = WIDTH / 2;
        double scoreY = 50;

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", 50));

        int snakeSize = game.getSnake().getSnakeBody().size();

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

        // Ajouter un message pour recommencer la partie
        gc.setFont(Font.font("Arial", 16));
        gc.fillText("Appuyez sur R pour recommencer", WIDTH / 2, HEIGHT / 2 + 40);
    }

    private Color getColorForSegment(int segmentIndex, int numSegments) {
        float hue = (float) segmentIndex / numSegments;
        return Color.hsb(hue * 360, 1.0, 1.0);
    }

    public void drawPause() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Dessiner un fond semi-transparent
        gc.setFill(Color.rgb(0, 0, 0, 0.5));
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Afficher le texte de pause
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Arial", 40));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText("Pause", WIDTH / 2, HEIGHT / 2);
    }

}
