import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.json.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.glassfish.jersey.client.ClientConfig;

public class GameOn {

	static WebTarget target;
	static Client client;
	static int stage = 5;
	static TrustManager[] certs = {
        new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
            }
        }
	};

	GameOn () throws Exception {
		initRAPIConsumerClient();
	}
	
	private static void initRAPIConsumerClient () throws Exception {
		HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
		SSLContext ctx = SSLContext.getInstance("SSL");
		ctx.init(null, certs, new SecureRandom());
		client = ClientBuilder.newBuilder().withConfig(new ClientConfig())
                .hostnameVerifier(hostnameVerifier).sslContext(ctx).build();
	}
	
	public static JSONObject playGame () throws Exception {
		Response response = getResponsebyURI(Constants.CHALLENGE_INPUT_PATH);
		JsonArray jArr = null;
		if (stage <= 4) {
			jArr = response.readEntity(JsonArray.class);
		} else {
			String tmp = response.readEntity(String.class);
			System.out.println(tmp);
			return null;
		}
		
		JSONObject ansObject = null;
		
		switch (stage) {
			case 1: solveStageOne(ansObject, jArr);
			break;
			
			case 2: solveStageTwo(ansObject, jArr);
			break;
			
			case 3: solveStageThree(ansObject, jArr);
			break;
			
			case 4: solveStageFour(ansObject, jArr);
			break;
			
			default: ansObject = new JSONObject();
			break;
			
		}
		
		System.out.println("JSON Created: \n" + ansObject.toString() + "\n\n");
		
		return ansObject;
	}
	
	// This method hits to URI : /challenge, which will give the current status of the player
	public static void getStatus () throws IOException {
		Response response = getResponsebyURI(Constants.URI);
		String formattedJson = jsonBeautify(response.readEntity(String.class));
		System.out.println("Qns: " + formattedJson);
	}
	
	
	private static Response getResponsebyURI (String URI) {
		target = client.target(URI);
		System.out.println("requesting on URI: " + URI + "\n\n");
		Response res = target.request(MediaType.APPLICATION_JSON).header("userId", Constants.userID).get();
		return res;
	}
	
	public static void callPost (JSONObject outputObject) {
		target = client.target(Constants.CHALLENGE_OUTPUT_PATH);
		Response response = target.request(MediaType.APPLICATION_JSON).header("userId", Constants.userID).accept(MediaType.APPLICATION_JSON).post(Entity.entity(outputObject.toString(), MediaType.APPLICATION_JSON));
		System.out.println("response value: " + response.readEntity(String.class));
		Scanner scan = new Scanner(System.in);
		System.out.println("\n\nSolution Accepted??? (y/n)\n");
		if ("y".equalsIgnoreCase(scan.next())) {
			stage++;
		}
	}
	
	private static String jsonBeautify (String str) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		JsonNode tree = objectMapper .readTree(str);
		return objectMapper.writeValueAsString(tree);
	}
	
	private static void solveStageOne (JSONObject ansObject, JsonArray jArr) throws Exception {
		ansObject.put("count", jArr.size());
	}
	
	private static void solveStageTwo (JSONObject ansObject, JsonArray jArr) throws Exception {
		System.out.println("Solving Stage Three!!!....");
		int count = 0;
		for (Object j : jArr) {
			JsonObject tmp = (JsonObject) j;
			String start = tmp.getString("startDate");
			String end = null;
			if (!"null".equalsIgnoreCase(tmp.get("endDate").toString())) {
				end = tmp.getString("endDate");
			}

			//System.out.print(tmp.getInt("price") + " total: " + total + " ");
			System.out.print("s: " + start.toString());
			
			String[] sArr = start.split("\\-");
			String[] eArr = null;
			if (end != null) {
				 System.out.println(" e: " + end.toString());
				 eArr = end.split("\\-");
			} else {
				System.out.println(" e: null");
			}

			Calendar cal = Calendar.getInstance();
			
			//Creating Start Date Object
			cal.set(Calendar.DATE, Integer.parseInt(sArr[2]));
			cal.set(Calendar.MONTH, Integer.parseInt(sArr[1]));
			cal.set(Calendar.YEAR, Integer.parseInt(sArr[0]));
			Date Dstart = cal.getTime();

			//Creating End Date Object
			Date Dend = null;
			if (eArr != null) {
				cal.set(Calendar.DATE, Integer.parseInt(eArr[2]));
				cal.set(Calendar.MONTH, Integer.parseInt(eArr[1]));
				cal.set(Calendar.YEAR, Integer.parseInt(eArr[0]));
				Dend = cal.getTime();
			}
			
			//Creating Today's Date Object
			Date today = new Date();
			
			//Actual Logic for Date comparison
			if (!today.before(Dstart)) {
				if (Dend == null || !today.after(Dend)) {
					count++;
				}
			}
			
			System.out.print("s: " + Dstart.toString() + " t: " + today.toString());
			if (Dend != null) {
				 System.out.println(" e: " + Dend.toString());
			} else {
				System.out.println(" e: null");
			}
			
		}
		ansObject.put("count", count);
	}

	private static void solveStageThree (JSONObject ansObject, JsonArray jArr) throws Exception {
		System.out.println("Solving Stage Three!!!....");
		
		for (Object j : jArr) {
			JsonObject tmp = (JsonObject) j;
			String start = tmp.getString("startDate");
			String end = null;
			if (!"null".equalsIgnoreCase(tmp.get("endDate").toString())) {
				end = tmp.getString("endDate");
			}

			//System.out.print(tmp.getInt("price") + " total: " + total + " ");
			System.out.print("s: " + start.toString());
			
			String[] sArr = start.split("\\-");
			String[] eArr = null;
			if (end != null) {
				 System.out.println(" e: " + end.toString());
				 eArr = end.split("\\-");
			} else {
				System.out.println(" e: null");
			}

			Calendar cal = Calendar.getInstance();
			
			//Creating Start Date Object
			cal.set(Calendar.DATE, Integer.parseInt(sArr[2]));
			cal.set(Calendar.MONTH, Integer.parseInt(sArr[1]));
			cal.set(Calendar.YEAR, Integer.parseInt(sArr[0]));
			Date Dstart = cal.getTime();

			//Creating End Date Object
			Date Dend = null;
			if (eArr != null) {
				cal.set(Calendar.DATE, Integer.parseInt(eArr[2]));
				cal.set(Calendar.MONTH, Integer.parseInt(eArr[1]));
				cal.set(Calendar.YEAR, Integer.parseInt(eArr[0]));
				Dend = cal.getTime();
			}
			
			//Creating Today's Date Object
			Date today = new Date();
			
			//Actual Logic for Date comparison
			if (!today.before(Dstart)) {
				if (Dend == null || !today.after(Dend)) {
					if (ansObject.has(tmp.getString("category"))) {
						ansObject.put(tmp.getString("category"), String.valueOf(Integer.valueOf(ansObject.getString(tmp.getString("category"))) + 1));
					} else {
						ansObject.put(tmp.getString("category"), String.valueOf(1));
					}
				}
			}
			
			System.out.print("s: " + Dstart.toString() + " t: " + today.toString());
			if (Dend != null) {
				 System.out.println(" e: " + Dend.toString());
			} else {
				System.out.println(" e: null");
			}
		}
	}

	private static void solveStageFour (JSONObject ansObject, JsonArray jArr) throws Exception {
		System.out.println("Solving Stage Four!!!....");
		
		int total = 0;
		for (Object j : jArr) {
			JsonObject tmp = (JsonObject) j;
			String start = tmp.getString("startDate");
			String end = null;
			if (!"null".equalsIgnoreCase(tmp.get("endDate").toString())) {
				end = tmp.getString("endDate");
			}

			//System.out.print(tmp.getInt("price") + " total: " + total + " ");
			System.out.print("s: " + start.toString());
			
			String[] sArr = start.split("\\-");
			String[] eArr = null;
			if (end != null) {
				 System.out.println(" e: " + end.toString());
				 eArr = end.split("\\-");
			} else {
				System.out.println(" e: null");
			}

			Calendar cal = Calendar.getInstance();
			
			//Creating Start Date Object
			cal.set(Calendar.DATE, Integer.parseInt(sArr[2]));
			cal.set(Calendar.MONTH, Integer.parseInt(sArr[1]));
			cal.set(Calendar.YEAR, Integer.parseInt(sArr[0]));
			Date Dstart = cal.getTime();

			//Creating End Date Object
			Date Dend = null;
			if (eArr != null) {
				cal.set(Calendar.DATE, Integer.parseInt(eArr[2]));
				cal.set(Calendar.MONTH, Integer.parseInt(eArr[1]));
				cal.set(Calendar.YEAR, Integer.parseInt(eArr[0]));
				Dend = cal.getTime();
			}
			
			//Creating Today's Date Object
			Date today = new Date();
			
			//Actual Logic for Date Comparison
			if (!today.before(Dstart)) {
				if (Dend == null || !today.after(Dend)) {
					total += tmp.getInt("price");
				}
			}
			
			System.out.print(tmp.getInt("price") + " total: " + total + " ");
			System.out.print("s: " + Dstart.toString() + " t: " + today.toString());
			if (Dend != null) {
				 System.out.println(" e: " + Dend.toString());
			} else {
				System.out.println(" e: null");
			}
			
		}
		
		ansObject.put("totalValue", String.valueOf(total));
	}

}
