package fr.uparis.informatique.cpoo5.projet.model;

import fr.uparis.informatique.cpoo5.projet.model.element.Food;
import fr.uparis.informatique.cpoo5.projet.model.factoryColor.RandomColorFactory;
import fr.uparis.informatique.cpoo5.projet.model.segment.NormalSegment;
import fr.uparis.informatique.cpoo5.projet.model.segment.SnakeSegment;
import javafx.scene.paint.Color;

public final class MultiplayerGame extends Game {
    private boolean gameOver2;
    private SnakeBody snakePlayer2;
    private double directionXPlayer2 = -1;
    private double directionYPlayer2 = 0;

    public MultiplayerGame() {
        super();
        // Initialiser le deuxième serpent
        snakePlayer2 = new SnakeBody();
        snakePlayer2.getSnakeBody()
                .add(new NormalSegment(gameConfig.getWidth() / 2 + 3, gameConfig.getHeight() / 2, Color.BLUE));
    }

    public void setDirectionPlayer2(double directionX, double directionY) {
        this.directionXPlayer2 = directionX;
        this.directionYPlayer2 = directionY;
    }

    @Override
    public void update() {
        immunity(snakePlayer2);
        immunity(super.getSnake());
        // Mise à jour du premier serpent
        if (!gameOver) {
            SnakeSegment head = super.getSnake().getSnakeBody().get(0);

            double newX = head.getX()
                    + super.getDirectionX() * (speed ? gameConfig.getIncSpeed() : GameConfig.getSPEED());
            double newY = head.getY()
                    + super.getDirectionY() * (speed ? gameConfig.getIncSpeed() : GameConfig.getSPEED());

            if (checkSelfCollision(super.getSnake(), newX, newY) || p1CollisionWithP2()) {
                System.out.println("Collision1");
                convertPlayerToFood(super.getSnake());
                gameOver = true;
            } else {
                grow(newX, newY, super.getSnake());
            }
        }

        // Mise à jour du deuxième serpent
        if (!gameOver2) {
            SnakeSegment headPlayer2 = snakePlayer2.getSnakeBody().get(0);

            double newXPlayer2 = headPlayer2.getX()
                    + directionXPlayer2 * (speed ? gameConfig.getIncSpeed() : GameConfig.getSPEED());
            double newYPlayer2 = headPlayer2.getY()
                    + directionYPlayer2 * (speed ? gameConfig.getIncSpeed() : GameConfig.getSPEED());

            // Vérifier la collision avec le corps du serpent
            if (checkSelfCollision(snakePlayer2, newXPlayer2, newYPlayer2)) {
                System.out.println("Self collision");
                convertPlayerToFood(snakePlayer2);
                gameOver2 = true;
            } else if (p2CollisionWithP1()) {
                System.out.println("Collision serpent");
                convertPlayerToFood(snakePlayer2);
                gameOver2 = true;
            } else {
                // Faire croître le deuxième serpent et mettre à jour
                grow(newXPlayer2, newYPlayer2, snakePlayer2);
            }
        }
    }

    @Override
    public SnakeBody getSnake() {
        return super.getSnake();
    }

    public SnakeBody getSnakePlayer2() {
        return snakePlayer2;
    }

    private boolean p1CollisionWithP2() {
        // Récupérer la tête du serpent du joueur
        SnakeSegment head = super.getSnake().getSnakeBody().get(0);

        // Calculer la différence pour centrer la vue
        double offsetX = gameConfig.getWidth() / 2 - head.getX();
        double offsetY = gameConfig.getHeight() / 2 - head.getY();

        // Normaliser les coordonnées de la tête après ajustement
        double normalizedHeadX = (head.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
        double normalizedHeadY = (head.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

        // Parcourir le joueur2 pour vérifier la collision avec son corps
        for (SnakeSegment body : snakePlayer2.getSnakeBody()) {

            // Normaliser les coordonnées du corps du joueur2 après ajustement
            double normalizedP2HeadX = (body.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
            double normalizedP2HeadY = (body.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

            // Vérifier la collision avec le corps du joueur2
            if (normalizedHeadX < normalizedP2HeadX + SnakeSegment.SIZE &&
                    normalizedHeadX + SnakeSegment.SIZE > normalizedP2HeadX &&
                    normalizedHeadY < normalizedP2HeadY + SnakeSegment.SIZE &&
                    normalizedHeadY + SnakeSegment.SIZE > normalizedP2HeadY) {

                if (super.getSnake().isImmune()) {
                    // Temps d'immunité
                    return false;
                } else {
                    // Collision détectée
                    // System.out.println("Joueur1 mort");
                    return true;
                }
            }
        }
        // Aucune collision avec le joueur2
        return false;
    }

    private boolean p2CollisionWithP1() {
        // Récupérer la tête du serpent du joueur2
        SnakeSegment head = snakePlayer2.getSnakeBody().get(0);

        // Calculer la différence pour centrer la vue
        double offsetX = gameConfig.getWidth() / 2 - head.getX();
        double offsetY = gameConfig.getHeight() / 2 - head.getY();

        // Normaliser les coordonnées de la tête après ajustement
        double normalizedHeadX = (head.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
        double normalizedHeadY = (head.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

        // Parcourir le joueur1 pour vérifier la collision avec son corps
        for (SnakeSegment body : super.getSnake().getSnakeBody()) {

            // Normaliser les coordonnées du corps du joueur1 après ajustement
            double normalizedP1HeadX = (body.getX() + offsetX + gameConfig.getWidth()) % gameConfig.getWidth();
            double normalizedP1HeadY = (body.getY() + offsetY + gameConfig.getHeight()) % gameConfig.getHeight();

            // Vérifier la collision avec le corps du joueur1
            if (normalizedHeadX < normalizedP1HeadX + SnakeSegment.SIZE &&
                    normalizedHeadX + SnakeSegment.SIZE > normalizedP1HeadX &&
                    normalizedHeadY < normalizedP1HeadY + SnakeSegment.SIZE &&
                    normalizedHeadY + SnakeSegment.SIZE > normalizedP1HeadY) {

                if (snakePlayer2.isImmune()) {
                    // Temps d'immunité
                    return false;
                } else {
                    // Collision détectée
                    // System.out.println("Joueur2 mort");
                    return true;
                }
            }
        }
        // Aucune collision avec le joueur1
        return false;
    }

    private void convertPlayerToFood(SnakeBody player) {
        if (player.getSnakeBody().size() < 4) {
            double x = player.getSnakeBody().get(0).getX();
            double y = player.getSnakeBody().get(0).getY();
            RandomColorFactory f = new RandomColorFactory();
            super.getFoodList().add(new Food(x, y, f.generateColor()));
        } else {
            for (int i = 0; i < 5; ++i) {
                double x = player.getSnakeBody().get(i).getX();
                double y = player.getSnakeBody().get(i).getY();
                RandomColorFactory f = new RandomColorFactory();
                super.getFoodList().add(new Food(x, y, f.generateColor()));
            }
        }
        // Retirer le joueur
        player.getSnakeBody().clear();
    }

    public boolean getgameOver2() {
        return this.gameOver2;
    }
}
