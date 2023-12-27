package pl.balwinski.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.Test;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.model.history.TransactionsCheckResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TransactionsCheckerTest {

    @Test
    void checkForNullArguments() {
        //given
        TransactionsChecker transactionsChecker = new TransactionsChecker();

        //when
        TransactionsCheckResult bothNull = transactionsChecker.check(null, null);
        TransactionsCheckResult localNull = transactionsChecker.check(null, new ArrayList<>());
        TransactionsCheckResult apiNull = transactionsChecker.check(new ArrayList<>(), null);

        //then
        assertThat(bothNull.hasErrors()).isTrue();
        assertThat(localNull.hasErrors()).isTrue();
        assertThat(apiNull.hasErrors()).isTrue();

        assertThat(bothNull.getErrorMessages()).contains("ERROR: Transaction list is null");
        assertThat(localNull.getErrorMessages()).contains("ERROR: Transaction list is null");
        assertThat(apiNull.getErrorMessages()).contains("ERROR: Transaction list is null");
    }

    @Test
    void checkSameLists() {
        //given
        List<Transaction> localTransactions = loadTransactionsFromResourcesCsv("transactions-local.csv");
        List<Transaction> apiTransactions = loadTransactionsFromResourcesCsv("transactions-local.csv");

        //when
        TransactionsChecker transactionsChecker = new TransactionsChecker();
        TransactionsCheckResult result = transactionsChecker.check(localTransactions, apiTransactions);

        //then
        assertThat(result.hasErrors()).isFalse();
        assertThat(result.getNewTransactions().size()).isEqualTo(0);
        assertThat(result.getErrorMessages().size()).isEqualTo(0);
    }

    @Test
    void checkNewTransaction() {
        //given
        List<Transaction> localTransactions = loadTransactionsFromResourcesCsv("transactions-local.csv");
        List<Transaction> apiTransactions = loadTransactionsFromResourcesCsv("transactions-api-new.csv");

        //when
        TransactionsChecker transactionsChecker = new TransactionsChecker();
        TransactionsCheckResult result = transactionsChecker.check(localTransactions, apiTransactions);

        //then
        assertThat(result.hasErrors()).isFalse();
        assertThat(result.getNewTransactions().size()).isEqualTo(1);
        assertThat(result.getNewTransactions().get(0).getId()).isEqualTo("e847ed3a-1602-4740-8041-d3e747760051");
        assertThat(result.getErrorMessages().size()).isEqualTo(0);
    }

    @Test
    void testForDataMismatchWithTheSameId() {
        //given
        List<Transaction> localTransactions = loadTransactionsFromResourcesCsv("transactions-local.csv");
        List<Transaction> apiTransactions = loadTransactionsFromResourcesCsv("transactions-api-data-mismatch.csv");

        //when
        TransactionsChecker transactionsChecker = new TransactionsChecker();
        TransactionsCheckResult result = transactionsChecker.check(localTransactions, apiTransactions);

        //then
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getNewTransactions().size()).isEqualTo(0);
        assertThat(result.getErrorMessages().size()).isEqualTo(1);
        assertThat(result.getErrorMessages()).contains(
                "ERROR: Transaction details mismatch in transactions with the same Id: " +
                "Local transaction: Transaction(id=4d20b586-8d7a-4129-91c5-d6d5e55a54a8, market=ETH-USDT," +
                        " time=1622355678901, amount=0.01616161, rate=4218.9999, initializedBy=Buy, wasTaker=false," +
                        " userAction=Sell, offerId=38ba8ab4-e980-4197-bc03-5543278858ca, commissionValue=0.212121)" +
                        " API-Transaction: Transaction(id=4d20b586-8d7a-4129-91c5-d6d5e55a54a8, market=ETH-USDT," +
                        " time=1622355678901, amount=0.10000000, rate=4218.9999, initializedBy=Buy, wasTaker=false," +
                        " userAction=Sell, offerId=38ba8ab4-e980-4197-bc03-5543278858ca, commissionValue=0.212121)");
    }

    @Test
    void testForDuplicatedIds() {
        //given
        List<Transaction> localTransactions = loadTransactionsFromResourcesCsv("transactions-local-same-ids.csv");
        List<Transaction> apiTransactions = loadTransactionsFromResourcesCsv("transactions-api-same-ids.csv");

        //when
        TransactionsChecker transactionsChecker = new TransactionsChecker();
        TransactionsCheckResult result = transactionsChecker.check(localTransactions, apiTransactions);

        //then
        assertThat(result.hasErrors()).isTrue();
        assertThat(result.getNewTransactions().size()).isEqualTo(0);
        assertThat(result.getErrorMessages().size()).isEqualTo(2);

        result.getErrorMessages().forEach(System.out::println);

        assertThat(result.getErrorMessages()).containsExactlyInAnyOrder(
                "ERROR in local transaction list: duplicated id found: 8f4539d7-c9a0-4ad8-982d-382a6bc60861",
                "ERROR in API transaction list: duplicated id found: 4d20b586-8d7a-4129-91c5-d6d5e55a54a8");
    }

    private List<Transaction> loadTransactionsFromResourcesCsv(String fileName) {
        File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
        try (Reader reader = new FileReader(file)) {
            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(reader)
                    .withType(Transaction.class)
                    .withOrderedResults(false)
                    .build();
            return csvToBean.parse();
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
