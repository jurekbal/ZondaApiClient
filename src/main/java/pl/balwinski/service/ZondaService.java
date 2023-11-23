package pl.balwinski.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import pl.balwinski.model.history.TransactionsFinalResult;
import pl.balwinski.model.history.TransactionsQuery;
import pl.balwinski.model.history.TransactionsResponse;
import pl.balwinski.model.wallet.BalanceResponse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Objects;
import java.util.UUID;

public class ZondaService {

    public static final String TRANSACTIONS_API_BASE_URL = "https://api.zonda.exchange/rest/trading/history/transactions";
    public static final String BALANCES_API_BASE_URL = "https://api.zonda.exchange/rest/balances/BITBAY/balance";
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final ApiKeyService apiKeyService = new FileApiKeyService();


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

    public BalanceResponse getBalanceResponse() throws IOException {

        String publicApiKey = apiKeyService.getPublicApiKey();
        String privateApiKey = apiKeyService.getPrivateApiKey();

        String operationId = UUID.randomUUID().toString();
        System.out.println("Request Operation-Id: " + operationId);
        long requestTimestamp = System.currentTimeMillis() / 1000L;
        System.out.println("Current timestamp: " + requestTimestamp);

        Request request = new Request.Builder()
                .url(BALANCES_API_BASE_URL)
                .method("GET", null)
                .header("API-Key", publicApiKey)
                .header("API-Hash", APIHashGenerator.generate(publicApiKey + requestTimestamp, privateApiKey))
                .header("operation-id", operationId)
                .header("Request-Timestamp", String.valueOf(requestTimestamp))
                .header("Content-Type", "application/json")
                .build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            String responseBodyString = Objects.requireNonNull(response.body()).string();
            return new Gson().fromJson(responseBodyString, BalanceResponse.class);
        }
    }

    public TransactionsFinalResult getTransactions(TransactionsQuery transactionsQuery) throws IOException {

        String publicApiKey = apiKeyService.getPublicApiKey();
        String privateApiKey = apiKeyService.getPrivateApiKey();

        TransactionsFinalResult result = new TransactionsFinalResult();

        //initial request
        TransactionsResponse transactionsResponse = getTransactionsPage(publicApiKey, privateApiKey, transactionsQuery);
        result.getItems().addAll(transactionsResponse.getItems());
        result.setStatus(transactionsResponse.getStatus());
        result.setErrors(transactionsResponse.getErrors());
        result.setTotalRows(transactionsResponse.getTotalRows());

//        TODO handle errors and prevent infinite requests loop other way
        //temporary cut-off counter set to max 10 requests
        for (int i = 0; i < 10; i++) {
            if (!transactionsResponse.getNextPageCursor().equals(transactionsQuery.getNextPageCursor())) {
                transactionsQuery.setNextPageCursor(transactionsResponse.getNextPageCursor());
                transactionsResponse = getTransactionsPage(publicApiKey, privateApiKey, transactionsQuery);
                result.getItems().addAll(transactionsResponse.getItems());
            } else {
                break;
            }
        }

        //TODO Check TotalRows value of response(s) against final list size
        return result;
    }

    public TransactionsFinalResult getTransactions() throws IOException {
        TransactionsQuery transactionsQuery = new TransactionsQuery();
        transactionsQuery.setNextPageCursor("start");
        return getTransactions(transactionsQuery);
    }

    private TransactionsResponse getTransactionsPage(String publicApiKey, String privateApiKey, TransactionsQuery query) throws IOException {
        String operationId = UUID.randomUUID().toString();
        System.out.println("Request Operation-Id: " + operationId);
        long requestTimestamp = System.currentTimeMillis() / 1000L;
        System.out.println("Current timestamp: " + requestTimestamp);

        //todo null check at parameter
        String queryParamsString = GSON.toJson(query);
        System.out.println("DEBUG: queryParamString: " + queryParamsString);
        String queryUrlEncoded = java.net.URLEncoder.encode(queryParamsString, Charset.defaultCharset());
        System.out.println("DEBUG: queryUrlEncoded: " + queryUrlEncoded);

        Request request = new Request.Builder()
                .url(TRANSACTIONS_API_BASE_URL + "?query=" + queryUrlEncoded)
                .method("GET", null)
                .header("API-Key", publicApiKey)
                .header("API-Hash", APIHashGenerator.generate(publicApiKey + requestTimestamp, privateApiKey))
                .header("operation-id", operationId)
                .header("Request-Timestamp", String.valueOf(requestTimestamp))
//                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .build();
        OkHttpClient client = new OkHttpClient();
        try (Response response = client.newCall(request).execute()) {
            assert response.body() != null;
            return GSON.fromJson(response.body().string(), TransactionsResponse.class);
        }
    }
}
