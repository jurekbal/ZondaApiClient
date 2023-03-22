package pl.balwinski;

import pl.balwinski.model.history.DecodedTransaction;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.service.CsvService;

import java.io.IOException;
import java.time.ZoneId;
import java.util.List;

public class LoadTransactionsFromLocalCsvFileTest {

    public static void main(String[] args) {

        CsvService csvService = new CsvService();
        try {
            List<Transaction> transactions = csvService.loadTransactions("output/transactions.csv");

            System.out.println("Read transactions list size: " + transactions.size());

            List<DecodedTransaction> decodedTransactions = transactions.stream().map(DecodedTransaction::decode).sorted().toList();
            decodedTransactions.forEach(System.out::println);

            System.out.println("ZoneId: " + ZoneId.systemDefault());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
