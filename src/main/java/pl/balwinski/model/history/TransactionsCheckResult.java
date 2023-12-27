package pl.balwinski.model.history;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class TransactionsCheckResult {

    private final List<Transaction> newTransactions;
    private final List<String> errorMessages;

    public TransactionsCheckResult() {
        this.newTransactions = new ArrayList<>();
        this.errorMessages = new ArrayList<>();
    }

    public boolean hasErrors() {
        return !this.errorMessages.isEmpty();
    }

    public void addNewTransaction(Transaction t) {
        this.newTransactions.add(t);
    }

    public void addErrorMessage(String message) {
        this.errorMessages.add(message);
    }
}
