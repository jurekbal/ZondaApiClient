package pl.balwinski.tools;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import pl.balwinski.model.wallet.Balance;
import pl.balwinski.model.wallet.BalanceResponse;
import pl.balwinski.service.CsvService;
import pl.balwinski.service.ZondaApiService;

import java.io.IOException;
import java.util.List;

import static pl.balwinski.commons.Commons.filterOutZeroBalances;

public class GetBalancesAndWriteCsv {

    public static void main(String[] args) {
        ZondaApiService service = new ZondaApiService();

        try {
            BalanceResponse balanceResponse = service.getBalanceResponse();
            List<Balance> allBalances = balanceResponse.getBalances();

            CsvService csvService = new CsvService();
            csvService.writeBalances(allBalances, "output/balances.csv");

            List<Balance> nonZeroBalances = filterOutZeroBalances(allBalances);
            csvService.writeBalances(nonZeroBalances, "output/nonZeroBalances.csv");

        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

}
