package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;


/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    long _duration;
    public LandoMicroservice(long duration) {
        super("Lando");
        _duration=duration;
    }

    @Override
    protected void initialize() throws Exception {
        try{


            this.subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{

                try {
                    this.terminate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Diary.getInstance().setLandoTerminate();
                Thread.currentThread().interrupt();
            });
        }
        catch (Exception e){}



        super.subscribeEvent(BombDestroyerEvent.class,(BombDestroyerEvent)->{

            Thread.sleep(_duration);


            super.sendBroadcast(new TerminateBroadcast());

        });

    }

}
