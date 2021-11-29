package pl.balwinski;

import com.google.gson.Gson;
import pl.balwinski.model.TickerResponse;
import pl.balwinski.service.ZondaService;

import java.io.IOException;

public class ZondaServiceTest {
    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try {
            String response = service.getTestApi();
            System.out.println(response);
            TickerResponse responseModel = new Gson().fromJson(response, TickerResponse.class);
            System.out.println(responseModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
