package pl.balwinski.service;

import pl.balwinski.model.wallet.Balance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BalancesChecker {

    /**
     * Cases to verify for each of apiBalances, check against CURRENCY:
     *             - new balance (not found in localBalances) - INFO
     *             - balance that is in local balances is not anymore in apiBalances - INFO
     *             - balance found more than once on list (also on local list) - ERROR
     *             - if found one then:
     *                 verify if ID, BALANCEENGINE, NAME, TYPE, USERID is the same
     *                     but: AVAILABLEFUNDS, LOCKEDFUNDS, TOTALFUNDS can differ
     * @param localBalances - Balances list obtained from local storage/database
     * @param apiBalances - Balances list obtained from API
     * @return true if check is positive and lists can be further processed; false in case of serious mismatch
     */
    public static boolean checkBalances(List<Balance> localBalances, List<Balance> apiBalances) {
        var result = true;

        if (localBalances == null || apiBalances == null) {
            System.out.println("ERROR: one of balance list is null");
            return false;
        }
        System.out.printf("localBalances size: %d; apiBalances size: %d\n", localBalances.size(), apiBalances.size());

        //check for dupes in localBalances
        Set<String> localCurrenciesSet = new HashSet<>();
        for (Balance b : localBalances) {
            if (localCurrenciesSet.contains(b.getCurrency())) {
                System.out.println("ERROR: duplicated currency found in localBalances list: " + b.getCurrency());
                return false;
            }
            localCurrenciesSet.add(b.getCurrency());
        }

        List<Balance> newBalances = new ArrayList<>();
        List<Balance> localOrphanBalances = new ArrayList<>();
        Set<String> apiBalancesCurrenciesSet = new HashSet<>();

        for (Balance apiBal : apiBalances) {

            //check for dupes in apiBalances
            if (apiBalancesCurrenciesSet.contains(apiBal.getCurrency())) {
                System.out.println("ERROR: duplicated currency found in apiBalances list: " + apiBal.getCurrency());
                return false;
            }
            apiBalancesCurrenciesSet.add(apiBal.getCurrency());

            //check how many currencies of api is in local balances
            List<Balance> foundBalances = findBalancesByCurrency(apiBal, localBalances);

            // check if there is no more than one
            if (foundBalances.size() > 1) {
                System.out.printf("ERROR: %d balances with currency %s found locally\n%s\n",
                        foundBalances.size(), apiBal.getCurrency(), foundBalances);
                return false;
            }

            // not found locally
            if (foundBalances.isEmpty()) {
                System.out.printf("INFO: New currency %s found in API list. It was not found locally.\n", apiBal.getCurrency());
                newBalances.add(apiBal);
            }

            // verify if found balance is the same in structural data
            boolean mismatch = false;
            if (foundBalances.size() == 1) {
                var foundBalance = foundBalances.get(0);
                if (!foundBalance.getId().equals(apiBal.getId())) {
                    System.out.println("ERROR: Balance ID mismatch");
                    mismatch = true;
                }
                if (!foundBalance.getUserId().equals(apiBal.getUserId())) {
                    System.out.println("ERROR: Balance USERID mismatch");
                    mismatch = true;
                }
                if (!foundBalance.getType().equals(apiBal.getType())) {
                    System.out.println("ERROR: Balance TYPE mismatch");
                    mismatch = true;
                }
                if (!foundBalance.getBalanceEngine().equals(apiBal.getBalanceEngine())) {
                    System.out.println("ERROR: Balance BALANCEENGINE mismatch");
                    mismatch = true;
                }if (!foundBalance.getName().equals(apiBal.getName())) {
                    System.out.println("ERROR: Balance NAME mismatch");
                    mismatch = true;
                }
                if (mismatch) {
                    printBalances(foundBalance, apiBal);
                    result = false;
                }
            }
        }

        //find orphan balances on local list (not present anymore in API)
        for (Balance b : localBalances) {
            boolean notFound = true;
            for (Balance apiBal : apiBalances) {
                if (b.getCurrency().equals(apiBal.getCurrency())) {
                    notFound = false;
                    break;
                }
            }
            if (notFound) {
                localOrphanBalances.add(b);
            }
        }
        if (localOrphanBalances.size() > 0) {
            System.out.printf("There are balances in local list which are not present on API list:\n%s", localOrphanBalances);
        }

        System.out.println("BALANCES CHECK SUMMARY:");
        System.out.println("New balances on API list: " + newBalances.size());
        System.out.println("Local orphans found: " + localBalances.size());
        System.out.println("Balances check result is " + (result ? "positive" : "negative"));

        return result;
    }

    private static void printBalances(Balance foundBalance, Balance apiBal) {
        System.out.printf("Local balance entry:%s\nAPI Balance entry  :%s", foundBalance, apiBal);
    }

    private static List<Balance> findBalancesByCurrency(Balance apiBalance, List<Balance> localBalances) {
        var found = new ArrayList<Balance>();
        for (Balance b : localBalances) {
            if (b.getCurrency().equals(apiBalance.getCurrency())) {
                found.add(b);
            }
        }
        return found;
    }
}
