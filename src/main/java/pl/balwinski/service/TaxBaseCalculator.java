package pl.balwinski.service;

import pl.balwinski.model.history.DecodedTransaction;
import pl.balwinski.model.history.UserActionType;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;

public class TaxBaseCalculator {

    /** Calculates and shows BUY transactions and total value
     * Hardcoded filter for PLN, BUY action in year 2022
     * @param decodedTransactions DecodedTransaction list
     */
    public void showTotalValueInPLNOfCryptoPurchaseIn2022(List<DecodedTransaction> decodedTransactions) {

        System.out.println("ZoneId: " + ZoneId.systemDefault());

        List<DecodedTransaction> buyForPlnIn2022Transactions = decodedTransactions.stream()
                .filter(t -> t.getTime().getYear() == 2022)
                .filter(t -> t.getMarket().getRight().equalsIgnoreCase("PLN"))
                .filter(t -> t.getUserAction().equals(UserActionType.BUY))
                .toList();

        System.out.println("buyForPlnIn2022Transactions:");
        buyForPlnIn2022Transactions.forEach(t -> {
            System.out.println(t);
            System.out.println("Transaction value: " + t.getAmount().multiply(t.getRate()));
        });

        BigDecimal totalValuePln = buyForPlnIn2022Transactions.stream()
                .map(t -> t.getAmount().multiply(t.getRate())).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("TOTAL for filtered (PLN, BUY, 2022): " + totalValuePln);
    }
}
