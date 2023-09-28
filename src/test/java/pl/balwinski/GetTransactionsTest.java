package pl.balwinski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.balwinski.model.history.TransactionsFinalResult;
import pl.balwinski.service.ZondaService;

import java.io.IOException;

public class GetTransactionsTest {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try {
            TransactionsFinalResult transactions = service.getTransactions();
            System.out.printf("Obtained items list size: %d\n", transactions.getItems().size());
//            System.out.println(GSON.toJson(transactions));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
