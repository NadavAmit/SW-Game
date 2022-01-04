package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.FinishDeactivation;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    long _duration;
    public R2D2Microservice(long duration) {

        super("R2D2");
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
                Diary.getInstance().setR2D2Terminate();
                Thread.currentThread().interrupt();

            });
        }
        catch (Exception e){}

        super.subscribeEvent(DeactivationEvent.class,(DeactivationEvent)->{
            Thread.sleep(_duration);


            FinishDeactivation finishDeactivationBroadcast=new FinishDeactivation();

            super.sendBroadcast(finishDeactivationBroadcast);

            Diary.getInstance().setR2D2Deactivate();


        });

    }


}
