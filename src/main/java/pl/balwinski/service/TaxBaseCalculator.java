package pl.balwinski.service;

import pl.balwinski.model.history.DecodedTransaction;
import pl.balwinski.model.history.UserActionType;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;

public class TaxBaseCalculator {

    /** Calculates and shows BUY transactions and total value
     * Hardcoded filter for PLN, BUY action in given year
     * @param decodedTransactions DecodedTransaction list
     * @param year int - year to consider tax affected transactions
     */
    public void showTotalValueInPlnOfCryptoPurchaseInYear(List<DecodedTransaction> decodedTransactions, int year) {

        System.out.println("ZoneId: " + ZoneId.systemDefault());

        System.out.printf("Total Tax effective transactions: %d",
                countTaxEffectiveTransactionsInYear(decodedTransactions, year));

        List<DecodedTransaction> buyForPlnInGivenYearTransactions = decodedTransactions.stream()
                .filter(t -> t.getTime().getYear() == year)
                .filter(t -> t.getMarket().getRight().equalsIgnoreCase("PLN"))
                .filter(t -> t.getUserAction().equals(UserActionType.BUY))
                .toList();

        System.out.println("buyForPlnInGivenYearTransactions: " + year);
        buyForPlnInGivenYearTransactions.forEach(t -> {
            System.out.println(t);
            System.out.println("Transaction value: " + t.getAmount().multiply(t.getRate()));
        });

        BigDecimal totalValuePln = buyForPlnInGivenYearTransactions.stream()
                .map(t -> t.getAmount().multiply(t.getRate())).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.printf("TOTAL for filtered (PLN, BUY, %d): %s/n", year,totalValuePln);
    }

    private long countTaxEffectiveTransactionsInYear(List<DecodedTransaction> decodedTransactions, int year) {
        return decodedTransactions.stream()
                .filter(t -> t.getTime().getYear() == year)
                .filter(t ->
                        t.getMarket().getRight().equalsIgnoreCase("PLN") ||
                        t.getMarket().getRight().equalsIgnoreCase("USD") ||
                        t.getMarket().getRight().equalsIgnoreCase("EUR") ||
                        t.getMarket().getRight().equalsIgnoreCase("GBP"))
                .filter(t ->
                        t.getUserAction().equals(UserActionType.BUY) ||
                        t.getUserAction().equals(UserActionType.SELL))
                .count();
    }
}
