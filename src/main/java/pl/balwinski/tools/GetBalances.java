package pl.balwinski.tools;

import pl.balwinski.model.wallet.Balance;
import pl.balwinski.model.wallet.BalanceResponse;
import pl.balwinski.service.ZondaService;

import java.io.IOException;
import java.util.List;

public class GetBalances {

    public static void main(String[] args) {
        ZondaService service = new ZondaService();

        try {
            BalanceResponse balanceResponse = service.getBalanceResponse();
            List<Balance> allBalances = balanceResponse.getBalances();

            List<Balance> nonZeroBalances = filterOutZeroBalances(balanceResponse.getBalances());
            System.out.println("Total balances: " + allBalances.size());
            System.out.println("Non zero balances: " + nonZeroBalances.size());

            printOutFiatTickersAndFounds(nonZeroBalances);
            printOutCryptoTickersAndFounds(nonZeroBalances);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static List<Balance> filterOutZeroBalances(List<Balance> balances) {
        return balances.stream()
                .filter(b -> !(b.getAvailableFunds().equals("0E-8") &&
                        b.getTotalFunds().equals("0E-8") &&
                        b.getLockedFunds().equals("0E-8")))
                .toList();
    }
    static void printOutFiatTickersAndFounds(List<Balance> balances) {
        List<Balance> fiatTickers = balances.stream()
                .filter(b -> b.getType().equals("FIAT"))
                .toList();
        System.out.println("Fiat currencies count:" + fiatTickers.size());
        fiatTickers.forEach(s -> System.out.printf("%s : %s\n", s.getCurrency(), s.getAvailableFunds()));
    }

    static void printOutCryptoTickersAndFounds(List<Balance> balances) {
        List<Balance> fiatTickers = balances.stream()
                .filter(b -> b.getType().equals("CRYPTO"))
                .toList();
        System.out.println("Crypto currencies count:" + fiatTickers.size());
        fiatTickers.forEach(s -> System.out.printf("%s : %s\n", s.getCurrency(), s.getAvailableFunds()));
    }

}