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
        if(!isOffLimits(x, y)){
            foodList.add(new Food(x, y));
        }
        else{
            generateFood();
        }
    }

    public Food getFood() {
        return food;
    }

    public List<Food> getFoodList() {
        return foodList;
    }
    
    private Food closestFood(SnakeSegmentIA head) {
        //On prend comme min la valeur max de double pour être sûr
        double distMin = Double.MAX_VALUE;
        Food closest = null;
        for(Food food : foodList) {
            //Pour chaque nourriture on calcule la distance de coordonnées
            double distanceToFoodX = head.getX() - food.getX();
            double distanceToFoodY = head.getY() - food.getY();
            //Théorème de Pythagore
            double dist = Math.sqrt((distanceToFoodX * distanceToFoodX) + (distanceToFoodY * distanceToFoodY));
            //Si on trouve une distance plus petite on change 
            if(dist < distMin) {
                distMin = dist;
                closest = food;
            }
        }
        return closest;
    }

    private void moveIaFoodStrat(List<SnakeSegmentIA> ia) {
        SnakeSegmentIA head = ia.get(0);
        //On récupère la nourriture la plus proche        
        Food closestFood = closestFood(head);
        //On calcule leur distance
        double distanceToFoodX = closestFood.getX() - head.getX();
        double distanceToFoodY = closestFood.getY() - head.getY();
        //On calcule l'angle
        double angleToFood = Math.atan2(distanceToFoodY, distanceToFoodX);
        //On met à jour la direction de l'IA
        ia.get(0).setDirection(Math.cos(angleToFood), Math.sin(angleToFood));
        //On calcule la nouvelle position
        double newX = head.getX() + ia.get(0).getDirectionX() * SPEED;
        double newY = head.getY() + ia.get(0).getDirectionY() * SPEED;
        //On met à jour dans le cas où ce n'est pas hors limites        
        if (!isOffLimits(newX, newY)) {
            ia.get(0).setX(newX);
            ia.get(0).setY(newY);
        }
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
        //On calcule les distances entre IA et le joueur
        double distanceToPlayerX = head.getX() - ia.get(0).getX();
        double distanceToPlayerY = head.getY() - ia.get(0).getY();
        //Théorème de Pythagore pour avoir la distance
        double sq1 = Math.pow(distanceToPlayerX, 2);
        double sq2 = Math.pow(distanceToPlayerY, 2);
        double sum = sq1 + sq2;
        //Si le joueur est à moins de 250 pixels, l'IA va vers le joueur
        return Math.sqrt(sum) < 250;
    }

    private void updateIA(){
        for (List<SnakeSegmentIA> ia : this.snakeIA) {
            //On vérifie si une IA est proche
            if(isCloseToPlayer(ia)){
                //Dans ce cas là on applique la stratégie kill
                moveIaKillStrat(ia);
                growIA(ia.get(0).getX(), ia.get(0).getY(), ia);
            }
            else{
                //Sinon on applique la stratégie food
                moveIaFoodStrat(ia);
                growIA(ia.get(0).getX(), ia.get(0).getY(), ia);
            }
        }
    }
}