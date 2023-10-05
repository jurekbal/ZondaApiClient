package pl.balwinski.service;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BalancesCheckerTest {

    @Test
    void checkBalancesFailsForNullArguments() {
        //when
        boolean bothListsNull = BalancesChecker.checkBalances(null, null);
        boolean localListIsNull = BalancesChecker.checkBalances(null, new ArrayList<>());
        boolean apiListIsNull = BalancesChecker.checkBalances(new ArrayList<>(), null);
        boolean bothNotNull = BalancesChecker.checkBalances(new ArrayList<>(), new ArrayList<>());

        //then
        assertFalse(bothListsNull);
        assertFalse(localListIsNull);
        assertFalse(apiListIsNull);
        assertTrue(bothNotNull);
    }

    //TODO rest of testcases

}