package akkaHW2019S;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Messages that are passed around the actors are usually immutable classes.
 * Think how you go about creating immutable classes:) Make them all static
 * classes inside the Messages class.
 * 
 * This class should have all the immutable messages that you need to pass
 * around actors. You are free to add more classes(Messages) that you think is
 * necessary
 * 
 * @author Akash Nagesh and M. Kokar
 *
 */
public class Messages {
	
	List<String> text;

	// Messages defined here
	private static Messages msg_instance;

	public static Messages getInstance() {

		if (msg_instance == null) {
			msg_instance = new Messages();
		}
		return msg_instance;
	}

	public void intiateText() {
		text.clear();
	}
}