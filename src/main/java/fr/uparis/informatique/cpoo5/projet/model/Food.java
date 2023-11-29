package fr.uparis.informatique.cpoo5.projet.model;

// La classe qui représente un aliment dans le jeu
public class Food {
    // La taille d'un aliment
    public static final double SIZE = 20;

    private double x;  // La coordonnée x de l'aliment
    private double y;  // La coordonnée y de l'aliment

    // Constructeur pour initialiser les coordonnées de l'aliment
    public Food(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Accéder à la coordonnée x de l'aliment
    public double getX() {
        return x;
    }

    // Accéder à la coordonnée y de l'aliment
    public double getY() {
        return y;
    }
}
