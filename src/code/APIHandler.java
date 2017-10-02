package code;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;

class APIHandler {

    /**
     * Interprets a Json String as a CryptowatchMarketPriceResponse Object by using getJsonFromURL().
     * Returns the market price as a Double.
     *
     * @param url The URL of the API (Cryptowatch)
     * @return Returns the market price as a Double
     * @throws Exception might throw an Exception
     */
    CryptowatchMarketPriceResponse getCryptowatchPrice(URL url) throws Exception {

        String jsonString = getJsonFromURL(url);

        Gson g = new Gson();
        CryptowatchMarketPriceResponse priceResponse = g.fromJson(jsonString, CryptowatchMarketPriceResponse.class);

        return priceResponse;
    }

    EtherscanWalletResponse getEtherscanWalletInfo(URL url) throws Exception {

        String jsonString = getJsonFromURL(url);
        System.out.println(jsonString);

        Gson g = new Gson();
        EtherscanWalletResponse walletResponse = g.fromJson(jsonString, EtherscanWalletResponse.class);

        return walletResponse;
    }

    /**
     * Fetches a Json String from the InputStream created when calling URL.openStream().
     * Uses BufferedReader and InputStreamReader to read and interpret data from the InputStream.
     *
     * @param url
     * @return
     * @throws Exception
     */
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
