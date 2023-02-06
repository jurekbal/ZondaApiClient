package pl.balwinski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.balwinski.model.history.TransactionsResponse;
import pl.balwinski.service.ApiKeyService;
import pl.balwinski.service.FileApiKeyService;
import pl.balwinski.service.ZondaService;

import java.io.IOException;

public class GetTransactionsTest {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try {
            TransactionsResponse transactionsResponse = getTransactions(service);
            System.out.printf("Obtained items list size: %d\n", transactionsResponse.getItems().size());
            System.out.println(GSON.toJson(transactionsResponse));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static TransactionsResponse getTransactions(ZondaService service) throws IOException {
        ApiKeyService apiKeyService = new FileApiKeyService();
        String response = service.getTransactions(apiKeyService.getPublicApiKey(), apiKeyService.getPrivateApiKey());

        System.out.println(response); // for debug

        return GSON.fromJson(response, TransactionsResponse.class);
    }
}
