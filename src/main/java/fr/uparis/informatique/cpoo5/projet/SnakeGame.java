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
import javafx.stage.StageStyle;
import java.util.ArrayList;
import java.util.List;



public class SnakeGame extends Application {

    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Game game = new Game();
        GamePane gamePane = new GamePane(game);

        Scene scene = new Scene(gamePane, WIDTH, HEIGHT);

        scene.setOnMouseMoved(gamePane::handleMouseMove);

        //primaryStage.setTitle("Slither.io");
        primaryStage.initStyle(StageStyle.UNDECORATED);
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
    private Food food;
    private List<Food> foodList = new ArrayList<>(); //Pour stocker tous les aliments de la map
    private static final double SPEED = 1.0;
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
    private List<SnakeSegment> snake = new ArrayList<>();

    private double directionX = 1;
    private double directionY = 0;

    public Game() {
        snake.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
        snakeBot.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
        generateAllFood();
    }

    //Pour générer plusieurs aliments et les stocker
    private void generateAllFood() {
        for (int i = 0; i < 20; i++) { 
            generateFood();
        }
    }

    public void update() {
        SnakeSegment head = snake.get(0);
        double newX = head.getX() + directionX * SPEED;
        double newY = head.getY() + directionY * SPEED;
        if(!isOffLimits(newX, newY)){
            grow(newX, newY, head);
        }
    }

    private void grow(double newX, double newY, SnakeSegment head){
        //On utilise une liste qui copie pour éviter les exceptions ConcurrentModificationException
        List<Food> foodCopy = new ArrayList<>(foodList); 
        for (Food food : foodCopy) {
            // Vérifier la collision avec la nourriture
            if (isCollidingWithFood(food, head)) {
                for(int i = 0; i<food.getSize()/2; ++i){ //Taille augmente en fonction de la taille de la nourriture
                    snake.add(i, new SnakeSegment(newX, newY));
                }
                foodList.remove(food); //On retire de la liste originale
                generateFood(); //On génère une nouvelle Food
            }
        }       
        //On supprime le dernier segment du serpent s'il n'a pas mangé de nourriture
        snake.remove(snake.size() - 1);
        snake.add(0, new SnakeSegment(newX, newY));
    }

    private boolean isCollidingWithFood(Food f, SnakeSegment head) {
        // Vérifier la collision avec la zone de la nourriture
        return head.getX() < f.getX() + f.getSize() &&
                head.getX() + SnakeSegment.SIZE > f.getX() &&
                head.getY() < f.getY() + f.getSize() &&
                head.getY() + SnakeSegment.SIZE > f.getY();
    }

    private boolean isOffLimits(double x, double y) {
        return x > WIDTH - SnakeSegment.SIZE || x < 0 + SnakeSegment.SIZE || y > HEIGHT - SnakeSegment.SIZE || y < 0 + SnakeSegment.SIZE;
    }
    
    public List<SnakeSegment> getSnake() {
        return snake;
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    private void generateFood() {
        double x = Math.random() * WIDTH;
        double y = Math.random() * HEIGHT;
        foodList.add(new Food(x, y)); //On ajoute à chaque fois dans la liste
    }

    public Food getFood() {
        return food;
    }

    public List<Food> getFoodList() {
        return foodList;
    }
}

class GamePane extends StackPane {
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
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
}

class SnakeSegment {
    public static final double SIZE = 25;

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

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }
}

class Food {
    private double size;
    private double x;
    private double y;

    public Food(double x, double y) {
        int max = 20;
        int min = 10;
        int range = max - min + 1;
        this.size = (int)(Math.random() * range) + min; //Taille de nourriture aléatoire
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize(){
        return this.size;
    }
}
