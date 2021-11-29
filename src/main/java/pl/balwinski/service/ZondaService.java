package pl.balwinski.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

public class ZondaService {


    public String getTestApi() throws IOException {
        Request request = new Request.Builder()
                .url("https://api.zonda.exchange/rest/trading/ticker/BTC-PLN")
                .method("GET", null)
                .build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();
        return Objects.requireNonNull(response.body()).string();
    }
}
