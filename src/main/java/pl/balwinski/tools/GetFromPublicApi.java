package pl.balwinski.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.balwinski.model.trading.pub.TickerResponse;
import pl.balwinski.service.ZondaApiService;

import java.io.IOException;

public class GetFromPublicApi {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) {
        ZondaApiService service = new ZondaApiService();

        try {
            publicApiTest(service);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void publicApiTest(ZondaApiService service) throws IOException {
        String response = service.getPublicOpenApi();
        System.out.println(response);

        TickerResponse tickerResponse = GSON.fromJson(response, TickerResponse.class);
        // any further computing

        System.out.println(GSON.toJson(tickerResponse));
    }
}
