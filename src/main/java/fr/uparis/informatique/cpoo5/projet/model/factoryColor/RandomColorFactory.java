package fr.uparis.informatique.cpoo5.projet.model.factoryColor;

import javafx.scene.paint.Color;
import java.util.Random;

// Implémentation concrète de la Factory
public final class RandomColorFactory implements ColorFactory {
    private Color randomColor;
    public RandomColorFactory() {
        generateRandomColor();
    }
    private void generateRandomColor() {
        Random random = new Random();
        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();
        this.randomColor = new Color(red, green, blue, 1.0);
    }

    @Override
    public Color generateColor() {
        return randomColor;
    }
}