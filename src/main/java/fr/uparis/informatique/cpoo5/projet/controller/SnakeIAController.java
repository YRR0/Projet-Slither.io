package fr.uparis.informatique.cpoo5.projet.controller;

import fr.uparis.informatique.cpoo5.projet.model.element.Food;
import fr.uparis.informatique.cpoo5.projet.model.Game;
import fr.uparis.informatique.cpoo5.projet.model.SnakeBody;
import fr.uparis.informatique.cpoo5.projet.model.segment.SnakeSegment;

public final class SnakeIAController {
    
    private Game game;

    public SnakeIAController(Game game) {
        this.game = game;
    }

    private Food closestFood(SnakeSegment head) {
        // On prend comme min la valeur max de double pour être sûr
        double distMin = Double.MAX_VALUE;
        Food closest = null;
        for (Food food : game.getFoodList()) {
            // Pour chaque nourriture on calcule la distance de coordonnées
            double distanceToFoodX = head.getX() - food.getX();
            double distanceToFoodY = head.getY() - food.getY();
            // Théorème de Pythagore
            double dist = Math.sqrt((distanceToFoodX * distanceToFoodX) + (distanceToFoodY * distanceToFoodY));
            // Si on trouve une distance plus petite on change
            if (dist < distMin) {
                distMin = dist;
                closest = food;
            }
        }
        return closest;
    }

    public void moveIaFoodStrat(SnakeBody ia) {
        SnakeSegment head = ia.getSnakeBody().get(0);
        // On récupère la nourriture la plus proche
        Food closestFood = closestFood(head);
        // On calcule leur distance
        double distanceToFoodX = closestFood.getX() - head.getX();
        double distanceToFoodY = closestFood.getY() - head.getY();
        // On calcule l'angle
        double angleToFood = Math.atan2(distanceToFoodY, distanceToFoodX);
        // On met à jour la direction de l'IA
        ia.getSnakeBody().get(0).setDirection(Math.cos(angleToFood), Math.sin(angleToFood));
        // On calcule la nouvelle position
        double newX = head.getX() + ia.getSnakeBody().get(0).getDirectionX() * game.getSpeed();
        double newY = head.getY() + ia.getSnakeBody().get(0).getDirectionY() * game.getSpeed();
        // On met à jour
        ia.getSnakeBody().get(0).setX(newX);
        ia.getSnakeBody().get(0).setY(newY);
    }

    public void moveIaKillStrat(SnakeBody ia) {
        SnakeSegment head = game.getSnake().getSnakeBody().get(0);
        // On calcule la différence entre les coordonnées du snake et des IA
        double distanceToPlayerX = head.getX() - ia.getSnakeBody().get(0).getX();
        double distanceToPlayerY = head.getY() - ia.getSnakeBody().get(0).getY();
        // On calcule l'angle des IA par rapport au snake
        double angleToPlayer = Math.atan2(distanceToPlayerY, distanceToPlayerX);
        // On met à jour la direction
        ia.getSnakeBody().get(0).setDirection(Math.cos(angleToPlayer), Math.sin(angleToPlayer));
        // On calcule les nouvelles coordonnées pour la tête des IA
        double newX = ia.getSnakeBody().get(0).getX() + ia.getSnakeBody().get(0).getDirectionX() * (0.35);
        double newY = ia.getSnakeBody().get(0).getY() + ia.getSnakeBody().get(0).getDirectionY() * (0.35);
        // On met à jour les positions
        ia.getSnakeBody().get(0).setX(newX);
        ia.getSnakeBody().get(0).setY(newY);
    }
}
