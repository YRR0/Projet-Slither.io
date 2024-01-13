package fr.uparis.informatique.cpoo5.projet.model.segment;

import fr.uparis.informatique.cpoo5.projet.model.segment.SnakeSegment;
import javafx.scene.paint.Color;

non-sealed public class WeakSegment extends SnakeSegment {

    public WeakSegment(double x, double y) {
        super(x,y, Color.GREEN);
    }    
}
