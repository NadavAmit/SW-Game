package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Attack;

public class AttackEvent implements Event<Boolean> {
    //FIELDS
    private Attack attackDetails;

    //CONSTRUCTOR
    public AttackEvent(Attack attack){
        this.attackDetails = attack;
    }

    public Attack getAttackDetails(){
        return attackDetails;
    }
}
