package fr.uparis.informatique.cpoo5.projet;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SnakeGame extends Application {

    private static final int WIDTH = (int)Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int)Screen.getPrimary().getBounds().getHeight();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        GamePane gamePane = new GamePane(game);

        Scene scene = new Scene(gamePane, WIDTH, HEIGHT);

        scene.setOnMouseMoved(gamePane::handleMouseMove);

        primaryStage.setTitle("Slither.io");
        primaryStage.setScene(scene);
        primaryStage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                game.update();
                gamePane.render();
            }
        }.start();
    }
}

class Game {
    private static final double SPEED = 1.0;
    private static final int WIDTH = (int)Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int)Screen.getPrimary().getBounds().getHeight();
    private List<SnakeSegment> snake = new ArrayList<>();
    private double directionX = 1;
    private double directionY = 0;

    public Game() {
        snake.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
    }

    public void update() {
        SnakeSegment head = snake.get(0);
        double newX = head.getX() + directionX * SPEED;
        double newY = head.getY() + directionY * SPEED;
        snake.add(0, new SnakeSegment(newX, newY));

        // Keep the snake size reasonable
        if (snake.size() > 20) {
            snake.remove(snake.size() - 1);
        }
    }

    public List<SnakeSegment> getSnake() {
        return snake;
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }
}

class GamePane extends StackPane {
    private static final int WIDTH = (int)Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int)Screen.getPrimary().getBounds().getHeight();
    private Game game;
    private Canvas canvas;

    public GamePane(Game game) {
        this.game = game;
        this.canvas = new Canvas(WIDTH, HEIGHT);
        getChildren().add(canvas);
    }

    public void handleMouseMove(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();

        SnakeSegment head = game.getSnake().get(0);
        double angle = Math.atan2(mouseY - head.getY(), mouseX - head.getX());
        game.setDirection(Math.cos(angle), Math.sin(angle));
    }

    public void render() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, WIDTH, HEIGHT);

        for (SnakeSegment segment : game.getSnake()) {
            gc.setFill(Color.GREEN);
            gc.fillOval(segment.getX(), segment.getY(), SnakeSegment.SIZE, SnakeSegment.SIZE);
        }
    }
}

class SnakeSegment {
    public static final double SIZE = 20;

    private double x;
    private double y;

    public SnakeSegment(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}