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
        for (int i = 0; i < 60; i++) { 
            generateFood();
        }
    }

    private void generateIA(){
        //On génère un nombre aléatoire d'IA pour la partie
        int nbrIA = randomGenerator(1, 1); 
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

    private int randomGenerator(int min, int max){
        int range = max - min + 1;
        return (int)(Math.random() * range) + min;
    }

    public void update() {
        SnakeSegment head = snake.get(0);
        double newX = head.getX() + directionX * SPEED;
        double newY = head.getY() + directionY * SPEED;
        if(!isOffLimits(newX, newY)){
            grow(newX, newY, snake);
        }
        updateIA();
    }

    private void grow(double newX, double newY, List<SnakeSegment> snake){
        //On utilise une liste qui copie pour éviter les exceptions ConcurrentModificationException
        List<Food> foodCopy = new ArrayList<>(foodList); 
        for (Food food : foodCopy) {
            // Vérifier la collision avec la nourriture
            if (isCollidingWithFood(food, snake.get(0))) {
                //Taille augmente en fonction de la taille de la nourriture
                for(int i = 0; i<food.getSize()/2; ++i){
                    snake.add(i, new SnakeSegment(newX, newY));
                }
                //On retire de la liste originale
                foodList.remove(food); 
                //On génère une nouvelle Food
                generateFood(); 
            }
        }     
        //On supprime le dernier segment du serpent s'il n'a pas mangé de nourriture
        snake.remove(snake.size() - 1);
        snake.add(0, new SnakeSegment(newX, newY));  
    }

    private void growIA(double newX, double newY, List<SnakeSegmentIA> snakeIA){
        //On utilise une liste qui copie pour éviter les exceptions ConcurrentModificationException
        List<Food> foodCopy = new ArrayList<>(foodList); 
        for (Food food : foodCopy) {
            // Vérifier la collision avec la nourriture
            if (isCollidingWithFood(food, snakeIA.get(0))) {
                //Taille augmente en fonction de la taille de la nourriture
                for(int i = 0; i<food.getSize(); ++i){
                    SnakeSegmentIA newSeg = new SnakeSegmentIA(newX, newY);
                    newSeg = new SnakeSegmentIA(newX, newY);
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
        snakeIA.add(0, newSeg1);     
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

    public List<List<SnakeSegmentIA>> getIA(){
        return snakeIA;
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    private void generateFood() {
        double x = Math.random() * WIDTH;
        double y = Math.random() * HEIGHT;
        //On ajoute à chaque fois dans la liste
        foodList.add(new Food(x, y)); 
    }

    public Food getFood() {
        return food;
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    /*public void moveIaAléatoire(List<SnakeSegmentIA> ia){
            double newX, newY;
            //On calcule les nouvelles coordonnées pour la tête du serpent
            newX = ia.get(0).getX() + ia.get(0).getDirectionX() * SPEED;
            newY = ia.get(0).getY() + ia.get(0).getDirectionY() * SPEED;
            //Chaque IA à un compte à rebours aléatoire pour changer de direction
            if (ia.get(0).getCountdown() <= 0) {
                //Si on arrive à la fin on calcule un nouvel angle aléatoire pour la direction
                double randAngle = Math.toRadians(randomGenerator(0, 360));    
                //On remet à 0 le compte à rebours       
                ia.get(0).resetCountdown();
                //On change la direction avec la nouvelle
                ia.get(0).setDirection(Math.cos(randAngle), Math.sin(randAngle));
            }
            //On calcule les nouvelles coordonnées pour la tête du serpent
            newX = ia.get(0).getX() + ia.get(0).getDirectionX() * SPEED;
            newY = ia.get(0).getY() + ia.get(0).getDirectionY() * SPEED;
            //On récupère les anciennes informations
            int formerCountdown = ia.get(0).getCountdown();
            //On met à jour les nouveaux paramètres
            ia.get(0).setX(newX);
            ia.get(0).setY(newY);
            ia.get(0).setCountdown(formerCountdown);
            //On décrémente le compte à rebours
            ia.get(0).decreaseCountdown();
            if(!isOffLimits(newX, newY)){
                ia.get(0).setX(newX);
                ia.get(0).setY(newY);
        }         
        
    }*/

    private void moveIaFoodStrat(List<SnakeSegmentIA> ia){
        
    }
    
    
    private void moveIaKillStrat(List<SnakeSegmentIA> ia) {
        SnakeSegment head = snake.get(0);
        //On calcule la différence entre les coordonnées du snake et des IA 
        double distanceToPlayerX = head.getX() - ia.get(0).getX();
        double distanceToPlayerY = head.getY() - ia.get(0).getY();
        //On calcule l'angle des IA par rapport au snake
        double angleToPlayer = Math.atan2(distanceToPlayerY, distanceToPlayerX);
        //On met à jour la direction
        ia.get(0).setDirection(Math.cos(angleToPlayer), Math.sin(angleToPlayer));
        //On calcule les nouvelles coordonnées pour la tête des IA 
        double newX = ia.get(0).getX() + ia.get(0).getDirectionX() * (SPEED/2);
        double newY = ia.get(0).getY() + ia.get(0).getDirectionY() * (SPEED/2);
        //On met à jour les positions
        if(!isOffLimits(newX, newY)){
            ia.get(0).setX(newX);
            ia.get(0).setY(newY);
        }              
    }

    private boolean isCloseToPlayer(List<SnakeSegmentIA> ia){
        SnakeSegment head = snake.get(0);
        double distanceToPlayerX = head.getX() - ia.get(0).getX();
        double distanceToPlayerY = head.getY() - ia.get(0).getY();
        double sq1 = Math.pow(distanceToPlayerX, 2);
        double sq2 = Math.pow(distanceToPlayerY, 2);
        double sum = sq1 + sq2;
        return Math.sqrt(sum) < 250;
    }

    private void updateIA(){
        for (List<SnakeSegmentIA> ia : this.snakeIA) {
            if(isCloseToPlayer(ia)){
                moveIaKillStrat(ia);
                growIA(ia.get(0).getX(), ia.get(0).getY(), ia);
            }
            else{
                growIA(ia.get(0).getX(), ia.get(0).getY(), ia);
            }
        }
    }
}


