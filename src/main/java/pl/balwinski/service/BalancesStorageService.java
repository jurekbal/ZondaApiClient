package pl.balwinski.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import pl.balwinski.model.wallet.Balance;

import java.io.IOException;
import java.util.List;

public interface BalancesStorageService {

    void writeBalances(List<Balance> balances, String fileName)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;

    List<Balance> loadBalances(String filaName) throws IOException;
}
