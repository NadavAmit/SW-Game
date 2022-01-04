package bgu.spl.mics;

import bgu.spl.mics.application.messages.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {

	//FIELDS
	private ConcurrentHashMap<Class<? extends Event> , LinkedList<MicroService>> eventsSubscribersMap;
	private ConcurrentHashMap<MicroService,ArrayList<Class<? extends Event>>> microServiceEventConcurrentHashMap;
	private  ConcurrentHashMap<Class<? extends Broadcast>,LinkedList<MicroService>> broadcastSubscribersMap;
	private ConcurrentHashMap<MicroService,ArrayList<Class<? extends Broadcast>>> microServiceBroadcastConcurrentHashMap;
	private ConcurrentHashMap<Event,Future> events;
	private static MessageBusImpl instance;
	private ConcurrentHashMap<MicroService, BlockingQueue<Message>> microserviceMessages;
	private Object eventLock;
	private Object broadcastLock;
	//==================================================================================================================

	//PRIVATE CONSTRUCTOR
	private MessageBusImpl(){
		eventsSubscribersMap = new ConcurrentHashMap<Class<? extends Event>,LinkedList<MicroService>>();
		microServiceBroadcastConcurrentHashMap = new ConcurrentHashMap<MicroService,ArrayList<Class<? extends Broadcast>>>();
		microServiceEventConcurrentHashMap = new ConcurrentHashMap<MicroService,ArrayList<Class<? extends Event>>>();
		LinkedList<MicroService> list1 = new LinkedList<MicroService>();
		eventsSubscribersMap.put(AttackEvent.class,list1);
		LinkedList<MicroService> list2 = new LinkedList<MicroService>();
		eventsSubscribersMap.put(DeactivationEvent.class,list2);
		LinkedList<MicroService> list3 = new LinkedList<MicroService>();
		eventsSubscribersMap.put(BombDestroyerEvent.class,list3);
		broadcastSubscribersMap = new ConcurrentHashMap<Class<? extends Broadcast>,LinkedList<MicroService>>();
		LinkedList<MicroService> list4 = new LinkedList<MicroService>();
		broadcastSubscribersMap.put(TerminateBroadcast.class, list4);
		LinkedList<MicroService> list5 = new LinkedList<MicroService>();
		broadcastSubscribersMap.put(FinishDeactivation.class, list5);
		events = new ConcurrentHashMap<Event,Future>();
		microserviceMessages = new ConcurrentHashMap<MicroService, BlockingQueue<Message>>();
		eventLock = new Object();
		broadcastLock = new Object();
	}

	//==================================================================================================================
	private static class MessageBusImplSingletonHandler{
		private static MessageBusImpl instance= new MessageBusImpl();
	}
	public static MessageBusImpl getInstance(){
		return MessageBusImplSingletonHandler.instance;
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {

		synchronized (eventLock) {

			if(!eventsSubscribersMap.containsKey(type))
			{eventsSubscribersMap.put(type,new LinkedList<MicroService>());
			}
			if (!eventsSubscribersMap.get(type).contains(m)) {
				eventsSubscribersMap.get(type).add(m);
				microServiceEventConcurrentHashMap.get(m).add(type);

			}
		}
	}

//======================================================================================================================

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		synchronized (broadcastLock){
			if(!broadcastSubscribersMap.containsKey(type)){
				broadcastSubscribersMap.put(type,new LinkedList<MicroService>());}
			if(!broadcastSubscribersMap.get(type).contains(m)) {
				broadcastSubscribersMap.get(type).add(m);
				microServiceBroadcastConcurrentHashMap.get(m).add(type);

			}
		}
	}

//======================================================================================================================

	@Override
	public <T> void complete(Event<T> e, T result) throws Exception {
		events.get(e).resolve(result);
	}

//======================================================================================================================

	@Override
	public void sendBroadcast(Broadcast b) throws InterruptedException {

		if(broadcastSubscribersMap.get(b.getClass()).isEmpty()){
			Thread.currentThread().sleep(1000);
		}
		synchronized (broadcastLock){
			for(MicroService microService: broadcastSubscribersMap.get(b.getClass())){
				microserviceMessages.get(microService).add(b);
			}

		}
	}

//======================================================================================================================

	@Override
	public <T> Future<T> sendEvent(Event<T> e) throws InterruptedException {

		while(eventsSubscribersMap.get(e.getClass()).isEmpty()){
			Thread.currentThread().sleep(1000);
		}
		synchronized (eventLock){
			LinkedList<MicroService> oldList = eventsSubscribersMap.get(e.getClass());

			MicroService responsible = oldList.removeFirst();
			oldList.addLast(responsible);
			eventsSubscribersMap.put(e.getClass(),oldList);
			microserviceMessages.get(responsible).add(e);

			Future<T> future = new Future<T>();
			events.put(e,future);
			return future;
		}
	}

//======================================================================================================================

	@Override
	public void register(MicroService m){
		BlockingQueue<Message> list = new LinkedBlockingQueue<Message>();
		microserviceMessages.put(m,list);
		microServiceEventConcurrentHashMap.put(m,new ArrayList<Class<? extends Event>>());
		microServiceBroadcastConcurrentHashMap.put(m,new ArrayList<Class<? extends Broadcast>>());
	}

//======================================================================================================================

	@Override
	public void unregister(MicroService m){
		synchronized (broadcastLock) {
			microserviceMessages.remove(m);
			for (Object obj : microServiceEventConcurrentHashMap.get(m)) {
				eventsSubscribersMap.get(obj).remove(m);
			}
			for (Object obj : microServiceBroadcastConcurrentHashMap.get(m)) {
				broadcastSubscribersMap.get(obj).remove(m);
			}
		}
	}

//======================================================================================================================

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		Message message  = microserviceMessages.get(m).take();
		return message;
	}
}
//======================================================================================================================

