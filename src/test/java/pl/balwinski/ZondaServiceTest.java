package pl.balwinski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.balwinski.model.BalanceResponse;
import pl.balwinski.model.TickerResponse;
import pl.balwinski.service.ApiKeyService;
import pl.balwinski.service.ConsoleInputApiKeyService;
import pl.balwinski.service.FileApiKeyService;
import pl.balwinski.service.ZondaService;

import java.io.IOException;

public class ZondaServiceTest {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try {
//            publicApiTest(service);
            getWalletsTest(service);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void publicApiTest(ZondaService service) throws IOException {
        String response = service.getPublicOpenApi();
        System.out.println(response);

        TickerResponse tickerResponse = GSON.fromJson(response, TickerResponse.class);
        // any further computing

        System.out.println(GSON.toJson(tickerResponse));
    }

    private static void getWalletsTest(ZondaService service) throws IOException {
        ApiKeyService apiKeyService = new FileApiKeyService();

        String response = service.getListOfWallets(apiKeyService.getPublicApiKey(), apiKeyService.getPrivateApiKey());
        BalanceResponse balanceResponse = new Gson().fromJson(response, BalanceResponse.class);

        System.out.println(GSON.toJson(balanceResponse));
    }
}
