package domain;

public class SynchronizedBuffer implements Buffer {

    //atributos de clase
    private int occupiedBufferCount = 0;
    private int barrelDelivered = 0;

    //método para setear un cambio en el buffer
    public synchronized void set() {

        //mientras el buffer esté ocupado espera
        while (occupiedBufferCount == 1) {
            if (!isFinish()) {
                try {
                    wait();
                }catch (InterruptedException exception) {
                    exception.printStackTrace();
                }//catch
            } else {
                occupiedBufferCount--;
            }//else
        } //while
        if (!isFinish()) {
            ++occupiedBufferCount;
        }//if (!isFinish())
        notify();

    } //set

    // disminuyé el contador para desocuparlo
    public synchronized void get() {
        //mientras no haya nada, espera
        while (occupiedBufferCount == 0) {

            try {
                wait();
            }catch (InterruptedException exception) {
                exception.printStackTrace();
            }//catch

        } //while
        
        --occupiedBufferCount;

        notify();

    } // get
    
    //retorna true si se dejó un barril en el centro
    public synchronized boolean barrel() {
        return occupiedBufferCount==1;
    }//barrel

    //método que aumenta la cantidad de barriles entregados
    public synchronized void setDeliveries() {
        this.barrelDelivered++;
        if (isFinish()) {
            notify();
        }
    }//setDeliveries
    
    //retorna true si se entregaron suficientes barriles
    public synchronized boolean isFinish() {
        return this.barrelDelivered == 5;
    }//finish

    //retorna la cantidad de barriles entregados para pintarlos
    public synchronized int barrelCont() {
        return this.barrelDelivered;
    }//barrelCont

} // end class SynchronizedBuffer

