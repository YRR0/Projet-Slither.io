package fr.uparis.informatique.cpoo5.projet.model;

// La classe qui représente un segment du serpent dans le jeu
public class SnakeSegment {
    // La taille d'un segment du serpent
    public static final double SIZE = 25;

    private double x;  // La coordonnée x du segment du serpent
    private double y;  // La coordonnée y du segment du serpent

    // Constructeur pour initialiser les coordonnées du segment
    public SnakeSegment(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Accéder à la coordonnée x du segment
    public double getX() {
        return x;
    }

    // Accéder à la coordonnée y du segment
    public double getY() {
        return y;
    }

    // Définir la coordonnée x du segment
    public void setX(double x) {
        this.x = x;
    }

    // Définir la coordonnée y du segment
    public void setY(double y) {
        this.y = y;
    }
}
