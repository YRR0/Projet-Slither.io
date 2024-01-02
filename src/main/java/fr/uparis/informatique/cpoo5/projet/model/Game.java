package fr.uparis.informatique.cpoo5.projet.model;

import fr.uparis.informatique.cpoo5.projet.controller.SnakeIAController;
import fr.uparis.informatique.cpoo5.projet.model.factoryColor.RandomColorFactory;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game {
    private boolean paused = false;
    private boolean gameOver = false;
    private boolean speed;
    private List<Food> foodList = new ArrayList<>(); // Pour stocker tous les aliments de la map
    private SnakeBody snake = new SnakeBody();
    private List<SnakeBody> snakeIA = new ArrayList<>();
    private SnakeIAController iaController;
    private GameConfig gameConfig;
    private double directionX = 1;
    private double directionY = 0;

    public Game() {
        this.gameConfig = new GameConfig();
        for (int j = 0; j < 30; ++j) {
            snake.getSnakeBody().add(new NormalSegment(gameConfig.getWidth() / 2, gameConfig.getHeight() / 2));
        }
        generateIA();
        generateAllFood();
        iaController = new SnakeIAController(this);
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }

    // Pour générer plusieurs aliments et les stocker
    private void generateAllFood() {
        for (int i = 0; i < gameConfig.getInitialFoodCount(); i++) {
            generateFood();
        }
    }

    private void generateIA() {
        // On génère un nombre aléatoire d'IA pour la partie
        int nbrIA = randomGenerator(gameConfig.getMinIA(), gameConfig.getMaxIA());
        int randPosX;
        int randPosY;
        SnakeBody ia;
        for (int i = 0; i < nbrIA; ++i) {
            randPosX = randomGenerator(0, (int) gameConfig.getWidth());
            randPosY = randomGenerator(0, (int) gameConfig.getHeight());
            ia = new SnakeBody();
            for (int j = 0; j < 30; ++j) {
                ia.getSnakeBody().add(new NormalSegment(randPosX, randPosY));
            }
            snakeIA.add(ia);
        }
    }

    private int randomGenerator(int min, int max) {
        int range = max - min + 1;
        return (int) (Math.random() * range) + min;
    }

    public void update() {
        if (!gameOver) {
            SnakeSegment head = snake.getSnakeBody().get(0);

            double newX = head.getX()
                    + directionX * (speed ? gameConfig.getIncSpeed() : GameConfig.getSPEED());
            double newY = head.getY()
                    + directionY * (speed ? gameConfig.getIncSpeed() : GameConfig.getSPEED());

            // Vérifier la collision avec le corps du serpent
            if (checkSelfCollision(newX, newY)) {
                this.setPaused(true);
                gameOver = true;
            } else {
                grow(newX, newY, snake);
                // detection de l'IA avec le coprs d'un joueur
                if (!checkIACollisionWithPlayer()) {
                    updateIA();
                } else {
                    snakeIA.clear();
                    generateIA();
                }
            }

            if (checkCollisionWithIA()) {
                this.setPaused(true);
                gameOver = true;
                System.out.println("Joueur MORT");
            }
        }
    }

    private void grow(double newX, double newY, SnakeBody snake) {
        // Utiliser une copie de la liste de nourriture pour éviter les
        // ConcurrentModificationException
        List<Food> foodCopy = new ArrayList<>(foodList);

        int totalFoodSize = 0;

        for (Food food : foodCopy) {
            if (isCollidingWithFood(food, snake.getSnakeBody().get(0))) {
                totalFoodSize += food.getSize();
                for (int i = 0; i < totalFoodSize; ++i) {
                    // Si la nourriture mangé contenait un pouvoir && que le joueur n'en avait pas
                    // déjà un
                    if (food.getPower() != null && !snake.hasPower()) {
                        addPowerToSnake(food.getPower(), snake);
                    }
                    // Si il a un pouvoir il le garde tant qu'il ne le perd pas peu importe la
                    // nourriture mangé
                    else if (snake.hasPower()) {
                        SnakeSegment head = snake.getSnakeBody().get(0);
                        if (head instanceof ShieldSegment) {
                            snake.getSnakeBody().add(0, new ShieldSegment(newX, newY));
                        } else if (head instanceof WeakSegment) {
                            snake.getSnakeBody().add(0, new WeakSegment(newX, newY));
                        }
                    } else {
                        snake.getSnakeBody().add(0, new NormalSegment(newX, newY));
                    }
                }
                foodList.remove(food);
            }
        }

        // Fais avancer le serpent
        move(snake, newX, newY);
    }

    // Pour ajouter des segments spéciaux si le serpent a mangé une nourriture
    // spéciale
    public void addPowerToSnake(Power power, SnakeBody snake) {
        snake.setPower(power);
        int snakeSize = snake.getSnakeBody().size();
        for (int i = 0; i < snakeSize; ++i) {
            double newX = snake.getSnakeBody().get(i).getX();
            double newY = snake.getSnakeBody().get(i).getY();
            if (power == Power.WEAK) {
                snake.getSnakeBody().set(i, new WeakSegment(newX, newY));
            } else if (power == Power.SHIELD) {
                snake.getSnakeBody().set(i, new ShieldSegment(newX, newY));
            }
        }
    }

    // Pour faire bouger le serpent en fonction des segments qui le composent
    public void move(SnakeBody snake, double newX, double newY) {
        SnakeSegment head = snake.getSnakeBody().get(0);
        if (head instanceof NormalSegment) {
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
            snake.getSnakeBody().add(0, new NormalSegment(newX, newY));
        } else if (head instanceof ShieldSegment) {
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
            snake.getSnakeBody().add(0, new ShieldSegment(newX, newY));
        } else if (head instanceof WeakSegment) {
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
            snake.getSnakeBody().add(0, new WeakSegment(newX, newY));
        }
    }

    private void growIA(double newX, double newY, SnakeBody snakeIA) {
        // On utilise une liste qui copie pour éviter les exceptions
        // ConcurrentModificationException
        List<Food> foodCopy = new ArrayList<>(foodList);
        for (Food food : foodCopy) {
            // Vérifier la collision avec la nourriture
            if (isCollidingWithFood(food, snakeIA.getSnakeBody().get(0))) {
                // Taille augmente en fonction de la taille de la nourriture
                for (int i = 0; i < food.getSize(); ++i) {
                    if (food.getPower() != null && !snakeIA.hasPower()) {
                        System.out.println("IA food");
                        addPowerToSnake(food.getPower(), snakeIA);
                    } else if (snakeIA.hasPower()) {
                        SnakeSegment head = snakeIA.getSnakeBody().get(0);
                        if (head instanceof ShieldSegment) {
                            snakeIA.getSnakeBody().add(0, new ShieldSegment(newX, newY));
                        } else if (head instanceof WeakSegment) {
                            snakeIA.getSnakeBody().add(0, new WeakSegment(newX, newY));
                        }
                    } else {
                        snakeIA.getSnakeBody().add(0, new NormalSegment(newX, newY));
                    }
                }
                // On retire de la liste originale
                foodList.remove(food);
                // On génère une nouvelle Food
                generateFood();
            }
        }
        // On met à jour les segments des IA
        move(snakeIA, newX, newY);
    }

    private boolean isCollidingWithFood(Food f, SnakeSegment head) {
        double offsetX = gameConfig.getWidth() / 2 - head.getX();
        double offsetY = gameConfig.getHeight() / 2 - head.getY();

        // Normaliser les coordonnées de la nourriture après ajustement
        double normalizedFoodX = (f.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
        double normalizedFoodY = (f.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

        // Normaliser les coordonnées de la tête après ajustement
        double normalizedHeadX = (head.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
        double normalizedHeadY = (head.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

        // Vérifier la collision avec la zone de la nourriture
        return normalizedHeadX < normalizedFoodX + f.getSize() &&
                normalizedHeadX + SnakeSegment.SIZE > normalizedFoodX &&
                normalizedHeadY < normalizedFoodY + f.getSize() &&
                normalizedHeadY + SnakeSegment.SIZE > normalizedFoodY;
    }

    private boolean checkSelfCollision(double newX, double newY) {
        // Vérifier la collision avec le propre corps du serpent
        for (int i = 1; i < snake.getSnakeBody().size(); i++) {
            SnakeSegment segment = snake.getSnakeBody().get(i);

            // Comparer les coordonnées de la tête avec celles du segment du corps
            if (newX == segment.getX() && newY == segment.getY()) {
                System.out.println("Collision avec le corps du serpent détectée. newX: " + newX + " newY: " + newY +
                        "   Segment: " + segment.getX() + " " + segment.getY() + "\n");
                return true; // Collision détectée
            }
        }
        return false; // Aucune collision
    }

    // Collision du joueur avec les IA
    private boolean checkCollisionWithIA() {
        // Récupérer la tête du serpent du joueur
        SnakeSegment head = snake.getSnakeBody().get(0);

        // Calculer la différence pour centrer la vue
        double offsetX = gameConfig.getWidth() / 2 - head.getX();
        double offsetY = gameConfig.getHeight() / 2 - head.getY();

        // Normaliser les coordonnées de la tête après ajustement
        double normalizedHeadX = (head.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
        double normalizedHeadY = (head.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

        // Parcourir toutes les IA pour vérifier la collision avec leur corps
        for (SnakeBody ia : snakeIA) {
            for (SnakeSegment body : ia.getSnakeBody()) {

                // Normaliser les coordonnées du corps de l'IA après ajustement
                double normalizedIAHeadX = (body.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
                double normalizedIAHeadY = (body.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

                // Vérifier la collision avec le corps de l'IA
                if (normalizedHeadX < normalizedIAHeadX + SnakeSegment.SIZE &&
                        normalizedHeadX + SnakeSegment.SIZE > normalizedIAHeadX &&
                        normalizedHeadY < normalizedIAHeadY + SnakeSegment.SIZE &&
                        normalizedHeadY + SnakeSegment.SIZE > normalizedIAHeadY) {

                    // Si le joueur possède un bouclier et que sa tête rentre en collision avec une
                    // IA et que l'IA n'a pas de segment faible
                    if (this.snake.getPower() == Power.SHIELD && ia.getPower() != Power.WEAK) {
                        // System.out.println("Collision mais shield joueur");
                        // On retire le shield du joueur
                        removeSnakePower(snake);
                        return false;
                    }
                    // Si l'IA touchée par la tête du joueur a un segment faible
                    else if (ia.getPower() == Power.WEAK) {
                        // System.out.println("Collision mais weak joueur");
                        // On retire la moitié des segments de l'IA
                        applyWeakPower(ia);
                        return false;
                    } else {
                        // Collision détectée
                        // System.out.println("IA mort");
                        return true;
                    }
                }
            }
        }
        // Aucune collision avec les IA
        return false;
    }

    // Pour enlever le pouvoir du serpent en cas de collision ou autre
    private void removeSnakePower(SnakeBody snake) {
        int snakeSize = snake.getSnakeBody().size();
        for (int i = 0; i < snakeSize; ++i) {
            double newX = snake.getSnakeBody().get(i).getX();
            double newY = snake.getSnakeBody().get(i).getY();
            snake.getSnakeBody().set(i, new NormalSegment(newX, newY));
        }
        snake.removePower();
    }

    // Quand un serpent a un segment faible et que un autre serpent rentre en
    // collision, il perd la moitié de ses segments
    private void applyWeakPower(SnakeBody snake) {
        int snakeSize = snake.getSnakeBody().size();
        if (snakeSize > 1) {
            for (int i = 0; i < snakeSize / 2; ++i) {
                snake.getSnakeBody().remove(i);
            }
        }
        removeSnakePower(snake);
    }

    // Collision des IA avec le joueur
    private boolean checkIACollisionWithPlayer() {
        for (SnakeBody ia : snakeIA) {
            // Récupérer la tête de l'IA
            SnakeSegment iaHead = ia.getSnakeBody().get(0);

            double offsetX = gameConfig.getWidth() / 2 - iaHead.getX();
            double offsetY = gameConfig.getHeight() / 2 - iaHead.getY();

            // Normaliser les coordonnées de la tête de l'IA après ajustement
            double normalizedIAHeadX = (iaHead.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
            double normalizedIAHeadY = (iaHead.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

            // Vérifier la collision avec n'importe quelle partie du corps du joueur
            for (int i = 0; i < snake.getSnakeBody().size(); i++) {
                SnakeSegment playerSegment = snake.getSnakeBody().get(i);

                // Normaliser les coordonnées du segment du joueur après ajustement
                double normalizedPlayerX = (playerSegment.getX() + offsetX + gameConfig.getWidth())
                        % gameConfig.getWidth();
                double normalizedPlayerY = (playerSegment.getY() + offsetY + gameConfig.getHeight())
                        % gameConfig.getHeight();

                // Vérifier la collision avec la tête de l'IA
                if (normalizedIAHeadX < normalizedPlayerX + SnakeSegment.SIZE &&
                        normalizedIAHeadX + SnakeSegment.SIZE > normalizedPlayerX &&
                        normalizedIAHeadY < normalizedPlayerY + SnakeSegment.SIZE &&
                        normalizedIAHeadY + SnakeSegment.SIZE > normalizedPlayerY) {
                    System.out.println("Collision de l'IA avec le corps du joueur détectée. " +
                            "IA: " + iaHead.getX() + " " + iaHead.getY() +
                            " Joueur: " + playerSegment.getX() + " " + playerSegment.getY() + "\n");
                    // Si l'IA possède un bouclier et que sa tête rentre en contact avec le joueur
                    if (ia.getPower() == Power.SHIELD && this.snake.getPower() != Power.WEAK) {
                        // System.out.println("Collision mais shield IA");
                        // On retire le bouclier de l'IA
                        removeSnakePower(ia);
                        return false;
                    }
                    // Si par contre le joueur a un segment faible on enlève la moitié du joueur
                    // sans retirer le pouvoir de l'IA
                    if (this.snake.getPower() == Power.WEAK) {
                        // System.out.println("Collision mais weak IA");
                        applyWeakPower(this.snake);
                        return false;
                    } else {
                        // Collision détectée
                        // System.out.println("Joueur mort");
                        // convertIAToFood(ia);
                        return true;
                    }
                }
            }
        }
        return false; // Aucune collision
    }

    // Utilisation dans la méthode convertIAToFood
    private void convertIAToFood(SnakeBody ia) {
        for (SnakeSegment segmentIA : ia.getSnakeBody()) {
            double x = segmentIA.getX();
            double y = segmentIA.getY();

            RandomColorFactory f = new RandomColorFactory();

            foodList.add(new Food(x, y, f.generateColor()));
        }

        // Retirer l'IA de la liste des IA, elle va réapparaître autre part
        snakeIA.remove(ia);
        // Générer une nouvelle IA à un emplacement aléatoire
        generateIA();
    }

    public SnakeBody getSnake() {
        return snake;
    }

    public List<SnakeBody> getIA() {
        return snakeIA;
    }

    public double getSpeed() {
        return GameConfig.getSPEED();
    }

    private void generateFood() {
        double x = Math.random() * gameConfig.getWidth();
        double y = Math.random() * gameConfig.getHeight();

        RandomColorFactory f = new RandomColorFactory();

        // 20% de chance d'avoir un pouvoir
        boolean hasPower = Math.random() < 0.2;

        Food food;
        if (hasPower) {
            // Choix aléatoire du pouvoir
            Power power = getRandomPower();
            food = new Food(x, y, f.generateColor());
            food.setFoodBehavior(power);
        } else {
            food = new Food(x, y, f.generateColor());
        }

        foodList.add(food);
    }

    // Pour choisir un pouvoir aléatoire
    private Power getRandomPower() {
        // Toutes les valeurs de l'énumération FoodPower
        Power[] powers = Power.values();
        // Choix aléatoire d'un pouvoir
        int randomIndex = (int) (Math.random() * powers.length);
        return powers[randomIndex];
    }

    public List<Food> getFoodList() {
        return foodList;
    }

    public boolean PlayerIsTooBig() {
        return snake.getSnakeBody().size() > 200;
    }

    private boolean isCloseToPlayer(SnakeBody ia) {
        SnakeSegment head = snake.getSnakeBody().get(0);

        // On calcule les distances entre IA et le joueur
        double distanceToPlayerX = head.getX() - ia.getSnakeBody().get(0).getX();
        double distanceToPlayerY = head.getY() - ia.getSnakeBody().get(0).getY();

        // Théorème de Pythagore pour avoir la distance
        double sq1 = Math.pow(distanceToPlayerX, 2);

        // On prend en compte l'offset
        double offsetX = gameConfig.getWidth() / 2 - head.getX();
        double offsetY = gameConfig.getHeight() / 2 - head.getY();

        // On met à jour les distances avec l'offset
        double distanceToPlayerWithOffsetX = distanceToPlayerX + offsetX;
        double distanceToPlayerWithOffsetY = distanceToPlayerY + offsetY;

        // Théorème de Pythagore pour la distance avec l'offset
        double sumWithOffset = Math.pow(distanceToPlayerWithOffsetX, 2) + Math.pow(distanceToPlayerWithOffsetY, 2);

        // Si le joueur est à moins de 250 pixels, l'IA va vers le joueur
        return Math.sqrt(sumWithOffset) < 250;
    }

    private void updateIA() {
        for (SnakeBody ia : this.snakeIA) {
            // On vérifie si une IA est proche
            if (isCloseToPlayer(ia)) {
                // Dans ce cas là on applique la stratégie kill
                iaController.moveIaKillStrat(ia);
            }
            // Si le joueur dépasse une certaine taille
            else if (PlayerIsTooBig()) {
                iaController.moveIaKillStrat(ia);
            } else {
                // Sinon on applique la stratégie food
                iaController.moveIaFoodStrat(ia);
            }
            // On applique les collisions avec la nourriture
            growIA(ia.getSnakeBody().get(0).getX(), ia.getSnakeBody().get(0).getY(), ia);
        }
    }

    public double getDirectionX(){return this.directionX;}


    public double getWidth() {
        return gameConfig.getWidth();
    }

    public double getHeight() {
        return gameConfig.getHeight();
    }

    public boolean getgameOver() {
        return this.gameOver;
    }

    public void increaseSpeed() {
        speed = true;
    }

    public void decreaseSpeed() {
        speed = false;
    }

    public boolean isPaused() {
        return !this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void togglePause() {
        setPaused(isPaused());
    }

    public void reset() {
        // Réinitialiser les listes
        foodList.clear();
        snake.getSnakeBody().clear();
        snakeIA.clear();

        // Réinitialiser les états du jeu
        paused = false;
        gameOver = false;
        speed = false;

        // Recréer le serpent initial et les IA
        snake.getSnakeBody().add(new NormalSegment(gameConfig.getWidth() / 2, gameConfig.getHeight() / 2));
        generateIA();
        generateAllFood();

        // Réinitialiser le contrôleur d'IA
        iaController = new SnakeIAController(this);

        // Réinitialiser la direction
        setDirection(1, 0);
    }

    public double getDirectionY() {
        return this.directionY;
    }
}