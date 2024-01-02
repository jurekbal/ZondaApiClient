package pl.balwinski.commons;

import pl.balwinski.model.wallet.Balance;

import java.util.List;

public class Commons {

    public static List<Balance> filterOutZeroBalances(List<Balance> balances) {
        return balances.stream()
                .filter(b -> !(b.getAvailableFunds().equals("0E-8") &&
                        b.getTotalFunds().equals("0E-8") &&
                        b.getLockedFunds().equals("0E-8")))
                .toList();
    }
}
