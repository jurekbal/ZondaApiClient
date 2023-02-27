package pl.balwinski;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.model.history.TransactionsFinalResult;
import pl.balwinski.model.history.TransactionsResponse;
import pl.balwinski.service.ApiKeyService;
import pl.balwinski.service.FileApiKeyService;
import pl.balwinski.service.ZondaService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class GetTransactionsAndWriteCsvTest {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try (Writer writer = new FileWriter("output/transactions.csv")){
            //TODO (service): download all transactions using query params (now only default number of 10 transactions are downloaded)
            TransactionsFinalResult transactionsResponse = getTransactions(service);

            StatefulBeanToCsv<Transaction> beanToCsv = new StatefulBeanToCsvBuilder<Transaction>(writer).build();
            //TODO set order for columns and data format (date, quotation mark);
            beanToCsv.write(transactionsResponse.getItems());

//            System.out.println(GSON.toJson(transactionsResponse));

        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

    private static TransactionsFinalResult getTransactions(ZondaService service) throws IOException {
        ApiKeyService apiKeyService = new FileApiKeyService();
        return service.getTransactions(apiKeyService.getPublicApiKey(), apiKeyService.getPrivateApiKey());
    }
}
