package fr.uparis.informatique.cpoo5.projet.view;
import fr.uparis.informatique.cpoo5.projet.model.Food;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegment;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegmentIA;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import java.util.List;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import javafx.scene.image.Image;

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
        // Définir la couleur de fond comme noir
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Trouver la tête du serpent
        SnakeSegment head = game.getSnake().get(0);
        // Calculer la différence pour centrer la vue
        double offsetX = WIDTH / 2 - head.getX();
        double offsetY = HEIGHT / 2 - head.getY();

        // Afficher tous les éléments décalés par la différence calculée
        for (Food food : game.getFoodList()) {
            if(!food.food_or_deadFood()) {
                double adjustedX = (food.getX() + offsetX + WIDTH) % WIDTH;
                double adjustedY = (food.getY() + offsetY + HEIGHT) % HEIGHT;

                gc.setFill(food.getColor());
                gc.fillOval(adjustedX, adjustedY, food.getSize(), food.getSize());
                }
            }

        // Dessiner le serpent
        for (SnakeSegment segment : game.getSnake()) {
            double adjustedX = (segment.getX() + offsetX + WIDTH) % WIDTH;
            double adjustedY = (segment.getY() + offsetY + HEIGHT) % HEIGHT;

            // Facteur de croissance en fonction de la taille du serpent
            double growthFactor = 1.0 + game.getSnake().size() * 0.000005;

            gc.setFill(segment.getColor());
            gc.fillOval(adjustedX, adjustedY, SnakeSegment.SIZE * growthFactor, SnakeSegment.SIZE * growthFactor);
        }

        // Dessiner les IA
        for (List<SnakeSegmentIA> ia : game.getIA()) {
            for (SnakeSegmentIA segment : ia) {
                double adjustedX = (segment.getX() + offsetX + WIDTH) % WIDTH;
                double adjustedY = (segment.getY() + offsetY + HEIGHT) % HEIGHT;

                gc.setFill(Color.BLUE);
                gc.fillOval(adjustedX, adjustedY, SnakeSegment.SIZE, SnakeSegment.SIZE);
            }
        }
    }
}
