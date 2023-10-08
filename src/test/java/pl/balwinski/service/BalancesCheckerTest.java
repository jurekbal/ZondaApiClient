package pl.balwinski.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.Test;
import pl.balwinski.model.wallet.Balance;
import pl.balwinski.model.wallet.BalanceCheckStatus;
import pl.balwinski.model.wallet.BalancesCheckResult;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class BalancesCheckerTest {

    @Test
    void checkBalancesFailsForNullArguments() {
        //given
        BalancesChecker checker = new BalancesChecker();

        //when
        BalancesCheckResult bothArgsNull = checker.check(null, null);
        BalancesCheckResult localListNull = checker.check(null, new ArrayList<>());
        BalancesCheckResult apiListIsNull = checker.check(new ArrayList<>(), null);

        //then
        assertThat(bothArgsNull.getStatus()).as("Both arguments null must give ERROR status")
                .isEqualByComparingTo(BalanceCheckStatus.ERROR);
        System.out.println(bothArgsNull.getMessagesCopy());

        assertThat(localListNull.getStatus()).as("local list argument null must give ERROR status")
                .isEqualByComparingTo(BalanceCheckStatus.ERROR);
        System.out.println(localListNull.getMessagesCopy());

        assertThat(apiListIsNull.getStatus()).as("API list argument null must give ERROR status")
                .isEqualByComparingTo(BalanceCheckStatus.ERROR);
        System.out.println(apiListIsNull.getMessagesCopy());

    }



    @Test
    void checkWarningsForEmptyLists() {
        //given
        BalancesChecker checker = new BalancesChecker();
        Balance balance = new Balance();
        balance.setCurrency("BAT");
        balance.setId("34a53641-34bc-4487-8d6d-0da999f520ef");
        balance.setName("BAT");
        balance.setType("CRYPTO");
        balance.setUserId("34a53641-34bc-4487-8d6d-0da999f520ef");
        balance.setBalanceEngine("BITBAY");
        balance.setAvailableFunds("0E-8");
        balance.setLockedFunds("0E-8");
        balance.setTotalFunds("0E-8");

        //when
        BalancesCheckResult bothEmpty = checker.check(new ArrayList<>(), new ArrayList<>());
        BalancesCheckResult localEmpty = checker.check(new ArrayList<>(), new ArrayList<>(List.of(balance)));
        BalancesCheckResult apiEmpty = checker.check(new ArrayList<>(List.of(balance)), new ArrayList<>());

        //then
            assertThat(bothEmpty.getStatus()).as("Empty lists as arguments must give WARN status")
                    .isEqualByComparingTo(BalanceCheckStatus.WARNING);
            assertThat(localEmpty.getStatus()).as("Empty local balance list argument must give WARN status")
                .isEqualByComparingTo(BalanceCheckStatus.WARNING);
            assertThat(apiEmpty.getStatus()).as("Empty api balance list argument must give WARN status")
                .isEqualByComparingTo(BalanceCheckStatus.WARNING);
    }

    // CASE 1 Both lists The same
    @Test
    void checkBothListsTheSame() {
        //give
        List<Balance> localBalances = loadBalancesFromResourcesCsv("balances-local.csv");
        List<Balance> apiBalances = loadBalancesFromResourcesCsv("balances-local-copy.csv");

        // basic check of test data
        assertThat(localBalances.size()).isEqualTo(apiBalances.size());
        assertThat(localBalances.isEmpty()).isFalse();
        assertThat(apiBalances.isEmpty()).isFalse();

        //when
        BalancesChecker balancesChecker = new BalancesChecker();
        BalancesCheckResult result = balancesChecker.check(localBalances, apiBalances);

        //then
        assertThat(result.getStatus()).isEqualByComparingTo(BalanceCheckStatus.OK);
        assertThat(result.getNewBalancesCopy().isEmpty()).isTrue();
        assertThat(result.getOrphanBalancesCopy().isEmpty()).isTrue();
    }

    // CASE 2 New balances on api list
    @Test
    void checkNewBalancesOnApiList() {
        //give
        List<Balance> localBalances = loadBalancesFromResourcesCsv("balances-local.csv");
        List<Balance> apiBalances = loadBalancesFromResourcesCsv("balances-api-new.csv");

        // basic check of test data
        assertThat(apiBalances.size()).isGreaterThan(localBalances.size());
        assertThat(localBalances.isEmpty()).isFalse();
        assertThat(apiBalances.isEmpty()).isFalse();

        //when
        BalancesChecker balancesChecker = new BalancesChecker();
        BalancesCheckResult result = balancesChecker.check(localBalances, apiBalances);

        //then
        System.out.println(result.getMessagesCopy());
        assertThat(result.getStatus()).isEqualByComparingTo(BalanceCheckStatus.OK);
        assertThat(result.getNewBalancesCopy().size()).isEqualTo(5);
        assertThat(result.getOrphanBalancesCopy().isEmpty()).isTrue();

    }
    //TODO
    // case 3 orphan balances on local list
    // case 4 duplicated entries on any of the list
    // case 5 immutable values mismatch between local and api
    // case 6 mutable values changed (add to checker result)

    private List<Balance> loadBalancesFromResourcesCsv(String fileName) {
        File file = new File(getClass().getClassLoader().getResource(fileName).getFile());
        try (Reader reader = new FileReader(file)) {
            CsvToBean<Balance> csvToBean = new CsvToBeanBuilder<Balance>(reader)
                    .withType(Balance.class)
                    .withOrderedResults(false)
                    .build();
            return csvToBean.parse();
        } catch (IOException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

}