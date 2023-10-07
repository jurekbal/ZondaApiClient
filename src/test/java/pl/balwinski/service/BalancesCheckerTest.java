package pl.balwinski.service;

import org.junit.jupiter.api.Test;
import pl.balwinski.model.wallet.Balance;
import pl.balwinski.model.wallet.BalanceCheckStatus;
import pl.balwinski.model.wallet.BalancesCheckResult;

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
        BalancesCheckResult bothNotNull = checker.check(new ArrayList<>(), new ArrayList<>());

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

    // TODO rest of testcases
    // CASE 1 Both lists The same
    // CASE 2 New balances on api list
    // case 3 orphan balances on local list
    // case 4 duplicated entries on lists
    // case 5 immutable values mismatch between local and api
    // case 6 mutable values changed (add to checker result)

}