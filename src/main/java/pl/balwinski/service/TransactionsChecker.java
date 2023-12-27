package pl.balwinski.service;

import pl.balwinski.model.history.Transaction;
import pl.balwinski.model.history.TransactionsCheckResult;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Check local Transactions with API Transactions lists against:
 * - new Transactions from API
 * - transactions with the same ID but different data on both lists
 * - duplicates ids on local or api list
 * Returns: Result object with list of new transactions found in api list, error messages list.
 */
public class TransactionsChecker {

    public TransactionsCheckResult check(Collection<Transaction> localTransactions, Collection<Transaction> apiTransactions) {
        TransactionsCheckResult result = new TransactionsCheckResult();
        if (localTransactions == null || apiTransactions == null) {
            result.addErrorMessage("ERROR: Transaction list is null");
            return result;
        }
        //duplicates
        findDuplicatedIdsInTransactionList(localTransactions, "local", result);
        findDuplicatedIdsInTransactionList(apiTransactions, "API", result);
        if (result.hasErrors()) {
            return result;
        }

        findNewTransactionsById(localTransactions, apiTransactions, result);
        compareTransactionsWithTheSameIds(localTransactions, apiTransactions, result);
        return result;
    }

    private void findDuplicatedIdsInTransactionList(Collection<Transaction> list, String listName,
                                                    TransactionsCheckResult result) {
        Set<String> idsSet = new HashSet<>();
        for (Transaction t : list) {
            if (idsSet.contains(t.getId())) {
                result.addErrorMessage(String.format("ERROR in %s transaction list: duplicated id found: %s",
                        listName, t.getId()));
            }
            idsSet.add(t.getId());
        }
    }

    private void findNewTransactionsById(Collection<Transaction> localTransactions, Collection<Transaction> apiTransactions, TransactionsCheckResult result) {
        for (Transaction apiTransaction : apiTransactions) {
            if (findById(localTransactions, apiTransaction).isEmpty()) {
                result.addNewTransaction(apiTransaction);
            }
        }
    }

    private void compareTransactionsWithTheSameIds(Collection<Transaction> localTransactions, Collection<Transaction> apiTransactions, TransactionsCheckResult result) {
        for (Transaction apiTransaction : apiTransactions) {
            Optional<Transaction> o = findById(localTransactions, apiTransaction);
            if (o.isPresent()) {
                if (!(apiTransaction.equals(o.get()))) {
                    result.addErrorMessage(String.format(
                            "ERROR: Transaction details mismatch in transactions with the same Id: " +
                                    "Local transaction: %s API-Transaction: %s", o.get(), apiTransaction));
                }
            }
        }
    }

    private Optional<Transaction> findById(Collection<Transaction> localList, Transaction apiTransaction) {
        return localList.stream().filter(t -> t.equalsById(apiTransaction)).findFirst();
    }
}
