package domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

public class Producer extends Character {

    //atributos de clase
    private int image;
    private boolean flag;
    private SynchronizedBuffer sharedBuffer;
    private int cont;
    private int danceDirection;

    //constructor
    public Producer(int x, int y, SynchronizedBuffer buff) throws FileNotFoundException {
        super(x, y);
        this.sharedBuffer = buff;
        this.image = this.cont = this.danceDirection = 0;
        this.flag = true;
        setSprite(0, 10);
    }//Producer

    //método para cargar en el arrayList el conjunto de imágenes que pide cada animación
    public void setSprite(int action, int img) throws FileNotFoundException {
        ArrayList<Image> sprite = super.getSprite();
        sprite.clear();
        super.getSprite().clear();
        switch (action) {
            case 0:
                for (int i = 0; i < img; i++) {

                    sprite.add(new Image(new FileInputStream("src/assets/" + i + ".png")));
                }
                break;
            case 1:
                for (int i = 0; i < img; i++) {
                    sprite.add(new Image(new FileInputStream("src/assets/dk" + i + ".png")));
                }
                break;
            default:
                for (int i = 0; i < img; i++) {
                    sprite.add(new Image(new FileInputStream("src/assets/dkd" + i + ".png")));
                }
                break;
        }

    }//setSprite

    //método run
    @Override
    public void run() {

        while (true) {

            try {
                if (flag) {

                    setSprite(0, 10);
                    super.setSpeed((int) (Math.random() * 100) + 10);
                    moveRightAnimation();
                    putBarrel();
                    this.sharedBuffer.set();
                } else {

                    setSprite(1, 6);
                    super.setSpeed((int) (Math.random() * 100) + 10);
                    moveLeftAnimation();
                }//else
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch

        }//while(true)

    }//run

    //metodo que genera la animación de movimiento hacia la izquierda
    private void moveLeftAnimation() {
        this.image = 0;
        this.cont = 0;
        while (!this.flag && !this.sharedBuffer.isFinish()) {
            x -= 10;
            this.image++;

            if (this.image > 5) {
                image = 0;
            }//if (this.image > 5)

            super.setImage(super.getSprite().get(image));
            try {
                Thread.sleep(super.getSpeed());
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch

            if (x == 0) {
                flag = true;
            }//if (x == 0)

        }//while (!this.flag && !this.sharedBuffer.isFinish())

        if (sharedBuffer.isFinish()) {
            danceAnimation();
        }//if (sharedBuffer.isFinish())

    }//moveLeftAnimation

    //método que genera el baile final
    private void danceAnimation() {
        this.image = 0;
        try {
            setSprite(2, 7);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
        }//catch

        while (true) {

            if (this.image == 6) {
                this.danceDirection = 1;
            } else if (this.image == 0) {
                this.danceDirection = 0;
            }//else if(image == 0)
            if (this.danceDirection == 0) {
                this.image++;
            } else {
                this.image--;
            }//else

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch

            super.setImage(super.getSprite().get(image));
        }//while(true)
    }//danceAnimation
    //metodo que genera la animación de movimiento hacia la derecha y si se entregó suficientes barriles, baila

    private void moveRightAnimation() {

        while (flag && !sharedBuffer.isFinish()) {
            x += 10;
            if (x < 310) {
                image++;
                if (image > 9) {
                    image = 6;
                }
                super.setImage(super.getSprite().get(image));
            }

            try {
                Thread.sleep(super.getSpeed());
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch
            if (x == 320) {
                flag = false;
            }//if (x == 320)
        }//while (flag && !sharedBuffer.isFinish())

        if (sharedBuffer.isFinish()) {
            danceAnimation();
        }//if (sharedBuffer.isFinish())

    }//moveRightAnimation

    //método para generar animación de dejar el barril
    private void putBarrel() {
        this.image = 3;
        while (image >= 0) {
            
            try {
                Thread.sleep(100);
                super.setImage(super.getSprite().get(image));
                this.image--;

            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch
        }//while(image>=0)
    }// putBarrel

} // fin de la clase
