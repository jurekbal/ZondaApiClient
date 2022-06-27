package pl.balwinski;

import com.google.gson.Gson;
import pl.balwinski.model.TickerResponse;
import pl.balwinski.service.ApiKeyService;
import pl.balwinski.service.ConsoleInputApiKeyService;
import pl.balwinski.service.ZondaService;

import java.io.IOException;

public class ZondaServiceTest {
    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try {
            publicApiTest(service);
//            getWalletsTest(service);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void publicApiTest(ZondaService service) throws IOException {
        String response = service.getPublicOpenApi();
        System.out.println(response);
        TickerResponse responseModel = new Gson().fromJson(response, TickerResponse.class);
        System.out.println(responseModel);
    }

    private static void getWalletsTest(ZondaService service) throws IOException {
        ApiKeyService apiKeyService = new ConsoleInputApiKeyService();

        String response = service.getListOfWallets(apiKeyService.getPublicApiKey(), apiKeyService.getPrivateApiKey());
        System.out.println(response);
//        TickerResponse responseModel = new Gson().fromJson(response, TickerResponse.class);
//        System.out.println(responseModel);
    }
}
