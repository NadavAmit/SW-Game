package bgu.spl.mics.application.services;

import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.LinkedList;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {

    //Fields
    private LinkedList<Message> myMessages;

    public C3POMicroservice() {
        super("C3PO");

    }

    @Override
    protected void initialize() {

        try{


            this.subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{

                try {
                    this.terminate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Diary.getInstance().setC3POTerminate();
                Thread.currentThread().interrupt();
            });

            this.subscribeEvent(AttackEvent.class,(AttackEvent)->{

                boolean didGotSupplies = false;
                while(!didGotSupplies){

                    didGotSupplies= Ewoks.getInstance().getSupplies(AttackEvent);

                    if(didGotSupplies){
                        Thread.sleep(AttackEvent.getAttackDetails().getDuration());

                        MessageBusImpl.getInstance().complete(AttackEvent,true);
                        Diary.getInstance().setC3POFinish();
                        Diary.getInstance().incTotalAttacks();
                        Ewoks.getInstance().releaseSupplies(AttackEvent);


                    }


                }

            });
        }
        catch (Exception e){}



    }

    public int getSize(){
        return myMessages.size();
    }
}
