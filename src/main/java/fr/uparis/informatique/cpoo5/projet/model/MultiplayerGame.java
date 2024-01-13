package fr.uparis.informatique.cpoo5.projet.model;

import fr.uparis.informatique.cpoo5.projet.model.segment.NormalSegment;
import fr.uparis.informatique.cpoo5.projet.model.segment.SnakeSegment;
import javafx.scene.paint.Color;

public class MultiplayerGame extends Game {
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
        // Mise à jour du premier serpent
        super.update();

        // Mise à jour du deuxième serpent
        if (!gameOver) {
            SnakeSegment headPlayer2 = snakePlayer2.getSnakeBody().get(0);

            double newXPlayer2 = headPlayer2.getX()
                    + directionXPlayer2 * (speed ? gameConfig.getIncSpeed() : GameConfig.getSPEED());
            double newYPlayer2 = headPlayer2.getY()
                    + directionYPlayer2 * (speed ? gameConfig.getIncSpeed() : GameConfig.getSPEED());

            // Vérifier la collision avec le propre corps du deuxième serpent
            if (checkSelfCollision(newXPlayer2, newYPlayer2) || checkCollisionWithPlayer()) {
                gameOver2 = true;
            } else {
                // Faire croître le deuxième serpent et mettre à jour
                grow(newXPlayer2, newYPlayer2, snakePlayer2);
            }
        }
    }

    private boolean checkCollisionWithPlayer() {
        return false;
    }

    @Override
    public SnakeBody getSnake() {
        return super.getSnake();
    }

    public SnakeBody getSnakePlayer2() {
        return snakePlayer2;
    }
}
