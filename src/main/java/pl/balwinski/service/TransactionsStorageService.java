package pl.balwinski.service;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import pl.balwinski.model.history.Transaction;

import java.io.IOException;
import java.util.List;

public interface TransactionsStorageService {

    void writeTransactions(List<Transaction> transactions, String fileName)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException;

    List<Transaction> loadTransactions(String filaName) throws IOException;
}
