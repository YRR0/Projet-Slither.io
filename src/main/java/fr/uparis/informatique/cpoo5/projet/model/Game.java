package fr.uparis.informatique.cpoo5.projet.model;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.Screen;

public class Game {
    private Food food;
    private List<Food> foodList = new ArrayList<>(); //Pour stocker tous les aliments de la map
    private static final double SPEED = 0.5;
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
    private List<SnakeSegment> snake = new ArrayList<>();

    private double directionX = 1;
    private double directionY = 0;

    public Game() {
        snake.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
        generateAllFood();
    }

    //Pour générer plusieurs aliments et les stocker
    private void generateAllFood() {
        for (int i = 0; i < 100; i++) {
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
    
     public void handleMouseMove(double mouseX, double mouseY) {
        SnakeSegment head = snake.get(0);
        double angle = Math.atan2(mouseY - head.getY(), mouseX - head.getX());
        setDirection(Math.cos(angle), Math.sin(angle));
    }
}