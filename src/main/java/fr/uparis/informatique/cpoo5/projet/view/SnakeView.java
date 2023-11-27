package fr.uparis.informatique.cpoo5.projet.view;
import fr.uparis.informatique.cpoo5.projet.model.SnakeModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SnakeView {

    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();

    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();;

    private Image background;
    private GraphicsContext gc;
    private SnakeModel model;
    private Scene scene;
    public SnakeView(Stage primaryStage, SnakeModel model) {
        this.model = model;
        primaryStage.setTitle("Snake");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        gc = canvas.getGraphicsContext2D();
    }

    public Scene getScene(){return this.scene;}

    public void update() {
        drawBackground();
        drawFood();
        drawSnake();
        drawScore();
    }

    private void drawBackground() {
        // Fond noir
        gc.setFill(javafx.scene.paint.Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Couleur des bords
        /*gc.setStroke(javafx.scene.paint.Color.WHITE);

        // Dessiner les rectangles avec bordures blanches
        for (int i = 0; i < model.getRows(); i++) {
            for (int j = 0; j < model.getRows(); j++) {
                gc.setFill(javafx.scene.paint.Color.BLACK);
                gc.fillRect(i * model.getSquareSize(), j * model.getSquareSize(), model.getSquareSize(), model.getSquareSize());

                // Dessiner la bordure blanche autour du rectangle
                gc.strokeRect(i * model.getSquareSize(), j * model.getSquareSize(), model.getSquareSize(), model.getSquareSize());
            }
        }*/
    }

    private void drawFood() {
        gc.drawImage(model.getFoodImage(),
                model.getFoodX() * model.getSquareSize(),
                model.getFoodY() * model.getSquareSize(),
                model.getSquareSize(),
                model.getSquareSize());
    }

    private void drawSnake() {
        gc.setFill(javafx.scene.paint.Color.web("orange"));
        gc.fillRoundRect(model.getSnakeHead().getX() * model.getSquareSize(), model.getSnakeHead().getY() * model.getSquareSize(), model.getSquareSize()-1, model.getSquareSize()-1, model.getSquareSize()-10, model.getSquareSize()-10);

        for (int i = 1; i < model.getSnakeBody().size(); i++) {
            gc.fillRoundRect(model.getSnakeBody().get(i).getX() * model.getSquareSize(), model.getSnakeBody().get(i).getY() * model.getSquareSize(), model.getSquareSize()-1,
                    model.getSquareSize()-1, model.getSquareSize()/2, model.getSquareSize()/2);
        }
    }

    private void drawScore() {
        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.setFont(new javafx.scene.text.Font("Digital-7", 35));
        gc.fillText("Score: " + model.getScore(), 10, 35);
    }
}