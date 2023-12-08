package fr.uparis.informatique.cpoo5.projet.controller;
import java.util.List;

import fr.uparis.informatique.cpoo5.projet.model.Food;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegment;
import fr.uparis.informatique.cpoo5.projet.model.SnakeSegmentIA;

public class SnakeIAController {
    
    private Game game;

    public SnakeIAController(Game game) {
        this.game = game;
    }

    private Food closestFood(SnakeSegmentIA head) {
        //On prend comme min la valeur max de double pour être sûr
        double distMin = Double.MAX_VALUE;
        Food closest = null;
        for(Food food : game.getFoodList()) {
            //Pour chaque nourriture on calcule la distance de coordonnées
            double distanceToFoodX = head.getX() - food.getX();
            double distanceToFoodY = head.getY() - food.getY();
            //Théorème de Pythagore
            double dist = Math.sqrt((distanceToFoodX * distanceToFoodX) + (distanceToFoodY * distanceToFoodY));
            //Si on trouve une distance plus petite on change 
            if(dist < distMin) {
                distMin = dist;
                closest = food;
            }
        }
        return closest;
    }

    public void moveIaFoodStrat(List<SnakeSegmentIA> ia) {
        SnakeSegmentIA head = ia.get(0);
        //On récupère la nourriture la plus proche        
        Food closestFood = closestFood(head);
        //On calcule leur distance
        double distanceToFoodX = closestFood.getX() - head.getX();
        double distanceToFoodY = closestFood.getY() - head.getY();
        //On calcule l'angle
        double angleToFood = Math.atan2(distanceToFoodY, distanceToFoodX);
        //On met à jour la direction de l'IA
        ia.get(0).setDirection(Math.cos(angleToFood), Math.sin(angleToFood));
        //On calcule la nouvelle position
        double newX = head.getX() + ia.get(0).getDirectionX() * game.getSpeed();
        double newY = head.getY() + ia.get(0).getDirectionY() * game.getSpeed();
        //On met à jour        
        ia.get(0).setX(newX);
        ia.get(0).setY(newY);
    }

    public void moveIaKillStrat(List<SnakeSegmentIA> ia) {
        SnakeSegment head = game.getSnake().get(0);
        //On calcule la différence entre les coordonnées du snake et des IA 
        double distanceToPlayerX = head.getX() - ia.get(0).getX();
        double distanceToPlayerY = head.getY() - ia.get(0).getY();
        //On calcule l'angle des IA par rapport au snake
        double angleToPlayer = Math.atan2(distanceToPlayerY, distanceToPlayerX);
        //On met à jour la direction
        ia.get(0).setDirection(Math.cos(angleToPlayer), Math.sin(angleToPlayer));
        //On calcule les nouvelles coordonnées pour la tête des IA 
        double newX = ia.get(0).getX() + ia.get(0).getDirectionX() * (0.35);
        double newY = ia.get(0).getY() + ia.get(0).getDirectionY() * (0.35);
        //On met à jour les positions
        ia.get(0).setX(newX);
        ia.get(0).setY(newY);
    }
}
