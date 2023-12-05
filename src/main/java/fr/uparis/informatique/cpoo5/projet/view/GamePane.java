package fr.uparis.informatique.cpoo5.projet.view;
import fr.uparis.informatique.cpoo5.projet.model.Food;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegment;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegmentIA;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import java.util.List;
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


        // Trouver la tête du serpent
        SnakeSegment head = game.getSnake().get(0);
        // Calculer la différence pour centrer la vue
        double offsetX = WIDTH / 2 - head.getX();
        double offsetY = HEIGHT / 2 - head.getY();

        // Afficher tous les éléments décalés par la différence calculée
        for (Food food : game.getFoodList()) {
            gc.setFill(Color.RED);
            gc.fillOval(food.getX() + offsetX, food.getY() + offsetY, food.getSize(), food.getSize());
        }


        // Dessiner le serpent
        for (SnakeSegment segment : game.getSnake()) {
            gc.setFill(Color.GREEN);
            gc.fillOval(segment.getX() + offsetX, segment.getY() + offsetY, SnakeSegment.SIZE, SnakeSegment.SIZE);
        }

        // Dessiner les IA
        for (List<SnakeSegmentIA> ia : game.getIA()) {
            for (SnakeSegmentIA segment : ia) {
                gc.setFill(Color.BLUE);
                gc.fillOval(segment.getX() + offsetX, segment.getY() + offsetY, segment.getSize(), segment.getSize());
            }
        }
    }
    
}
