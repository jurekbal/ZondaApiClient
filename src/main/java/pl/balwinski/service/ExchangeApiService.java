package pl.balwinski.service;

import pl.balwinski.model.history.TransactionsFinalResult;
import pl.balwinski.model.history.TransactionsQuery;
import pl.balwinski.model.wallet.BalanceResponse;

import java.io.IOException;

public interface ExchangeApiService {

    BalanceResponse getBalanceResponse() throws IOException;

    TransactionsFinalResult getTransactions(TransactionsQuery transactionsQuery) throws IOException;

    TransactionsFinalResult getTransactions() throws IOException;
}
