package fr.uparis.informatique.cpoo5.projet.model.segment;

import javafx.scene.paint.Color;

non-sealed public class NormalSegment extends SnakeSegment {
    public NormalSegment(double x, double y) {
        super(x, y, Color.BLUE);
    }

    public NormalSegment(double x, double y, Color a) {
        super(x, y, a);
    }
}
