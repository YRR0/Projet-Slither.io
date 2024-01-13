package fr.uparis.informatique.cpoo5.projet.model.segment;

import javafx.scene.paint.Color;
// La classe qui représente un segment du serpent dans le jeu
public sealed abstract class SnakeSegment permits ShieldSegment, NormalSegment, WeakSegment{
    // La taille d'un segment du serpent
    public static final double SIZE = 25;
    private Color color;
    private double x;  // La coordonnée x du segment du serpent
    private double y;  // La coordonnée y du segment du serpent
    private double directionX;
    private double directionY;
    
    public SnakeSegment(double x, double y, Color color){
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public SnakeSegment(double x, double y){
        this.x = x;
        this.y = y;
    }

    // Accéder à la coordonnée x du segment
    public double getX(){
        return this.x;
    }

    // Accéder à la coordonnée y du segment
    public double getY(){
        return this.y;
    }

    public Color getColor(){
        return this.color;
    }

    // Définir la coordonnée x du segment
    public void setX(double x){
        this.x = x;
    }

    // Définir la coordonnée y du segment
    public void setY(double y){
        this.y = y;
    }

    public double getDirectionX(){
        return this.directionX;
    }

    public double getDirectionY(){
        return this.directionY;
    }

    public void setDirection(double directionX, double directionY) {
        this.directionX = directionX;
        this.directionY = directionY;
    }
}
