package fr.uparis.informatique.cpoo5.projet.model.element;
import javafx.scene.paint.Color;
// La classe qui représente un aliment dans le jeu
public class Food {
    // La taille d'un aliment
    private double size;
    private boolean snakeDead;
    private double x;  // La coordonnée x de l'aliment
    private double y;  // La coordonnée y de l'aliment
    private Power power;

    // une couleur pour la nourriture
    private Color color;

    // Constructeur pour initialiser les coordonnées de l'aliment
    public Food(double x, double y,Color a) {
        int max = 20;
        int min = 10;
        this.color = a;
        int range = max - min + 1;
        this.size = (int)(Math.random() * range) + min; //Taille de nourriture aléatoire
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

    public boolean food_or_deadFood(){return this.snakeDead;}
    public void setDead_Food(boolean a){this.snakeDead = true;}
    public void setX(double x){this.x = x;}
    public void setY(double y){this.y = y;}
    public Color getColor(){ return this.color ;}

    public double getSize(){
        return this.size;
    }

    public void setFoodBehavior(Power power) {
        this.power = power;
    }

    public Power getPower(){
        return this.power;
    }
}
