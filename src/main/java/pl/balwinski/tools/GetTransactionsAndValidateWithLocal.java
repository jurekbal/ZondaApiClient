package pl.balwinski.tools;

import pl.balwinski.model.history.Transaction;
import pl.balwinski.model.history.TransactionsCheckResult;
import pl.balwinski.model.history.TransactionsFinalResult;
import pl.balwinski.model.history.TransactionsQuery;
import pl.balwinski.service.CsvService;
import pl.balwinski.service.TransactionsChecker;
import pl.balwinski.service.ZondaApiService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class GetTransactionsAndValidateWithLocal {

    public static void main(String[] args) throws IOException {
        CsvService csvService = new CsvService();
        ZondaApiService service = new ZondaApiService();

        //load local transactions
        List<Transaction> localTransactions = csvService.loadTransactions("output/transactions.csv");
        System.out.printf("Loaded transactions count: %d\n", localTransactions.size());

        //load transactions from stock API
        List<Transaction> apiTransactions = null;
        try {
            TransactionsQuery transactionsQuery = new TransactionsQuery();
            transactionsQuery.setNextPageCursor("start");
            ZoneId zoneId = ZoneId.systemDefault();
            LocalDateTime dtFrom = LocalDateTime.of(2022,1,1,0,0);
            LocalDateTime dtTo = LocalDateTime.of(2022,12,31,23,59);
            transactionsQuery.setFromTime(String.valueOf(dtFrom.atZone(zoneId).toEpochSecond()*1000));
            transactionsQuery.setToTime(String.valueOf(dtTo.atZone(zoneId).toEpochSecond()*1000));

            TransactionsFinalResult transactions = service.getTransactions(transactionsQuery);
            apiTransactions = transactions.getItems();
            System.out.printf("Obtained API transactions count: %d\n", apiTransactions.size());

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        TransactionsChecker transactionsChecker = new TransactionsChecker();
        TransactionsCheckResult checkResult = transactionsChecker.check(localTransactions, apiTransactions);

        if (checkResult.hasErrors()) {
            System.out.println("Transactions check result has errors");
            checkResult.getErrorMessages().forEach(System.out::println);
        }

    }

}
