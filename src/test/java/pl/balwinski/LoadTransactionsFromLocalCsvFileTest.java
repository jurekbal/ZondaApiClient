package pl.balwinski;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import pl.balwinski.model.history.Transaction;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class LoadTransactionsFromLocalCsvFileTest {

    public static void main(String[] args) {

        try (Reader reader = new FileReader("output/transactions.csv")) {

            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(reader)
                    .withType(Transaction.class)
                    .withOrderedResults(false)
                    .build();
            List<Transaction> transactions = csvToBean.parse();

            System.out.println("Read transactions list size: " + transactions.size());
            transactions.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
