import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestCaller {

	
	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(System.in);
		GameOn go = new GameOn();
		System.out.println("Enter the value to send the type of request: ");
		System.out.println("1. Get Current Status of the player!!!");
		System.out.println("2. Play???");
		switch (scan.nextInt()) {
			case 1: 
				go.getStatus();

				System.out.println("\n\nWant to Play??? : (y/n)");
				if ("y".equalsIgnoreCase(scan.next())) {
					JSONObject output = go.playGame();
					if (output != null) {
						go.callPost(output);
					}
				}
			break;
			
			case 2: 
				JSONObject output = go.playGame();
				if (output != null) {
					go.callPost(output);
				}
			break;
			
			default:
				System.out.println("Wrong input, calling default game status");
				go.getStatus();
				break;
		}
		System.out.println("\n\nRAPI call done, Termnating");
	}
}
