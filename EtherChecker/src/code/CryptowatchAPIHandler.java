package code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

class CryptowatchAPIHandler {
	/**
	 * 
	 * 
	 * @param url The URL of the API
	 * @return Returns the market price as a Double
	 * @throws Exception might throw an Exception
	 * 
	 */

	Double getCryptowatchPrice(URL url) throws Exception {

		String jsonString = getJsonFromURL(url);

		System.out.println(jsonString);

		Gson g = new Gson();
		PriceResponse priceResponse = g.fromJson(jsonString, PriceResponse.class);

		return priceResponse.result.price;
	}

	private String getJsonFromURL(URL url) throws Exception {

		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

		String jsonString = "";
		String appendString;

		while ((appendString = br.readLine()) != null) {
			jsonString = jsonString.concat(appendString);
		}
		br.close();

		return jsonString;

	}

}
