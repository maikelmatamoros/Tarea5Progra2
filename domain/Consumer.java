package domain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;

public class Consumer extends Character {
    //atributos de clase
    private int imageSelection;
    private boolean flag;
    private SynchronizedBuffer sharedBuffer;
    
    //constructor
    public Consumer(int x, int y, SynchronizedBuffer buff) throws FileNotFoundException {
        super(x, y);
        this.sharedBuffer = buff;
        this.imageSelection= 0;
        this.flag = true;
        setSprite(0, 16);
        
    }

    //método para cargar en el arrayList el conjunto de imágenes que pide cada animación
    public void setSprite(int animation, int imgs) throws FileNotFoundException {
        ArrayList<Image> sprite = super.getSprite();
        sprite.clear();
        switch (animation) {
            case 0:
                for (int i = 0; i < imgs; i++) {
                    sprite.add(new Image(new FileInputStream("src/assets/dx" + i + ".png")));
                }
                break;
            case 1:
                for (int i = 0; i < imgs; i++) {
                    sprite.add(new Image(new FileInputStream("src/assets/dxw" + i + ".png")));
                }
                break;
            case 2:
                for (int i = 0; i < imgs; i++) {
                    sprite.add(new Image(new FileInputStream("src/assets/dxg" + i + ".png")));
                }
                break;
            default:
                for (int i = 0; i < imgs; i++) {
                    sprite.add(new Image(new FileInputStream("src/assets/dxi" + i + ".png")));
                }
                break;
        }
    }
    //método run...
    @Override
    public void run() {
        while (true) {
            
            try {
                if (flag) {
                    setSprite(0, 16);
                    super.setSpeed((int) (Math.random() * 100) + 10);
                    moveLeftAnimation();
                } else {
                    grabAnimation();
                    setSprite(1, 17);
                    super.setSpeed((int) (Math.random() * 100) + 10);
                    moveRightAnimation();
                }//else
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch
        }//while(true)
        
    }//run
    
    //método que genera la animación de movimiento hacia la izquierda y si se entregaron 5 productos activa la animación
    //final
    private void moveLeftAnimation() {
        imageSelection = 0;
        try {
            while (flag) {
                x -= 10;
                imageSelection++;
                
                if (imageSelection > 15) {
                    imageSelection = 0;
                }//if (imageSelection > 15)
                
                super.setImage(super.getSprite().get(imageSelection));
                Thread.sleep(super.getSpeed());
                
                if (x == 600 && sharedBuffer.isFinish()) {
                    idleAnimation();
                }//if (x == 600 && sharedBuffer.isFinish())
                
                if (x == 400) {
                    flag = false;
                }//if (x == 400)        
            }//while(flag
        } catch (InterruptedException ex) {
            Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
        }//catch

    }//moveLeftAnimation

    //método que genera la animación de movimiento hacia la derecha
    private void moveRightAnimation() {
        while (!flag) {
            x += 10;
            imageSelection++;
            
            if (imageSelection > 16) {
                imageSelection = 0;
            }//if (imageSelection > 16)
            
            super.setImage(super.getSprite().get(imageSelection));
            try {
                Thread.sleep(super.getSpeed());
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch
            
            if (x == 720) {
                flag = true;
                sharedBuffer.setDeliveries();
            }//if (x == 720)

        }//while(!flag)
    }//moveRigthAnimation

    //método que genera la animación final
    private void idleAnimation() {
        imageSelection = 0;
        while (true) {
            try {
                setSprite(3, 11);
                
                if (imageSelection == 10) {
                    imageSelection = 0;
                }//if (imageSelection == 10)
                
                super.setImage(super.getSprite().get(imageSelection));
                Thread.sleep(200);
                imageSelection++;
                
            } catch (FileNotFoundException | InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch
        }//while(true)
    }//idleAnimation
    
    //método que genera la animación donde levanta el barril
    public void grabAnimation() throws FileNotFoundException {
        int animacion = 0;
        setSprite(2, 14);
        this.sharedBuffer.get();
        
        while (animacion < 14) {
            super.setImage(super.getSprite().get(animacion));
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }//catch
            animacion++;
        }//while (animacion < 14)
    
    }//grabAnimation

} // fin de la clase

