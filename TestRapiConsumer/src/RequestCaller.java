import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Scanner;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestCaller {
	static final String CHALLENGE_OUTPUT_PATH = "/challenge/output";
	static final String CHALLENGE_INPUT_PATH = "/challenge/input";
	static final String CHALLENGE_PATH = "/challenge";
	
	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException, JSONException, ParseException {
		Scanner scan = new Scanner(System.in);
		GameOn go = new GameOn();
		System.out.println("Enter the value to send the type of request: ");
		System.out.println("1. GET");
		System.out.println("2. POST");
		String additional = "";
		switch (scan.nextInt()) {
			case 1: 
				System.out.println("want to add URI? (y/n)");
				if ("y".equalsIgnoreCase(scan.next())) {
					System.out.println("Enter:");
					additional = scan.next();
				}
				JSONObject jo = go.callGet(additional);
				go.callPost(CHALLENGE_OUTPUT_PATH, jo);
			break;
			case 2: 
				System.out.println("want to add URI? (y/n)");
				if ("y".equalsIgnoreCase(scan.next())) {
					System.out.println("Enter:");
					additional = scan.next();
				}
				JSONObject j = new JSONObject();
				go.callPost(additional,j);
			break;
			
			default:
				System.out.println("Wrong input, calling default GET");
				go.callGet(additional);
				break;
		}
		System.out.println("\n\nRAPI call done, Termnating");
	}
}
