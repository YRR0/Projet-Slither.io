package fr.uparis.informatique.cpoo5.projet.model;
import fr.uparis.informatique.cpoo5.projet.controller.SnakeIAController;
import fr.uparis.informatique.cpoo5.projet.model.factoryColor.RandomColorFactory;
import java.util.ArrayList;
import java.util.List;
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
    private SnakeIAController iaController;

    private double directionX = 1;
    private double directionY = 0;

    public Game() {
        snake.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
        generateIA();
        generateAllFood();
        iaController = new SnakeIAController(this);
    }

    //Pour générer plusieurs aliments et les stocker
    private void generateAllFood() {
        for (int i = 0; i < 50; i++) {
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

        double newX = head.getX() + directionX * (speed ? INC_SPEED : 1);
        double newY = head.getY() + directionY * (speed ? INC_SPEED : 1);

        // Vérifier la collision avec le corps du serpent
        if (checkSelfCollision(newX, newY)) {
            // Gérer la collision ici (par exemple, redémarrer le jeu, afficher un message, etc.)
            // Dans cet exemple, on réinitialise simplement le serpent et la nourriture
            this.setPaused(true);
            //snake.clear();
            //snake.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
            //foodList.clear();
            //generateAllFood();
        }
        else if (checkCollisionWithIA()) {
            // Gérer la collision avec une IA
            //this.setPaused(true);
            //snakeIA.clear();
        }
        else {
            // Si aucune collision avec le corps, continuer normalement
            grow(newX, newY, snake);
            updateIA();
        }
    }

    private void grow(double newX, double newY, List<SnakeSegment> snake){
        // On utilise une liste qui copie pour éviter les exceptions ConcurrentModificationException
        List<Food> foodCopy = new ArrayList<>(foodList);
        for (Food food : foodCopy) {
            // Vérifier la collision avec la nourriture
            if (isCollidingWithFood(food, snake.get(0))) {
                // Taille augmente en fonction de la taille de la nourriture
                for (int i = 0; i < food.getSize(); ++i) {
                    snake.add(i,new SnakeSegment(newX, newY,food.getColor()));
                }
                // On retire de la liste originale
                foodList.remove(food);
                // On génère une nouvelle Food
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
                    snakeIA.add(i, new SnakeSegmentIA(newX, newY));
                }
                //On retire de la liste originale
                foodList.remove(food);
                //On génère une nouvelle Food
                generateFood();
            }
        }
        //On met à jour les segments des IA
        snakeIA.remove(snakeIA.size() - 1);
        snakeIA.add(0, new SnakeSegmentIA(newX, newY));     
    }

    private boolean isCollidingWithFood(Food f, SnakeSegment head) {
        double offsetX = WIDTH / 2 - head.getX();
        double offsetY = HEIGHT / 2 - head.getY();

        // Normaliser les coordonnées de la nourriture après ajustement
        double normalizedFoodX = (f.getX() + offsetX + WIDTH) % WIDTH;
        double normalizedFoodY = (f.getY() + offsetY + HEIGHT) % HEIGHT;

        // Normaliser les coordonnées de la tête après ajustement
        double normalizedHeadX = (head.getX() + offsetX + WIDTH) % WIDTH;
        double normalizedHeadY = (head.getY() + offsetY + HEIGHT) % HEIGHT;

        // Vérifier la collision avec la zone de la nourriture
        return normalizedHeadX < normalizedFoodX + f.getSize() &&
                normalizedHeadX + SnakeSegment.SIZE > normalizedFoodX &&
                normalizedHeadY < normalizedFoodY + f.getSize() &&
                normalizedHeadY + SnakeSegment.SIZE > normalizedFoodY;
    }

    private boolean checkSelfCollision(double newX, double newY) {
        // Vérifier la collision avec le propre corps du serpent
        for (int i = 1; i < snake.size(); i++) {
            SnakeSegment segment = snake.get(i);

            // Comparer les coordonnées avec une petite marge d'erreur
            double collisionMargin = 0.1;
            if (Math.abs(newX - segment.getX()) < collisionMargin &&
                    Math.abs(newY - segment.getY()) < collisionMargin) {
                System.out.println("I " + i + " newX: " + newX + " newY: " + newY + " x: " + segment.getX() + " y: " + segment.getY());
                return true;  // Collision détectée
            }
        }
        return false;  // Aucune collision
    }

    private boolean checkCollisionWithIA() {
        // Récupérer la tête du serpent du joueur
        SnakeSegment head = snake.get(0);

        // Calculer la différence pour centrer la vue
        double offsetX = WIDTH / 2 - head.getX();
        double offsetY = HEIGHT / 2 - head.getY();

        // Normaliser les coordonnées de la tête après ajustement
        double normalizedHeadX = (head.getX() + offsetX + WIDTH) % WIDTH;
        double normalizedHeadY = (head.getY() + offsetY + HEIGHT) % HEIGHT;

        // Parcourir toutes les IA pour vérifier la collision avec leur tête
        for (List<SnakeSegmentIA> ia : snakeIA) {
            SnakeSegmentIA iaHead = ia.get(0);

            // Normaliser les coordonnées de la tête de l'IA après ajustement
            double normalizedIAHeadX = (iaHead.getX() + offsetX + WIDTH) % WIDTH;
            double normalizedIAHeadY = (iaHead.getY() + offsetY + HEIGHT) % HEIGHT;

            // Vérifier la collision avec la tête de l'IA
            if (normalizedHeadX < normalizedIAHeadX + SnakeSegment.SIZE &&
                    normalizedHeadX + SnakeSegment.SIZE > normalizedIAHeadX &&
                    normalizedHeadY < normalizedIAHeadY + SnakeSegment.SIZE &&
                    normalizedHeadY + SnakeSegment.SIZE > normalizedIAHeadY) {
                // Collision détectée
                System.out.println("Collision IA");
                convertIAToFood(snakeIA.get(0));
                return true;
            }
        }
        // Aucune collision avec les IA
        return false;
    }

    private void convertIAToFood(List<SnakeSegmentIA> ia) {
        // Convertir chaque segment de l'IA en une Food
        for (SnakeSegmentIA segmentIA : ia) {
            double x = segmentIA.getX();
            double y = segmentIA.getY();

            // Créer une nouvelle Food à la position de l'ancien segment de l'IA
            Food newFood = new Food(x, y, segmentIA.getColor());
            // Ajouter la nouvelle Food à la liste des Food
            foodList.add(newFood);
        }

        // Retirer l'IA de la liste des IA, elle va réapparaître autre part
        snakeIA.remove(ia);
        // Générer une nouvelle IA à un emplacement aléatoire
        generateIA();
    }

    public List<SnakeSegment> getSnake() {
        return snake;
    }

    public List<List<SnakeSegmentIA>> getIA(){
        return snakeIA;
    }

    public double getSpeed(){
        return SPEED;
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    private void generateFood() {
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

    public boolean PlayerIsTooBig(){
        return snake.size() > 200;
    }

    private boolean isCloseToPlayer(List<SnakeSegmentIA> ia){
        SnakeSegment head = snake.get(0);

        // On calcule les distances entre IA et le joueur
        double distanceToPlayerX = head.getX() - ia.get(0).getX();
        double distanceToPlayerY = head.getY() - ia.get(0).getY();

        // Théorème de Pythagore pour avoir la distance
        double sq1 = Math.pow(distanceToPlayerX, 2);
        double sq2 = Math.pow(distanceToPlayerY, 2);
        double sum = sq1 + sq2;

        // On prend en compte l'offset
        double offsetX = WIDTH / 2 - head.getX();
        double offsetY = HEIGHT / 2 - head.getY();

        // On met à jour les distances avec l'offset
        double distanceToPlayerWithOffsetX = distanceToPlayerX + offsetX;
        double distanceToPlayerWithOffsetY = distanceToPlayerY + offsetY;

        // Théorème de Pythagore pour la distance avec l'offset
        double sumWithOffset = Math.pow(distanceToPlayerWithOffsetX, 2) + Math.pow(distanceToPlayerWithOffsetY, 2);

        // Si le joueur est à moins de 250 pixels, l'IA va vers le joueur
        return Math.sqrt(sumWithOffset) < 500;
    }

    private void updateIA(){
        for (List<SnakeSegmentIA> ia : this.snakeIA) {
            //On vérifie si une IA est proche
            if(isCloseToPlayer(ia)){
                //Dans ce cas là on applique la stratégie kill
                iaController.moveIaKillStrat(ia);
            }
            //Si le joueur dépasse une certaine taille
            else if(PlayerIsTooBig()){
                iaController.moveIaKillStrat(ia);
            }
            else{
                //Sinon on applique la stratégie food
                iaController.moveIaFoodStrat(ia);
            }
            //On applique les collisions avec la nourriture
            growIA(ia.get(0).getX(), ia.get(0).getY(), ia);
        }
    }

    public double getWidth() {
        return WIDTH;
    }
    public double getHeight() {
        return HEIGHT;
    }

    public double getDirectionX(){return this.directionX;}
    public double getDirectionY(){return this.directionY;}


    public void increaseSpeed() {
       speed = true;
    }
    
    public void decreaseSpeed() {
        speed = false;
    }

    public boolean isPaused(){
        return this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void togglePause() {
        setPaused(!isPaused());
    }
}