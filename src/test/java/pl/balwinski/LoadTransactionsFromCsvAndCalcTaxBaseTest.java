package pl.balwinski;

import pl.balwinski.model.history.DecodedTransaction;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.service.CsvService;
import pl.balwinski.service.TaxBaseCalculator;

import java.io.IOException;
import java.util.List;

public class LoadTransactionsFromCsvAndCalcTaxBaseTest {

    public static void main(String[] args) {

        CsvService csvService = new CsvService();
        try {
            List<Transaction> transactions = csvService.loadTransactions("output/transactions.csv");

            System.out.println("Read transactions list size: " + transactions.size());

            List<DecodedTransaction> decodedTransactions = transactions.stream()
                    .map(DecodedTransaction::decode)
                    .sorted()
                    .toList();

            TaxBaseCalculator taxBaseCalculator = new TaxBaseCalculator();
            taxBaseCalculator.showTotalValueInPLNOfCryptoPurchaseIn2022(decodedTransactions);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
