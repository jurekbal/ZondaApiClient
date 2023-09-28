package pl.balwinski;

import pl.balwinski.model.wallet.Balance;
import pl.balwinski.service.CsvService;

import java.io.IOException;
import java.util.List;

public class LoadBalancesFromCsvTest {

    public static void main(String[] args) {

        CsvService csvService = new CsvService();
        try {
            List<Balance> balances = csvService.loadBalances("output/balances.csv");

            System.out.println("Read balances list size:" + balances.size());
            System.out.println("************************");
            System.out.println(balances);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
