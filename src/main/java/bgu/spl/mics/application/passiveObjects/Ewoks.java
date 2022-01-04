package bgu.spl.mics.application.passiveObjects;


import bgu.spl.mics.application.messages.AttackEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    //FIELDS

    private ConcurrentHashMap<Integer,Ewok> ewoks;
    private ConcurrentHashMap<Integer, Boolean> availability;
    private ConcurrentHashMap<Integer, ArrayList<Integer>> combinations;
    private HashMap<Integer, Object> locker1;
    private HashMap<Object, ArrayList<Integer>> locker2;
    private Object lockSuppliesChange;

    //CONSTRUCTOR
    private Ewoks() {
        ewoks = new ConcurrentHashMap();
        locker1 = new HashMap<Integer, Object>();
        locker2 = new HashMap<Object, ArrayList<Integer>>();
        availability = new ConcurrentHashMap<Integer, Boolean>();
        combinations = new ConcurrentHashMap<Integer, ArrayList<Integer>>();
        lockSuppliesChange = new Object();
    }


    private static class EwoksSingletonHandler {
        private static Ewoks instance = new Ewoks();
    }

    public static Ewoks getInstance() {
        return EwoksSingletonHandler.instance;
    }



    public void add(List<Integer> serialsForAttack) {
        boolean containsAlready;
        for (Integer i : serialsForAttack) {
            {   containsAlready=ewoks.containsKey(i);
                if(containsAlready==false){
                    ewoks.put(i,new Ewok(i));
                }
            }
        }
    }

    public boolean getSupplies(AttackEvent attack) throws InterruptedException {

        synchronized (lockSuppliesChange) {
            for (Integer i : attack.getAttackDetails().serials) {
                ewoks.get(i).acquire();
            }
        }
        return true;
    }
    public void releaseSupplies(AttackEvent attack){

            for (Integer i : attack.getAttackDetails().serials) {
                ewoks.get(i).release();
            }


    }


}

