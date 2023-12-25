package fr.uparis.informatique.cpoo5.projet.model;

import javafx.stage.Screen;
public class GameConfig {
  private static final double SPEED = 0.5;
    private static final double INC_SPEED = 2.0;
    private final int minIA;
    private final int maxIA;
    private final int initialFoodCount;

    public GameConfig(int minIA, int maxIA, int initialFoodCount) {
        this.minIA = minIA;
        this.maxIA = maxIA;
        this.initialFoodCount = initialFoodCount;
    }

    public GameConfig(){
        this(1,1,50);
    }

    /*public void setWidth(double w){WIDTH = w;}
    public void setWIDTH(double h){HEIGHT = h;}
    public double getWidth() {
        return WIDTH;
    }

    public double getHeight() {
        return HEIGHT;
    }*/

    public static double getSPEED() {
        return SPEED;
    }

    public double getIncSpeed(){return INC_SPEED; }

    public int getMinIA() {
        return minIA;
    }

    public int getMaxIA() {
        return maxIA;
    }

    public int getInitialFoodCount() {
        return initialFoodCount;
    }
}