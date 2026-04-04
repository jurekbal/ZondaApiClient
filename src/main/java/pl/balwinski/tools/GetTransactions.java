package pl.balwinski.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import pl.balwinski.model.history.TransactionsFinalResult;
import pl.balwinski.model.history.TransactionsQuery;
import pl.balwinski.service.ZondaApiService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class GetTransactions {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) {
        ZondaApiService service = new ZondaApiService();

        try {
            TransactionsQuery transactionsQuery = new TransactionsQuery();
            transactionsQuery.setNextPageCursor("start");
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime dtFrom = LocalDateTime.of(2023,12,31,23,59);
            LocalDateTime dtTo = LocalDateTime.of(2025,1,1,0,0);
            transactionsQuery.setFromTime(String.valueOf(dtFrom.atZone(zoneId).toEpochSecond()*1000));
            transactionsQuery.setToTime(String.valueOf(dtTo.atZone(zoneId).toEpochSecond()*1000));


            TransactionsFinalResult transactions = service.getTransactions(transactionsQuery);
            System.out.printf("Obtained items list size: %d\n", transactions.getItems().size());
            System.out.println(GSON.toJson(transactions));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
