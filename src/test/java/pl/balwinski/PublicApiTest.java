package pl.balwinski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.balwinski.model.trading.pub.TickerResponse;
import pl.balwinski.service.ZondaService;

import java.io.IOException;

public class PublicApiTest {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try {
            publicApiTest(service);
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
}
