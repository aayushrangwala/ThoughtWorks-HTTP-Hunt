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

import org.json.*;

import org.glassfish.jersey.client.ClientConfig;

public class GameOn {

	static WebTarget target;
	static Client client;
	static final String userID = "bwU4gJQR-";
	static final String URI = "https://http-hunt.thoughtworks-labs.net";
	//static final String postURI = "https://jsonplaceholder.typicode.com/todos/5";
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
	GameOn () throws KeyManagementException, NoSuchAlgorithmException {
		HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
		ClientConfig config = new ClientConfig();
		SSLContext ctx = SSLContext.getInstance("SSL");
		
		ctx.init(null, certs, new SecureRandom());
		//config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
		//client = ClientBuilder.newClient();
		client = ClientBuilder.newBuilder().withConfig(config)
                .hostnameVerifier(hostnameVerifier)
                .sslContext(ctx)
.build();
		//target = client.target(URI);
		//"https://httpbin.org/get"
	}
	
	public static JSONObject callGet (String additional) throws JSONException, ParseException {
		String tmpURI = URI + additional;
		target = client.target(tmpURI);
		System.out.println("requesting on URI: " + tmpURI + "\n\n");
		/*System.out.println(
				target.request(MediaType.APPLICATION_JSON).header("userId", userID).get(String.class)
			);*/
		
		Response response = target.request(MediaType.APPLICATION_JSON).header("userId", userID).get();
		//System.out.println("Status of the response: " + response.getStatus());
		System.out.println("Qns: " + response.readEntity(String.class));
		//JsonArray ja = response.readEntity(JsonArray.class);
		//JsonArray ja = new JsonArray(str);
		
		JSONObject jo = new JSONObject();
		int total = 0;
		/*for (Object j : ja) {
			JsonObject tmp = (JsonObject) j;
			String start = tmp.getString("startDate");
			String end = "";
			if ("null".equalsIgnoreCase(tmp.get("endDate").toString())) {
				end = null;
			} else {
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
		
			//DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.US);
			
			//Date Dstart = new Date(dateFormat.parse(start).getTime());
			
			cal.set(Calendar.DATE, Integer.parseInt(sArr[2]));
			cal.set(Calendar.MONTH, Integer.parseInt(sArr[1]));
			cal.set(Calendar.YEAR, Integer.parseInt(sArr[0]));
			Date Dstart = cal.getTime();

			Date Dend = null;
			if (eArr != null) {
			//	Dend = new Date(dateFormat.parse(end).getTime());
				cal.set(Calendar.DATE, Integer.parseInt(eArr[2]));
				cal.set(Calendar.MONTH, Integer.parseInt(eArr[1]));
				cal.set(Calendar.YEAR, Integer.parseInt(eArr[0]));
				Dend = cal.getTime();
			}
			
			Date today = new Date();
			//String t = dateFormat.format(to);
			//Date today = new Date(dateFormat.parse("2019-01-04").getTime());
			//Date today = new Date(dateFormat.parse(t).getTime());
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
			
		}*/
		
		String totalVal = "totalValue";
		jo.put(totalVal,String.valueOf(total));
		System.out.println("JSON Created: " + jo.toString());
		return jo;
	}
	
	public static void callPost (String additional, JSONObject jo) {
		String tmpURI = URI + additional;
		target = client.target(tmpURI);
		Response response = target.request(MediaType.APPLICATION_JSON).header("userId", userID).accept(MediaType.APPLICATION_JSON).post(Entity.entity(jo.toString(), MediaType.APPLICATION_JSON));
		System.out.println("Status of the response: " + response.getStatus());
		System.out.println("response value: " + response.readEntity(String.class));
	}
}
