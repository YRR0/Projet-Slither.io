package fr.uparis.informatique.cpoo5.projet.model;

import java.util.ArrayList;
import java.util.List;

public class SnakeBody {
    private List<SnakeSegment> body;   
    private Power power;
    private boolean hasPower;
    private boolean immunity = false;
    
    public SnakeBody(){
        body = new ArrayList<>();
        power = null;
        hasPower = false;
    }

    public void setPower(Power power){
        this.power = power;
        hasPower = true;
    }

    public void removePower(){
        this.power = null;
        hasPower = false;
    }

    public Power getPower(){
        return this.power;
    }

    public boolean hasPower(){
        return this.hasPower;
    }

    public List<SnakeSegment> getSnakeBody(){
        return this.body;
    }

    public boolean isImmune(){
        return this.immunity;
    }

    public void setImmunity(boolean b){
        this.immunity = b;
    }
}
