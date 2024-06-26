package pl.balwinski.tools;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import pl.balwinski.model.history.TransactionsFinalResult;
import pl.balwinski.service.CsvService;
import pl.balwinski.service.ZondaApiService;

import java.io.IOException;

public class GetTransactionsAndWriteCsv {

    public static void main(String[] args) {

        try {
            ZondaApiService service = new ZondaApiService();
            TransactionsFinalResult transactionsResponse = service.getTransactions();

            CsvService csvService = new CsvService();
            csvService.writeTransactions(transactionsResponse.getItems(), "output/transactions.csv");

        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

}
