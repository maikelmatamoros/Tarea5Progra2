package domain;

import java.util.ArrayList;
import javafx.scene.image.Image;

public class Character extends Thread{
    //atributos de clase
    protected int x;
    protected int y;
    private Image image;
    private ArrayList<Image> sprite;
    protected int speed;

    //constructor
    public Character(int x, int y) {
        this.x = x;
        this.y = y;
        this.sprite = new ArrayList<>();
    }//Character

    //métodos accesores
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public ArrayList<Image> getSprite() {
        return sprite;
    }

    public void setSprite(ArrayList<Image> sprite) {
        this.sprite = sprite;
    }
    
    public void setSpeed(int speed){
        this.speed=speed;
    }
    public int getSpeed(){
        return this.speed;
    }
}
