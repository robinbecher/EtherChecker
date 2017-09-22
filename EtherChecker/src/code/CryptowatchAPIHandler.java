package code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

public class CryptowatchAPIHandler {

	public CryptowatchAPIHandler() throws Exception {

	}

	/**
	 * 
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * 
	 */
	
	public Double getCryptowatchPrice(URL url) throws Exception {

		String jsonString = getJsonFromURL(url);

		System.out.println(jsonString);

		Gson g = new Gson();
		Response response = g.fromJson(jsonString, Response.class);

		return response.result.price;
	}
	
	public String getJsonFromURL(URL url) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		String jsonString = "";
		String appendString = "";

		while ((appendString = br.readLine()) != null) {
			jsonString = jsonString.concat(appendString);
		}
		br.close();
		
		return jsonString;
		
	}

}
