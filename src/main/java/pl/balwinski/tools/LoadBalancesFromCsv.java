package pl.balwinski.tools;

import pl.balwinski.model.wallet.Balance;
import pl.balwinski.service.CsvService;

import java.io.IOException;
import java.util.List;

import static pl.balwinski.tools.GetBalances.*;

public class LoadBalancesFromCsv {

    public static void main(String[] args) {

        CsvService csvService = new CsvService();
        try {
            List<Balance> balances = csvService.loadBalances("output/balances.csv");

            List<Balance> nonZeroBalances = filterOutZeroBalances(balances);
            System.out.println("Total balances: " + balances.size());
            System.out.println("Non zero balances: " + nonZeroBalances.size());

            printOutFiatTickersAndFounds(nonZeroBalances);
            printOutCryptoTickersAndFounds(nonZeroBalances);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
