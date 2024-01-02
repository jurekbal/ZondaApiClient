package pl.balwinski.data;

import lombok.Getter;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.model.wallet.Balance;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CurrentDataset {

    INSTANCE;

    List<Balance> balances;
    List<Transaction> transactions;

    CurrentDataset() {
        this.balances = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    void setBalances(List<Balance> pBalances) {
        balances = pBalances;
    }

    void setTransactions(List<Transaction> pTransactions) {
        transactions = pTransactions;
    }
}
