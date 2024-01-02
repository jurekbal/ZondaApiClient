package pl.balwinski;

import pl.balwinski.data.CurrentDataset;
import pl.balwinski.model.history.Transaction;
import pl.balwinski.model.wallet.Balance;

import java.util.List;

import static pl.balwinski.commons.Commons.filterOutZeroBalances;

public class Application {



    public static void main(String[] args) {
        // Print out welcome greetings (one time at start)
        System.out.println("Welcome to Zonda Exchange transaction archive and tax accounting tool");

        List<Balance> currentBalances = CurrentDataset.INSTANCE.getBalances();
        List<Transaction> currentTransactions = CurrentDataset.INSTANCE.getTransactions();

        // Print out current status and operation result (after each operation)
        System.out.println("Current data set:");
        System.out.printf("Balances (wallet) size: %d, in this %d non zero balances\n",
                currentBalances.size(), filterOutZeroBalances(currentBalances).size());
        System.out.printf("Transactions entries size: %d\n", currentTransactions.size());

        // Print out menu options and prompt for option
        System.out.println("Select option:");

        /*TODO Options to implement:
        1. Load data from storage
        2. Download data from api, verify and integrate
             suboptions and flow:
                a. Balances: download, verify and update after confirmation
                b. Get filter options for transactions (let's KIS, years from-to only)
                c. Transactions: download, verify and update after confirmation
                d. Printout summary after each step.
        3. Save current dataset to storage (simple overwrite for now, keep your copies manually)
        4. Calculate tax related values for selected Year with details printout
        5. Reports end exports (creating summaries, simple analyses and reports with filtering by some selected criteria (at least dates, valor types)
        */


    }
}
