package pl.balwinski.tools;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import pl.balwinski.model.history.DecodedTransaction;
import pl.balwinski.model.history.ReadableTransactionBean;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.service.CsvService;

import java.io.IOException;
import java.util.List;

public class ConvertTransactionsCsv2Csv {

    public static void main(String[] args) {

        int MAX_SAMPLES = 3;

        CsvService csvService = new CsvService();
        try {
            List<Transaction> transactions = csvService.loadTransactions("output/transactions.csv");

            System.out.println("Read transactions list size: " + transactions.size());

            int i = 0;
            for (Transaction transaction : transactions) {
                if (++i > 30) break;
                System.out.println(i + "(rawT): " + transaction);
            }

            List<DecodedTransaction> decodedTransactions = transactions.stream().map(DecodedTransaction::decode).toList();

            i = 0;
            for (DecodedTransaction decodedTransaction : decodedTransactions) {
                if (++i > MAX_SAMPLES) break;
                System.out.println(i + "(decT): " + decodedTransaction);
            }

            List<ReadableTransactionBean> beans = decodedTransactions.stream().map(ReadableTransactionBean::fromDecodedTransaction).toList();

            i = 0;
            for (ReadableTransactionBean bean : beans) {
                if (++i > MAX_SAMPLES) break;
                System.out.println(i + "(bean): " + bean);
            }

            csvService.writeReadableCsv(beans, "output/readableTransactions.csv");

        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }
}
