package pl.balwinski;

import pl.balwinski.model.history.DecodedTransaction;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.model.history.UserActionType;
import pl.balwinski.service.CsvService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;

public class LoadTransactionsFromCsvTest {

    public static void main(String[] args) {

        CsvService csvService = new CsvService();
        try {
            List<Transaction> transactions = csvService.loadTransactions("output/transactions.csv");

            System.out.println("Read transactions list size: " + transactions.size());
            System.out.println("ZoneId: " + ZoneId.systemDefault());

            List<DecodedTransaction> decodedTransactions = transactions.stream()
                    .map(DecodedTransaction::decode)
                    .sorted()
                    .toList();

            //temporary hardcoded filter for PLN, BUT action in 2022
            List<DecodedTransaction> buyForPlnIn2022Transactions = decodedTransactions.stream()
                    .filter(t -> t.getTime().getYear() == 2022)
                    .filter(t -> t.getMarket().getRight().equalsIgnoreCase("PLN"))
                    .filter(t -> t.getUserAction().equals(UserActionType.BUY))
                    .toList();

            System.out.println("buyForPlnIn2022Transactions:");
            buyForPlnIn2022Transactions.forEach(t -> {
                System.out.println(t);
                System.out.println("Transaction value: " + t.getAmount().multiply(t.getRate()));
            });

            BigDecimal totalValuePln = buyForPlnIn2022Transactions.stream()
                    .map(t -> t.getAmount().multiply(t.getRate())).reduce(BigDecimal.ZERO, BigDecimal::add);
            System.out.println("TOTAL for filtered (PLN, BUY, 2022): " + totalValuePln);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
