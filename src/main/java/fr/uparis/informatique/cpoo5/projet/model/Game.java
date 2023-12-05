package fr.uparis.informatique.cpoo5.projet.model;
import fr.uparis.informatique.cpoo5.projet.model.factoryColor.RandomColorFactory;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.stage.Screen;

public class Game {

    private boolean paused = false;
    private Food food;
    private List<Food> foodList = new ArrayList<>(); //Pour stocker tous les aliments de la map
    private static final double SPEED = 0.5;

    private static final double INC_SPEED = 2.0;
    private boolean speed;
    private static final int WIDTH = (int) Screen.getPrimary().getBounds().getWidth();
    private static final int HEIGHT = (int) Screen.getPrimary().getBounds().getHeight();
    private List<SnakeSegment> snake = new ArrayList<>();
    private List<List<SnakeSegmentIA>> snakeIA = new ArrayList<>();



    private double directionX = 1;
    private double directionY = 0;

    public Game() {
        snake.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
        generateIA();
        generateAllFood();
    }

    //Pour générer plusieurs aliments et les stocker
    private void generateAllFood() {
        for (int i = 0; i < 100; i++) {
            generateFood();
        }
    }

    private void generateIA(){
        //On génère un nombre aléatoire d'IA pour la partie
        int nbrIA = randomGenerator(1, 3); 
        int randPosX;
        int randPosY;
        List<SnakeSegmentIA> ia;
        for(int i = 0; i<nbrIA; ++i){
            randPosX = randomGenerator(0, WIDTH);
            randPosY = randomGenerator(0, HEIGHT);
            ia = new ArrayList<>();
            ia.add(new SnakeSegmentIA(randPosX, randPosY));
            snakeIA.add(ia);
        }
    }

    public int randomGenerator(int min, int max){
        int range = max - min + 1;
        return (int)(Math.random() * range) + min;
    }

    public void update() {
        SnakeSegment head = snake.get(0);
        double newX = head.getX() + directionX * SPEED * (speed ? INC_SPEED : 1); // vérification de boost
        double newY = head.getY() + directionY * SPEED * (speed ? INC_SPEED : 1);

        grow(newX, newY, snake);

        //On supprime le dernier segment du serpent s'il n'a pas mangé de nourriture
        snake.remove(snake.size() - 1);
        snake.add(0, new SnakeSegment(newX, newY));
        moveIaKillStrat();
    }

    private void grow(double newX, double newY, List<SnakeSegment> snake){
        // On utilise une liste qui copie pour éviter les exceptions ConcurrentModificationException
        List<Food> foodCopy = new ArrayList<>(foodList);
        for (Food food : foodCopy) {
            // Vérifier la collision avec la nourriture
            if (isCollidingWithFood(food, snake.get(0))) {
                // Taille augmente en fonction de la taille de la nourriture
                for (int i = 0; i < food.getSize() / 2; ++i) {
                    /*if (i == 0) {
                        // Utiliser la couleur spécifiée pour la tête
                        snake.add(i, new SnakeSegment(newX, newY, Color.BLACK));
                    } else {
                        // Utiliser la couleur verte par défaut pour le reste du corps
                        snake.add(i, new SnakeSegment(newX, newY,Color.GREEN));
                    }*/
                    snake.add(i,new SnakeSegment(newX, newY,food.getColor()));
                }
                // On retire de la liste originale
                foodList.remove(food);
                // On génère une nouvelle Food
                generateFood();
            }
        }
    }


    private void growIA(double newX, double newY, List<SnakeSegmentIA> snakeIA){
        int formerSize = snakeIA.get(0).getSize();
        //On utilise une liste qui copie pour éviter les exceptions ConcurrentModificationException
        List<Food> foodCopy = new ArrayList<>(foodList);
        for (Food food : foodCopy) {
            // Vérifier la collision avec la nourriture
            if (isCollidingWithFood(food, snakeIA.get(0))) {
                //Taille augmente en fonction de la taille de la nourriture
                for(int i = 0; i<food.getSize(); ++i){
                    SnakeSegmentIA newSeg = new SnakeSegmentIA(newX, newY);
                    newSeg = new SnakeSegmentIA(newX, newY);
                    newSeg.setSize(formerSize);
                    snakeIA.add(i, newSeg);
                }
                //On retire de la liste originale
                foodList.remove(food);
                //On génère une nouvelle Food
                generateFood();
            }
        }
        //On met à jour les segments des IA
        snakeIA.remove(snakeIA.size() - 1);
        SnakeSegmentIA newSeg1 = new SnakeSegmentIA(newX, newY);
        newSeg1.setSize(formerSize);
        snakeIA.add(0, newSeg1);
    }

    private boolean isCollidingWithFood(Food f, SnakeSegment head) {
        // Vérifier la collision avec la zone de la nourriture
        return head.getX() < f.getX() + f.getSize() &&
                head.getX() + SnakeSegment.SIZE > f.getX() &&
                head.getY() < f.getY() + f.getSize() &&
                head.getY() + SnakeSegment.SIZE > f.getY();
    }



    public List<SnakeSegment> getSnake() {
        return snake;
    }

    public List<List<SnakeSegmentIA>> getIA(){
        return snakeIA;
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    private void generateFood() {
        SnakeSegment head = snake.get(0);

        double x = Math.random() * WIDTH ;
        double y = Math.random() * HEIGHT;

        //On ajoute à chaque fois dans la liste
        RandomColorFactory f = new RandomColorFactory();
        f.generateColor();
        foodList.add(new Food(x, y,f.generateColor()));
    }

    public Food getFood() {
        return food;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public void moveIaKillStrat() {
        for (List<SnakeSegmentIA> ia : this.snakeIA) {
            SnakeSegment head = snake.get(0);
            //On calcule la différence entre les coordonnées du snake et des IA 
            double directionToPlayerX = head.getX() - ia.get(0).getX();
            double directionToPlayerY = head.getY() - ia.get(0).getY();
            //On calcule l'angle des IA par rapport au snake
            double angleToPlayer = Math.atan2(directionToPlayerY, directionToPlayerX);
            //On met à jour la direction
            ia.get(0).setDirection(Math.cos(angleToPlayer), Math.sin(angleToPlayer));
            //On calcule les nouvelles coordonnées pour la tête des IA 
            double newX = ia.get(0).getX() + ia.get(0).getDirectionX() * (SPEED/2);
            double newY = ia.get(0).getY() + ia.get(0).getDirectionY() * (SPEED/2);
            //On met à jour les positions
            //if(!isOffLimits(newX, newY)){
                growIA(newX, newY, ia);
                ia.get(0).setX(newX);
                ia.get(0).setY(newY);
            //}
        }
    }

    public double getWidth() {
        return WIDTH;
    }
    public double getHeight() {
        return HEIGHT;
    }

    public void increaseSpeed() {
       speed = true;
    }
    public void decreaseSpeed() {
        speed = false;
    }

    public boolean isPaused(){return this.paused;}

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
    public void togglePause() {
        setPaused(!isPaused());
    }


}