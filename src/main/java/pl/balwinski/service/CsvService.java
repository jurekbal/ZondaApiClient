package pl.balwinski.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import pl.balwinski.model.history.ReadableTransactionBean;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.model.wallet.Balance;

import java.io.*;
import java.util.List;

public class CsvService implements TransactionsStorageService, BalancesStorageService {

    @Override
    public void writeTransactions(List<Transaction> transactions, String fileName)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        try (Writer writer = new FileWriter(fileName)) {

            //TODO set order for columns and data format (date, quotation mark);
            StatefulBeanToCsv<Transaction> beanToCsv = new StatefulBeanToCsvBuilder<Transaction>(writer).build();
            beanToCsv.write(transactions);
        }
    }

    @Override
    public List<Transaction> loadTransactions(String filaName) throws IOException {
        try (Reader reader = new FileReader(filaName)) {

            CsvToBean<Transaction> csvToBean = new CsvToBeanBuilder<Transaction>(reader)
                    .withType(Transaction.class)
                    .withOrderedResults(true)
                    .build();
            return csvToBean.parse();

        }
    }

    public void writeBalances(List<Balance> balances, String fileName)
            throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        try (Writer writer = new FileWriter(fileName)) {

            StatefulBeanToCsv<Balance> beanToCsv = new StatefulBeanToCsvBuilder<Balance>(writer).build();
            beanToCsv.write(balances);
        }
    }

    public List<Balance> loadBalances(String filaName) throws IOException {
        try (Reader reader = new FileReader(filaName)) {

            CsvToBean<Balance> csvToBean = new CsvToBeanBuilder<Balance>(reader)
                    .withType(Balance.class)
                    .withOrderedResults(false)
                    .build();
            return csvToBean.parse();

        }
    }

    public void writeReadableCsv(List<ReadableTransactionBean> tos, String fileName) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        try (Writer writer = new FileWriter(fileName)) {

            StatefulBeanToCsv<ReadableTransactionBean> beanToCsv = new StatefulBeanToCsvBuilder<ReadableTransactionBean>(writer)
                    .withSeparator(';')
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withOrderedResults(true)
                    .build();
            beanToCsv.write(tos);
        }
    }

}
