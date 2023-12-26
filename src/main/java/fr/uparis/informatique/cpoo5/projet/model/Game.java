package fr.uparis.informatique.cpoo5.projet.model;
import java.awt.Dimension;
import java.awt.Toolkit;
import fr.uparis.informatique.cpoo5.projet.controller.SnakeIAController;
import fr.uparis.informatique.cpoo5.projet.model.factoryColor.RandomColorFactory;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int id ;
    private static  double WIDTH ;
    private static  double HEIGHT ;

    private boolean paused = false;
    private boolean speed;
    private List<Food> foodList = new ArrayList<>(); //Pour stocker tous les aliments de la map
    private List<SnakeSegment> snake = new ArrayList<>();
    private Map<Integer, List<SnakeSegment>> playerSnakes = new HashMap<>();

    private List<List<SnakeSegmentIA>> snakeIA = new ArrayList<>();
    private SnakeIAController iaController;
    private double directionX = 1;
    private double directionY = 0;
    private GameConfig gameConfig;

    public Game() {
        initializeDimensions();
        this.gameConfig = new GameConfig();

        // Pour la création du reseau on ne crée le serpent que quand le joueur se connecte
        //snake.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
        generateIA();
        generateAllFood();
        iaController = new SnakeIAController(this);
    }

    private void initializeDimensions() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();

        WIDTH = screenSize.getWidth();
        HEIGHT = screenSize.getHeight();
    }

    //Pour générer plusieurs aliments et les stocker
    private void generateAllFood() {
        for (int i = 0; i < gameConfig.getInitialFoodCount(); i++) {
            generateFood();
        }
    }

    private void generateIA(){
        //On génère un nombre aléatoire d'IA pour la partie
        int nbrIA = randomGenerator(gameConfig.getMinIA(), gameConfig.getMaxIA());
        int randPosX;
        int randPosY;
        List<SnakeSegmentIA> ia;
        for(int i = 0; i<nbrIA; ++i){
            randPosX = randomGenerator(0, (int)WIDTH);
            randPosY = randomGenerator(0, (int)HEIGHT);
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
        for (int playerId : playerSnakes.keySet()) {
                List<SnakeSegment> playerSnake = playerSnakes.get(playerId);
               // System.out.println("index "+id);
                if(playerSnake != null && !playerSnake.isEmpty()) {
                    SnakeSegment head = playerSnake.get(0);

                    double newX = head.getX() + directionX * (speed ? gameConfig.getIncSpeed() : 1);
                    double newY = head.getY() + directionY * (speed ? gameConfig.getIncSpeed() : 1);

                    // Vérifier la collision avec le corps du serpent
                    if (checkSelfCollision(newX, newY)) {
                        //this.setPaused(true);
                        //snake.clear();
                    } else {
                        // Si aucune collision avec le corps, continuer normalement
                        grow(newX, newY, playerSnake);
                        updateIA();
                    }

                    if (checkCollisionWithIA()) {
                        //this.setPaused(true);
                    }
                }
                else{
                   // System.out.println(" Liste vide imossible d'update ");
                }
        }

    }

    private void grow(double newX, double newY, List<SnakeSegment> snake){
        List<Food> foodCopy = new ArrayList<>(foodList);

        int totalFoodSize = 0;
         for (Food food : foodCopy) {
            if (isCollidingWithFood(food, snake.get(0))) {
                totalFoodSize += food.getSize();
                foodList.remove(food);
            }
        }

        for (int i = 0; i < totalFoodSize; ++i) {
            snake.add(0, new SnakeSegment(newX, newY));
        }

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
        double offsetX =WIDTH / 2 - head.getX();
        double offsetY = HEIGHT / 2 - head.getY();

        // Normaliser les coordonnées de la nourriture après ajustement
        double normalizedFoodX = (f.getX() + offsetX + WIDTH) % WIDTH;
        double normalizedFoodY = (f.getY() + offsetY + HEIGHT) % HEIGHT;

        // Normaliser les coordonnées de la tête après ajustement
        double normalizedHeadX = (head.getX() + offsetX +WIDTH) % WIDTH;
        double normalizedHeadY = (head.getY() + offsetY + HEIGHT) % HEIGHT;

        // Vérifier la collision avec la zone de la nourriture
        return normalizedHeadX < normalizedFoodX + f.getSize() &&
                normalizedHeadX + SnakeSegment.SIZE > normalizedFoodX &&
                normalizedHeadY < normalizedFoodY + f.getSize() &&
                normalizedHeadY + SnakeSegment.SIZE > normalizedFoodY;
    }

    private boolean checkSelfCollision(double newX, double newY) {
        for (int i = 1; i < playerSnakes.get(id).size() ; i++) {
            SnakeSegment segment = playerSnakes.get(id).get(i);

            double distance = Math.hypot(newX - segment.getX(), newY - segment.getY());

            double collisionMargin = 0.5;
            if (distance < collisionMargin) {
                System.out.println("Collision avec le corps du serpent détectée. newX: "+newX+ " newY: "+newY+"   " + segment.getX() +" "+ segment.getX()+ "\n");
                return true;  // Collision détectée
            }
        }
        return false;  // Aucune collision
    }

    private boolean checkCollisionWithIA() {
        SnakeSegment head = playerSnakes.get(id).get(0);

        double offsetX = WIDTH / 2 - head.getX();
        double offsetY = HEIGHT / 2 - head.getY();

        double normalizedHeadX = (head.getX() + offsetX + WIDTH) % WIDTH ;
        double normalizedHeadY = (head.getY() + offsetY + HEIGHT) %HEIGHT;

        for (List<SnakeSegmentIA> ia : snakeIA) {
            SnakeSegmentIA iaHead = ia.get(0);

            double normalizedIAHeadX = (iaHead.getX() + offsetX + WIDTH ) % HEIGHT ;
            double normalizedIAHeadY = (iaHead.getY() + offsetY + WIDTH) % HEIGHT;

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
        return false;
    }

    private void convertIAToFood(List<SnakeSegmentIA> ia) {
        for (SnakeSegmentIA segmentIA : ia) {
            double x = segmentIA.getX();
            double y = segmentIA.getY();

            Food newFood = new Food(x, y, segmentIA.getColor());
            newFood.setDead_Food(true);

            foodList.add(newFood);
        }
        snakeIA.remove(ia);
        generateIA();
    }

    // Attribuer un serpent à un joueur lorsqu'il se connecte
    public void assignSnakeToPlayer(int playerId) {
        System.out.println("Assignation Id : " + playerId);
        id = playerId;
        playerSnakes.put(id, snake);
        snake.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
        if(playerSnakes.containsKey(id)){
                System.out.println("Assignation ok" + id);
        }
        else {
            System.out.println("Assignation pas ok");
        }
    }

    public List<SnakeSegment> getSnake() {
        //return snake;
        synchronized (playerSnakes) {
            if (playerSnakes != null) {
                List<SnakeSegment> snakeList = playerSnakes.get(id);
                if (playerSnakes.get(id) == null) {
                    System.out.println("Pas Ok " + id);
                    snakeList = new ArrayList<>();
                    playerSnakes.put(id, snakeList);
                    snakeList.add(new SnakeSegment(WIDTH / 2, HEIGHT / 2));
                }
                return playerSnakes.get(this.id);
            }
            return null;
        }
    }

    public List<List<SnakeSegmentIA>> getIA(){
        return snakeIA;
    }

    public Map<Integer, List<SnakeSegment>>  getAllSnakes() {
        return playerSnakes;
    }

    public double getSpeed(){
        return GameConfig.getSPEED();
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    private void generateFood() {
        double x = Math.random() * WIDTH;
        double y = Math.random() * HEIGHT;

        RandomColorFactory f = new RandomColorFactory();
        f.generateColor();
        foodList.add(new Food(x, y,f.generateColor()));
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public boolean PlayerIsTooBig(){
        return playerSnakes.get(id).size() > 200;
    }

    private boolean isCloseToPlayer(List<SnakeSegmentIA> ia){
        SnakeSegment head = playerSnakes.get(id).get(0);

        // On calcule les distances entre IA et le joueur
        double distanceToPlayerX = head.getX() - ia.get(0).getX();
        double distanceToPlayerY = head.getY() - ia.get(0).getY();

        // Théorème de Pythagore pour avoir la distance
        double sq1 = Math.pow(distanceToPlayerX, 2);

        // On prend en compte l'offset
        double offsetX = WIDTH/ 2 - head.getX();
        double offsetY = HEIGHT / 2 - head.getY();

        // On met à jour les distances avec l'offset
        double distanceToPlayerWithOffsetX = distanceToPlayerX + offsetX;
        double distanceToPlayerWithOffsetY = distanceToPlayerY + offsetY;

        // Théorème de Pythagore pour la distance avec l'offset
        double sumWithOffset = Math.pow(distanceToPlayerWithOffsetX, 2) + Math.pow(distanceToPlayerWithOffsetY, 2);

        // Si le joueur est à moins de 500 pixels, l'IA va vers le joueur
        return Math.sqrt(sumWithOffset) < 500;
    }

    private void updateIA(){
        for (List<SnakeSegmentIA> ia : this.snakeIA) {
            if(isCloseToPlayer(ia)){
                //Dans ce cas là on applique la stratégie kill
                iaController.moveIaKillStrat(ia);
            }
            else if(PlayerIsTooBig()){
                iaController.moveIaKillStrat(ia);
            }
            else{
                //Sinon on applique la stratégie food
                iaController.moveIaFoodStrat(ia);
            }
            growIA(ia.get(0).getX(), ia.get(0).getY(), ia);
        }
    }

    public int getPlayerId(){
        return this.id;
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

    public boolean isPaused(){
        return !this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void togglePause() {
        setPaused(isPaused());
    }

    public Map<Integer, List<SnakeSegment>> getPlayerSnakes() {
        return this.playerSnakes;
    }

    // Quitter la partie si c'est gameOver à rajouter
}