package pl.balwinski.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class ZondaService {


    public String getPublicOpenApi() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.zonda.exchange/rest/trading/ticker/BTC-PLN")
                .method("GET", null)
                .build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public String getListOfWallets(String publicApiKey, String privateApiKey) throws IOException {
        String operationId = UUID.randomUUID().toString();
        System.out.println("Request Operation-Id: " + operationId);
        long requestTimestamp = System.currentTimeMillis() / 1000L;
        System.out.println("Current timestamp: " + requestTimestamp);

        Request request = new Request.Builder()
                .url("https://api.zonda.exchange/rest/balances/BITBAY/balance")
                .method("GET", null)
                .header("API-Key", publicApiKey)
                .header("API-Hash", APIHashGenerator.generate( publicApiKey+requestTimestamp , privateApiKey))
                .header("operation-id", operationId)
                .header("Request-Timestamp", String.valueOf(requestTimestamp))
                .header("Content-Type" , "application/json")
                .build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public String getTransactions(String publicApiKey, String privateApiKey) throws IOException {
        String operationId = UUID.randomUUID().toString();
        System.out.println("Request Operation-Id: " + operationId);
        long requestTimestamp = System.currentTimeMillis() / 1000L;
        System.out.println("Current timestamp: " + requestTimestamp);

        //TODO handle parameters (pages, list size etc);
//        String queryParams = "%7B%22markets%22%3A%5B%22BTC-PLN%22%5D%7D"; //for debug: markets array
        String queryParams = "%7B%22nextPageCursor%22%3A%22start%22%7D"; //for debug: nextPageCursor="start"
//        String queryParams = "%7B%22nextPageCursor%22%3A%22QW9OeGg4UzloZjRDUHdVME1XUTBOMlUwTkMwMk9UVmxMVEV4WldNdFlURm1ZUzB3TWpReVlXTXhNVEF3TURNL0pGc2lSbFJOTFZWVFJGUWlMQ0l4TmpRd09EWXhOREE1TXpreklpd2lOREZrTkRkbE5EUXROamsxWlMweE1XVmpMV0V4Wm1FdE1ESTBNbUZqTVRFd01EQXpJbDA9%22%7D";
            //for debug: nextPageCursor=["..."]


        Request request = new Request.Builder()
                .url("https://api.zonda.exchange/rest/trading/history/transactions?query=" + queryParams) //todo get transactions in loop using nextPageCursor
                .method("GET", null)
                .header("API-Key", publicApiKey)
                .header("API-Hash", APIHashGenerator.generate( publicApiKey+requestTimestamp , privateApiKey))
                .header("operation-id", operationId)
                .header("Request-Timestamp", String.valueOf(requestTimestamp))
//                .header("accept", "application/json")
                .header("Content-Type" , "application/json")
                .build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }
}
