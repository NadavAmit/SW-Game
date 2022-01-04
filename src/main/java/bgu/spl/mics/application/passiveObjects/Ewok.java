package bgu.spl.mics.application.passiveObjects;

import java.util.concurrent.Semaphore;

/**
 * Passive data-object representing a forest creature summoned when HanSolo and C3PO receive AttackEvents.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Ewok {
    int serialNumber;
    boolean available;
    private Semaphore lock;


    public Ewok(int num){
        serialNumber = num;
        available = true;
        lock = new Semaphore(1);
    }

    public int getSerialNumber(){return serialNumber;}
    /**
     * Acquires an Ewok
     */
    public void acquire() {
        try {
            lock.acquire();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        available = false;
    }

    /**
     * release an Ewok
     */
    public void release() {              //command
        available = true;
        lock.release();
    }
    public boolean isAvailable(){
        return available;
    }

}
