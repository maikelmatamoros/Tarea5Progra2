package tarea;

import domain.Consumer;
import domain.Producer;
import domain.SynchronizedBuffer;
import java.io.FileNotFoundException;
import java.nio.BufferOverflowException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Tarea extends Application implements Runnable {

    private Thread thread;
    private Scene scene;
    private Pane pane;
    private Canvas canvas;
    private SynchronizedBuffer buffer;
    private Consumer consumer;
    private Producer producer;
    private final Image background = new Image("/assets/back.png");
    private final Image barrel = new Image("/assets/barrel.png");

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Graphics and Threads");
        initComponents(primaryStage);
        primaryStage.setOnCloseRequest(exit);
        primaryStage.show();
    }

    @Override
    public void run() {
        long start;
        long elapsed;
        long wait;
        int fps = 60;
        long time = 1000 / fps;

        while (true) {
            try {
                GraphicsContext gc = this.canvas.getGraphicsContext2D();
                start = System.nanoTime();
                elapsed = System.nanoTime() - start;
                wait = time - elapsed / 1000000;
                Thread.sleep(wait);
                draw(gc);
            } catch (InterruptedException ex) {
            }
        }
    }

    private void initComponents(Stage primaryStage) {
        try {
            this.pane = new Pane();
            this.scene = new Scene(this.pane, 800, 700);
            this.canvas = new Canvas(800, 700);

            this.pane.getChildren().add(this.canvas);

            primaryStage.setScene(this.scene);
            this.buffer = new SynchronizedBuffer();
            try {
                this.producer = new Producer(0, 455, buffer);
                this.consumer = new Consumer(730, 528, buffer);

            } catch (FileNotFoundException ex) {
                Logger.getLogger(Tarea.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.consumer.start();
            this.producer.start();

            this.thread = new Thread(this);
            this.thread.start();
        } catch (BufferOverflowException ex) {
        }
    }

    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, 800, 700);
        gc.drawImage(background, 0, 0, 800, 700);
        for (int i = 0; i < buffer.barrelCont(); i++) {
            gc.drawImage(barrel, i * 60, 0, 60, 70);
        }//pinta en la parte superior los barriles que se han entregado
        if (buffer.barrel()) {
            gc.drawImage(barrel, 370, 520, 60, 70);
        }//pinta el barril en el centro si es que se puso uno ahí y no lo han recogido
        gc.drawImage(this.consumer.getImage(), this.consumer.getX(), this.consumer.getY(), 70, 80);
        gc.drawImage(this.producer.getImage(), this.producer.getX(), this.producer.getY(), 80, 140);
    }

    EventHandler<WindowEvent> exit = new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
            System.exit(0);
        }
    };

}
