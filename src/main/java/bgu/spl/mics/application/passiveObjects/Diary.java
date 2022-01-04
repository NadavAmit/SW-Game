package bgu.spl.mics.application.passiveObjects;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {

    private int totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;
    private transient Object lock1= new Object();


    private Diary(){
        totalAttacks=0;
    }

    public void resetNumberAttacks() {
        totalAttacks=0;
    }

    private static class DiarySingletonHolder{
        private static Diary instance = new Diary();
    }
    public static Diary getInstance(){

        return DiarySingletonHolder.instance;
    }

    public  void incTotalAttacks() {
        synchronized(lock1) {
            this.totalAttacks++;
        }
    }

    public void setHanSoloFinish(){

            HanSoloFinish=System.currentTimeMillis();

    }

    public void setC3POFinish(){

            C3POFinish=System.currentTimeMillis();

    }

    public void setR2D2Deactivate(){

            R2D2Deactivate=System.currentTimeMillis();

    }

    public void setLeiaTerminate() {
            LeiaTerminate=System.currentTimeMillis();

    }
    public void setHanSoloTerminate() {

            HanSoloTerminate=System.currentTimeMillis();

    }public void setC3POTerminate() {

            C3POTerminate=System.currentTimeMillis();

    }
    public void setLandoTerminate() {
            LandoTerminate=System.currentTimeMillis();

    }
    public void setR2D2Terminate() {
            R2D2Terminate=System.currentTimeMillis();

    }
    public int getTotalAttacks() {
        synchronized (lock1){
        return totalAttacks;}
    }
    public long getHanSoloFinish(){
        return HanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

}
