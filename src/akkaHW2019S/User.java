package akkaHW2019S;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
//import akkaHW2019S.Messages;
import akka.actor.Props;

/**
 * Main class for your estimation actor system.
 *
 * @author Akash Nagesh and M. Kokar
 *
 */
public class User {
	
	private int[][] cities;
	private int startCity;
	private int maxLength;

	public static void main(String[] args) throws Exception {


		/*
		 * Create the Solver Actor and send it the StartProcessing message. Once you get
		 * back the response, use it to print the result. Remember, there is only one
		 * actor directly under the ActorSystem. Also, do not forget to shutdown the
		 * actorsystem
		 */
		
		ActorSystem system = ActorSystem.create("EstimationSystem");
		User user = new User();
        user.init();
        Props solverProps = Props.create(Solver.class, user.startCity, user.maxLength, user.cities);
        ActorRef solveActor = system.actorOf(solverProps, "solveActor");
        solveActor.tell("Start search!", null);

	}
	
    public void init() {
        boolean initIsFinish = false;
        while (!initIsFinish) {
            Scanner file = new Scanner(System.in);
            System.out.println("Please enter file Name without extension!");
            String filePath = file.nextLine();
            System.out.println("Enter start City index");
            this.startCity = file.nextInt() - 1;
            System.out.println("Enter max length");
            this.maxLength = file.nextInt();
            this.cities = convertToArray(filePath);
            if (this.cities == null) {
                continue;
            }
            initIsFinish = true;
        }
    }
    
    private int[][] convertToArray(String path) {
    	try {
    		//read file
    		String pathName =path + ".txt";
    		System.out.println(pathName);
    		File file = new File(pathName);
            if (! file.exists() ) {
            	System.out.println("File can't be found, please re-input your file!");
            }
            
            //convert file to Array
            BufferedReader br = new BufferedReader(new FileReader(file));
            String str;
            List<int[]> list = new ArrayList<int[]>();
            while ((str = br.readLine()) != null) {
                int s = 0;
                String[] arr = str.split(" ");
                int[] dArr = new int[arr.length];
                for (String ss : arr) {
                    if (ss != null) {
                        dArr[s++] = Integer.parseInt(ss);
                    }
                }
                list.add(dArr);
            }
            int max = 0;
            for (int i = 0; i < list.size(); i++) {
                if (max < list.get(i).length)
                    max = list.get(i).length;
            }
            int[][] array = new int[list.size()][max];
            if(array[0].length != array.length) {
            	//Array is not a square array
            	System.out.println("The length and height of input array are not equal, please check your input!!");
            	return null;
            }
            else if(this.startCity > array.length-1) {
            	//input is beyond the size of array
            	System.out.println("Your input index of start city is larger than array's length, please check your input!");
            	return null;
            }
            else {
	            for (int i = 0; i < array.length; i++) {
	                for (int j = 0; j < list.get(i).length; j++) {
	                    array[i][j] = list.get(i)[j];
	                }
	            }
	            System.out.println();
	            System.out.println("###############");
	            System.out.println("Input Matrix is:");
	            System.out.println("###############");
	            System.out.println();
	            for (int i = 0; i < array.length; i++) {
	                for (int j = 0; j < list.get(i).length; j++) {
	                    System.out.print(array[i][j] + " ");
	                }
	                System.out.println();
	            }
	            System.out.println();

	            return array;
            }
    	}
    	catch  (IOException e)
        {
            System.out.println("File I/O error!");
        }
    	return null;
    }

}
