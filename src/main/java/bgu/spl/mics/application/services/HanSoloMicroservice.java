package bgu.spl.mics.application.services;


import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice() {
        super("Han");

    }




    @Override
    protected void initialize() {

        try{




            this.subscribeEvent(AttackEvent.class,(AttackEvent)->{

                boolean didGotSupplies = false;
                while(!didGotSupplies){

                    didGotSupplies= Ewoks.getInstance().getSupplies(AttackEvent);

                    if(didGotSupplies){
                        Thread.sleep(AttackEvent.getAttackDetails().getDuration());

                        super.complete(AttackEvent,true);
                        Diary.getInstance().setHanSoloFinish();
                        Diary.getInstance().incTotalAttacks();
                        Ewoks.getInstance().releaseSupplies(AttackEvent);

                    }

                }

            });
            this.subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{

                try {
                    this.terminate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Diary.getInstance().setHanSoloTerminate();
                Thread.currentThread().interrupt();
            });
        }
        catch (Exception e){}




    }




}

