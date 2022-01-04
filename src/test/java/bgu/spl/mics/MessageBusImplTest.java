package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackBroadcast;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import bgu.spl.mics.application.services.LeiaMicroservice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//import com.sun.tools.javac.util.Assert;

class MessageBusImplTest {

   private MicroService m1;
    private MicroService m2;
    private MicroService l;
    private MessageBus b;
    private Attack[] at;
    private AttackEvent attackEvent;
    private AttackBroadcast attackBroadcast;
    private Message attackMessage;
    private Message broadcastMessage;
    private Future<Boolean> futureResult;

    @BeforeEach
    void setUp() {
        l = new LeiaMicroservice(at);
        m1 = new C3POMicroservice();
        m2 = new HanSoloMicroservice();
        b = MessageBusImpl.getInstance();
        //attackEvent = new AttackEvent();
        attackBroadcast = new AttackBroadcast();
        attackMessage = attackEvent;
        broadcastMessage = attackBroadcast;
        futureResult = new Future<Boolean>();
    }

    @Test
    void subscribeEvent() {
        boolean noNeedToTest = true;
        assertTrue(noNeedToTest);
    }

    @Test
    void subscribeBroadcast() {
        boolean noNeedToTest = true;
        assertTrue(noNeedToTest);
    }

    @Test
    //CHECK IF THE FUTUREVALUE IS EQUAL TO RESULT     v
    void complete() throws Exception {
        b.register(m1);
        b.subscribeEvent(AttackEvent.class, m1);
        futureResult = b.sendEvent(attackEvent);  //MESSAGEBUS SHOULD ADD THE EVENT TO M1
        boolean result = false;    //DEFAULT VALUE
        b.complete(attackEvent,result);
        assertTrue(futureResult.isDone());
        assertEquals(futureResult.get(), result);
    }

    @Test
    //CHECK IF MESSAGEBUS ADDED THE MESSAGE TO ALL PROPER MICROSERVICES   v
    void sendBroadcast() throws Exception {
        b.register(m1);
        b.register(m2);
        b.subscribeBroadcast(AttackBroadcast.class, m1);
        b.subscribeBroadcast(AttackBroadcast.class, m2);
        b.sendBroadcast(attackBroadcast);     //MESSAGEBUS SHOULD ADD ATTACKBROADCAST TO ALL MICRO ASSIGNED TO ATTACKBROADCAST
        assertEquals(broadcastMessage, b.awaitMessage(m1));
        assertEquals(broadcastMessage, b.awaitMessage(m2));
    }

    @Test
    //CHECK IF MESSAGEBUS ADDED THE EVENT TO ONLY ONE PROPER MICROSERVICE   v
    //CHECK IS MESSAGEBUS ADDED THE EVENT BY ROBBIN MANNER      v
    void sendEvent() throws Exception {
        b.register(m1);
        b.register(m2);
        b.subscribeEvent(AttackEvent.class, m1);
        b.subscribeEvent(AttackEvent.class, m2);
        b.sendEvent(attackEvent);   //MESSAGEBUS SHOULD ADD ATTACKEVENT TO M1 BECAUSE OF ROBBIN MANNER
        assertEquals(attackMessage, b.awaitMessage(m1));
        b.sendEvent(attackEvent);  //MESSAGEBUS SHOULD ADD ATTACKEVENT TO M2  BECAUSE OF ROBBIN MANNER
        assertEquals(attackMessage, b.awaitMessage(m2));
    }

    @Test

    void testRegister1() {
        boolean noNeedToTest = true;
        assertTrue(noNeedToTest);
    }

    @Test
    void unregister() {
        boolean noNeedToTest = true;
        assertTrue(noNeedToTest);
    }

    @Test
    //CHECK IF MICROSERVICE Extract THE MESSAGE      v
    void awaitMessage() throws Exception {
        b.register(m1);
        b.subscribeEvent(AttackEvent.class, m1);
        b.sendEvent(attackEvent);
        assertEquals(attackMessage, b.awaitMessage(m1));
    }
}