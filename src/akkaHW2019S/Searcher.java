package akkaHW2019S;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import akka.actor.UntypedActor;

/**
 * this actor implements the search for a path that satisfies the project
 * requirements
 *
 * @author M. Kokar
 *
 */
public class Searcher extends UntypedActor {
	
	//private String AgentName;
	private int startCity;
	private int maxLength;
	private int[][] cities;
	private int angetID;
	private boolean finish;
	private List<Integer> path;
	private int totalLength;
	
	public Searcher(int startCity, int maxLength, int[][] cities, int angetID) {
		// TODO
		this.startCity = startCity;
		this.maxLength = maxLength;
		this.cities = cities;
		this.angetID = angetID;
		this.finish = false;
		this.path = new ArrayList<>();
		this.totalLength = 0;
	}

	@Override
	public void onReceive(Object msg) throws Throwable {
		// Code to implement
		//System.out.println("Searcher ID is");
		//System.out.println(this.angetID);
		if(msg instanceof String) {
			String message = (String) msg;
			if(message.equals("GO!")){
				this.solve();
				if(this.finish == true) {
					getSender().tell(this.angetID, this.getSelf());
				}
				else {
					getSender().tell("Fails!", this.getSelf());
				}
			}
		}
		else if(msg instanceof Integer) {
			if((int)msg == this.angetID) {
				System.out.println("#Agent "+this.angetID+"#:   "+"   I won!   "+" The path is "+this.path+". Total Length is " + this.totalLength + "!");
			}
			else {
				System.out.println("#Agent "+this.angetID+"#:   "+"Anget "+ (int)msg+ " won!"+" The path is "+this.path+". Total Length is " + this.totalLength + "!");
			}
		}
	}
	
	//Greedy Algorithm solution
	public List<Integer> solve(){
		Stack stack = new Stack<Integer>();
		int numberOfNodes = cities[1].length - 1;
        int[] visited = new int[numberOfNodes + 1];
        visited[this.startCity] = 1;
        stack.push(1);
        int element, dst = 0, i;
        int min = Integer.MAX_VALUE;
        boolean minFlag = false;
        //System.out.print(this.startCity + "\t");
        this.path.add(this.startCity+1);
 
        while (!stack.isEmpty())
        {
            element = (int)stack.peek();
            i = 0;
            min = Integer.MAX_VALUE;
            while (i <= numberOfNodes )
            {
                if (cities[element][i] > 1 && visited[i] == 0)
                {
                    if (min > cities[element][i])
                    {
                        min = cities[element][i];
                        dst = i;
                        minFlag = true;
                    }
                }
                i++;
            }
            if (minFlag)
            {
                visited[dst] = 1;
                stack.push(dst);    
                this.path.add(dst+1);
                minFlag = false;
                continue;
            }
            stack.pop();
        }
        this.path.add(this.startCity+1);
        
        for(int j = 0; j < this.path.size()-1; j++) {
        	this.totalLength += cities[this.path.get(j)-1][this.path.get(j+1)-1];
        }
        if(this.totalLength <= this.maxLength) {
        	this.finish = true;
        	return this.path;
        }
        else { 
        	return null;
        }
	}

	
	public int getID() {
		return this.angetID;
	}
}
