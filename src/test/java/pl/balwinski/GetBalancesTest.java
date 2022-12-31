package pl.balwinski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.balwinski.model.wallet.Balance;
import pl.balwinski.model.wallet.BalanceResponse;
import pl.balwinski.service.ApiKeyService;
import pl.balwinski.service.FileApiKeyService;
import pl.balwinski.service.ZondaService;

import java.io.IOException;
import java.util.List;

public class GetBalancesTest {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try {
            BalanceResponse balanceResponse = getWallet(service);
//            System.out.println(GSON.toJson(balanceResponse));
            List<Balance> nonZeroBalances = filterOutZeroBalances(balanceResponse.getBalances());
            System.out.println(GSON.toJson(nonZeroBalances));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static List<Balance> filterOutZeroBalances(List<Balance> balances) {
        return balances.stream()
                .filter(b -> !(b.getAvailableFunds().equals("0E-8") &&
                        b.getTotalFunds().equals("0E-8") &&
                        b.getLockedFunds().equals("0E-8")))
                .toList();
    }

    private static BalanceResponse getWallet(ZondaService service) throws IOException {
        ApiKeyService apiKeyService = new FileApiKeyService();

        String response = service.getListOfWallets(apiKeyService.getPublicApiKey(), apiKeyService.getPrivateApiKey());
        return new Gson().fromJson(response, BalanceResponse.class);
    }
}
