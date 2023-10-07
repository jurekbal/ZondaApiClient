package pl.balwinski.service;

import pl.balwinski.model.wallet.Balance;
import pl.balwinski.model.wallet.BalancesCheckResult;

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
     * @return BalancesCheckResult object with status, check messages, newBalances list, orphanBalances list
     */
    public BalancesCheckResult check(List<Balance> localBalances, List<Balance> apiBalances) {

        BalancesCheckResult result = new BalancesCheckResult();

        // check for null arguments
        if (localBalances == null) {
            result.setError("local balance list is null");
            return result;
        }
        if (apiBalances == null) {
            result.setError("api balance list is null");
            return result;
        }

        // check for empty lists argument
        if (localBalances.isEmpty()) {
            result.setWarning("local balance list is empty");
        }
        if (apiBalances.isEmpty()) {
            result.setWarning("api balance list is empty");
        }

        result.setInfoMessage(String.format("localBalances size: %d; apiBalances size: %d\n",
                localBalances.size(), apiBalances.size()));

        //check for dupes in localBalances
        Set<String> localCurrenciesSet = new HashSet<>();
        for (Balance b : localBalances) {
            if (localCurrenciesSet.contains(b.getCurrency())) {
                result.setError("duplicated currency found in localBalances list: " + b.getCurrency());
                return result;
            }
            localCurrenciesSet.add(b.getCurrency());
        }

        List<Balance> newBalances = new ArrayList<>();
        List<Balance> localOrphanBalances = new ArrayList<>();
        Set<String> apiBalancesCurrenciesSet = new HashSet<>();

        for (Balance apiBal : apiBalances) {

            //check for dupes in apiBalances
            if (apiBalancesCurrenciesSet.contains(apiBal.getCurrency())) {
                result.setError("duplicated currency found in apiBalances list: " + apiBal.getCurrency());
                return result;
            }
            apiBalancesCurrenciesSet.add(apiBal.getCurrency());

            //check how many currencies of api is in local balances
            List<Balance> foundBalances = findBalancesByCurrency(apiBal, localBalances);

            // check if there is no more than one
            if (foundBalances.size() > 1) {
                result.setError(String.format("ERROR: %d balances with currency %s found locally\n%s\n",
                        foundBalances.size(), apiBal.getCurrency(), foundBalances));
                return result;
            }

            // not found locally
            if (foundBalances.isEmpty()) {
                result.setInfoMessage(String.format("New currency %s found in API list. It was not found locally.\n",
                        apiBal.getCurrency()));
                newBalances.add(apiBal);
            }

            // verify if found balance is the same in structural data
            if (foundBalances.size() == 1) {
                var foundBalance = foundBalances.get(0);
                if (!foundBalance.getId().equals(apiBal.getId())) {
                    result.setError(
                            String.format("Balance ID mismatch:\nLocal balance entry:%s\nAPI Balance entry  :%s\n",
                                    foundBalance, apiBal));
                }
                if (!foundBalance.getUserId().equals(apiBal.getUserId())) {
                    result.setError(
                            String.format("Balance USERID mismatch:\nLocal balance entry:%s\nAPI Balance entry  :%s\n",
                                    foundBalance, apiBal));
                }
                if (!foundBalance.getType().equals(apiBal.getType())) {
                    result.setError(
                            String.format("Balance TYPE mismatch:\nLocal balance entry:%s\nAPI Balance entry  :%s\n",
                                    foundBalance, apiBal));
                }
                if (!foundBalance.getBalanceEngine().equals(apiBal.getBalanceEngine())) {
                    result.setError(
                            String.format("Balance BALANCEENGINE mismatch:\nLocal balance entry:%s\nAPI Balance entry  :%s\n",
                                    foundBalance, apiBal));

                }if (!foundBalance.getName().equals(apiBal.getName())) {
                    result.setError(
                            String.format("Balance NAME mismatch:\nLocal balance entry:%s\nAPI Balance entry  :%s\n",
                                    foundBalance, apiBal));
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
            result.setWarning(String.format("There are balances in local list which are not present on API list:\n%s",
                    localOrphanBalances));
        }

        result.setInfoMessage(String.format("""
                        BALANCES CHECK SUMMARY:
                        New balances on API list: %d
                        Local orphans found: %d
                        Balances check status is %s
                        """,
                newBalances.size(), localBalances.size(), result.getStatus().name()));

        return result;
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
