package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {
    //FIELDS
    private int duration;

    //CONSTRUCTOR
    public BombDestroyerEvent() {
    }
}