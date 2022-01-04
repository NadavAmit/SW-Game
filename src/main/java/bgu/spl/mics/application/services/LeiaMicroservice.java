package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.ArrayList;
import java.util.List;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
    private Attack[] _attacks;
    private List<AttackEvent> attackEventList = new ArrayList<>();
    private List<Future> attackEventsFutureList = new ArrayList<>();




    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
        _attacks = attacks;


    }

    @Override
    protected void initialize() throws Exception {

        super.subscribeBroadcast(FinishDeactivation.class, (FinishDeactivation)->{

            BombDestroyerEvent event = new BombDestroyerEvent();
            super.sendEvent(event);
        });
        this.subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast)->{

            try {
                this.terminate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Diary.getInstance().setLeiaTerminate();
            Thread.currentThread().interrupt();

        });
        makeAttackEventsList();
        for(AttackEvent event:attackEventList){
            Future future = null;
            try {
                future = super.sendEvent(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            attackEventsFutureList.add(future);
        }




        boolean flag = false;
        while(!flag){
            for(Future attackEventFuture : attackEventsFutureList){

                attackEventFuture.get();

            }
            flag=true;
        }


        DeactivationEvent deactivationEvent = new DeactivationEvent();
        super.sendEvent(deactivationEvent);



    }








    private void makeAttackEventsList(){
        for(Attack attack : _attacks){
            Ewoks.getInstance().add(attack.getSerials());
            AttackEvent newAttackEvent= new AttackEvent(attack);
            attackEventList.add(newAttackEvent);
        }
    }



}

