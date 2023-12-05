package fr.uparis.informatique.cpoo5.projet.model;

public class SnakeSegmentIA extends SnakeSegment {
    private double directionX;
    private double directionY;
    private int countdown;
    
    public SnakeSegmentIA(double x, double y) {
        super(x, y);
        double randAngle = Math.toRadians(randomGenerator(0, 360));
        this.directionX = Math.cos(randAngle);
        this.directionY = Math.sin(randAngle);
        this.countdown = randomGenerator(300, 400);
    }
    
    public double getDirectionY(){
        return directionY;
    }

    public double getDirectionX(){
        return directionX;
    }

    public void setDirection(double directionX, double directionY){
        this.directionX = directionX;
        this.directionY = directionY;
    }

    public int randomGenerator(int min, int max){
        int range = max - min + 1;
        return (int)(Math.random() * range) + min;
    }

    public int getCountdown(){
        return this.countdown;
    }

    public void setCountdown(int c){
        this.countdown = c;
    }

    public void decreaseCountdown(){
        this.countdown --;
    }

    public void resetCountdown(){
        this.countdown = randomGenerator(300, 400);
    }
}