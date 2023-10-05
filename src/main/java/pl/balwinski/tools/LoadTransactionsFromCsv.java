package pl.balwinski.tools;

import pl.balwinski.model.history.Transaction;
import pl.balwinski.service.CsvService;

import java.io.IOException;
import java.util.List;

public class LoadTransactionsFromCsv {

    public static void main(String[] args) {

        CsvService csvService = new CsvService();
        try {
            List<Transaction> transactions = csvService.loadTransactions("output/transactions.csv");

            System.out.println("Read transactions list size: " + transactions.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
