package fr.uparis.informatique.cpoo5.projet.model;

import javafx.scene.image.Image;
import javafx.stage.Screen;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class SnakeModel {

    private static final int ROWS = 60;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = (int) Screen.getPrimary().getBounds().getWidth() / ROWS;

    private List<Point> snakeBody;
    private Point snakeHead;
    private Image foodImage;
    private int foodX;
    private int foodY;
    private boolean gameOver;
    private int currentDirection;
    private int score;

    public SnakeModel() {
        snakeBody = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        generateFood();
    }

    public List<Point> getSnakeBody() {
        return snakeBody;
    }

    public Point getSnakeHead() {
        return snakeHead;
    }

    public Image getFoodImage() {
        return foodImage;
    }

    public int getFoodX() {
        return foodX;
    }

    public int getFoodY() {
        return foodY;
    }

    public int getRows(){
        return ROWS;
    }
    public boolean isGameOver() {
        return gameOver;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public int getScore() {
        return score;
    }

    public int getSquareSize(){
        return this.SQUARE_SIZE;
    }

    public void setCurrentDirection(int currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void moveRight() {
        snakeHead.x++;
    }

    public void moveLeft() {
        snakeHead.x--;
    }

    public void moveUp() {
        snakeHead.y--;
    }

    public void moveDown() {
        snakeHead.y++;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void increaseScore(int points) {
        score += points;
    }

    public void generateFood() {
        start:
        while (true) {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);

            for (Point snake : snakeBody) {
                if (snake.getX() == foodX && snake.getY() == foodY) {
                    continue start;
                }
            }
            foodImage = new Image(getRandomFoodImagePath());
            break;
        }
    }

    public void eatFood() {
        if (snakeHead.getX() == foodX && snakeHead.getY() == foodY) {
            snakeBody.add(new Point(-1, -1));
            generateFood();
            score += 5;
        }
    }

    private String getRandomFoodImagePath() {
        String[] FOODS_IMAGE = {"/images/ic_orange.png", "/images/ic_apple.png", "/images/ic_cherry.png",
                "/images/ic_berry.png", "/images/ic_coconut_.png", "/images/ic_peach.png", "/images/ic_watermelon.png", "/images/ic_orange.png",
                "/images/ic_pomegranate.png"};
        return FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)];
    }
}
