package akkaHW2019S;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * This is the main actor and the only actor that is created directly under the
 * {@code ActorSystem} This actor creates 4 child actors {@code Searcher}
 * 
 * @author Akash Nagesh and M. Kokar
 *
 */
public class Solver extends UntypedActor {
	
	private int[][] cities;
    private int agentAmount = 5;
    private int startCity;
    private int maxLength;
    private boolean searchIsFinish = false;
    
	public Solver(int startCity, int maxLength, int[][] cities) {
		this.startCity = startCity;
        this.maxLength = maxLength;
        this.cities = cities;
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		// Code to implement
		//System.out.println(maxLength);

        if (msg instanceof String) {
            String message = (String) msg;
            //generate searchers
            if (message.equals("Start search!")) {
                for (int iagentID = 1; iagentID <= agentAmount; iagentID++) {
                	Props searcherProps = Props.create(Searcher.class, this.startCity, this.maxLength, this.cities, iagentID);
                	ActorRef searcherActor = getContext().actorOf(searcherProps, "Agent" + iagentID);
                }
                for (ActorRef actor : getContext().getChildren()) {
                    actor.tell("GO!", this.getSelf());
                }
            }
            else if(message.equals("Fails!")) {
            	agentAmount--;
            }
            	//System.out.println(agentAmount);
            	if(agentAmount == 0) {
            		System.out.println("Can't find a path that costs less then the max lenght you set, please re-set that value!");
            		//context().system().terminate();
            	} 
        } 
        else if (msg instanceof Integer) {
            if (!searchIsFinish) {
            	//To make sure application is concurrent!
            	this.searchIsFinish = true;
            	Integer agentID = (Integer)msg;
                System.out.println("#############################");
                System.out.println("Please find below the results");
                System.out.println("#############################");
                System.out.println();
                context().stop(getSelf());
                for (ActorRef actor : getContext().getChildren()) {
                    actor.tell(agentID, this.getSelf());
                }
            }
            context().system().terminate();
        }
	}
}
