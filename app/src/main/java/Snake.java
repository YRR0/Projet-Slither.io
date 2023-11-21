import java.awt.Color;

public class Snake {

    private int length;
    private boolean isAlive;
    private Color color;

    public Snake(Color color, boolean isAlive, int length){
        this.color = color;
        this.isAlive = isAlive;
        this.length = length;
    }

    public int getLength(){
        return this.length;
    }

    public boolean isAlive(){
        return this.isAlive;
    }

    public Color getColor(){
        return new Color(this.color.getRGB());
    }

    public void sizeIncrease(){
        this.length++;
    }
}
