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
import java.util.Set;
import java.util.stream.Collectors;

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
        //given
        List<Balance> localBalances = loadBalancesFromResourcesCsv("balances-local.csv");
        List<Balance> apiBalances = loadBalancesFromResourcesCsv("balances-api-new.csv");

        // basic check of test data
        assertThat(apiBalances.size()).isGreaterThan(localBalances.size());
        assertThat(apiBalances.isEmpty()).isFalse();

        //when
        BalancesChecker balancesChecker = new BalancesChecker();
        BalancesCheckResult result = balancesChecker.check(localBalances, apiBalances);

        //then
        assertThat(result.getStatus()).isEqualByComparingTo(BalanceCheckStatus.OK);
        assertThat(result.getNewBalancesCopy().size()).isEqualTo(5);
        assertThat(result.getOrphanBalancesCopy().isEmpty()).isTrue();

    }

    // case 3 orphan balances on local list
    @Test
    void checkOrphanBalancesOnLocalList() {
        //given
        List<Balance> localBalances = loadBalancesFromResourcesCsv("balances-local.csv");
        List<Balance> apiBalances = loadBalancesFromResourcesCsv("balances-api-lack2.csv");

        // basic check of test data
        assertThat(apiBalances.size()).isLessThan(localBalances.size());
        assertThat(apiBalances.isEmpty()).isFalse();

        //when
        BalancesChecker balancesChecker = new BalancesChecker();
        BalancesCheckResult result = balancesChecker.check(localBalances, apiBalances);

        //then
        assertThat(result.getStatus()).isEqualByComparingTo(BalanceCheckStatus.WARNING);
        assertThat(result.getOrphanBalancesCopy().size()).isEqualTo(2);
        assertThat(result.getNewBalancesCopy().isEmpty()).isTrue();
    }

    // case 4 duplicated entries on any of the list
    @Test
    void noDuplicatesOnAnyList() {
        //given
        List<Balance> apiBalances = loadBalancesFromResourcesCsv("balances-api-2xduplicates.csv");
        List<Balance> localBalances = loadBalancesFromResourcesCsv("balances-local-duplicates.csv");

        // basic check of test data
        assertThat(localBalances.isEmpty()).isFalse();
        assertThat(apiBalances.isEmpty()).isFalse();

        //when
        BalancesChecker balancesChecker = new BalancesChecker();
        BalancesCheckResult result = balancesChecker.check(localBalances, apiBalances);

        //then
        assertThat(result.getStatus()).isEqualByComparingTo(BalanceCheckStatus.ERROR);
        assertThat(result.getMessagesCopy()).contains("ERROR: duplicated currency found in apiBalances list: SOL");
        assertThat(result.getMessagesCopy()).contains("ERROR: duplicated currency found in apiBalances list: SHIB");
        assertThat(result.getMessagesCopy()).contains("ERROR: duplicated currency found in localBalances list: ATRI");
        result.getMessagesCopy().forEach(System.out::println);
    }

    // case 5 immutable values mismatch between local and api. We expect that ID, BALANCEENGINE, NAME, TYPE, USERID are the same
    @Test
    void immutableValuesAreTheSameOnBothLists() {
        //give
        List<Balance> localBalances = loadBalancesFromResourcesCsv("balances-local.csv");
        List<Balance> apiBalances = loadBalancesFromResourcesCsv("balances-api-different-immutables.csv");

        // basic check of test data
        assertThat(localBalances.size()).isEqualTo(apiBalances.size());
        assertThat(localBalances.isEmpty()).isFalse();
        assertThat(apiBalances.isEmpty()).isFalse();

        //when
        BalancesChecker balancesChecker = new BalancesChecker();
        BalancesCheckResult result = balancesChecker.check(localBalances, apiBalances);

        //then
        assertThat(result.getStatus()).isEqualByComparingTo(BalanceCheckStatus.ERROR);
        assertThat(result.getMessagesCopy()).contains("""
                ERROR: Balance BALANCEENGINE mismatch:
                Local balance entry:Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8466, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0E-8, totalFunds=0E-8, lockedFunds=0E-8, currency=BCC, type=CRYPTO, name=BCC, balanceEngine=BITBAY)
                API Balance entry:Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8466, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0E-8, totalFunds=0E-8, lockedFunds=0E-8, currency=BCC, type=CRYPTO, name=BCC, balanceEngine=KRAKEN)""");
        assertThat(result.getMessagesCopy()).contains("""
                ERROR: Balance ID mismatch:
                Local balance entry:Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8467, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0.08521334, totalFunds=0.08521334, lockedFunds=0E-8, currency=MKR, type=CRYPTO, name=MKR, balanceEngine=BITBAY)
                API Balance entry:Balance(id=8e9e9be4-dbbf-41e6-97fb-c00c64bd8467, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0.08521334, totalFunds=0.08521334, lockedFunds=0E-8, currency=MKR, type=CRYPTO, name=MKR, balanceEngine=BITBAY)""");
        assertThat(result.getMessagesCopy()).contains("""
                ERROR: Balance NAME mismatch:
                Local balance entry:Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8468, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0E-8, totalFunds=0E-8, lockedFunds=0E-8, currency=PSG, type=CRYPTO, name=PSG, balanceEngine=BITBAY)
                API Balance entry  :Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8468, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0E-8, totalFunds=0E-8, lockedFunds=0E-8, currency=PSG, type=CRYPTO, name=ASG, balanceEngine=BITBAY)""");
        assertThat(result.getMessagesCopy()).contains("""
                ERROR: Balance TYPE mismatch:
                Local balance entry:Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8469, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0E-8, totalFunds=0E-8, lockedFunds=0E-8, currency=ATRI, type=CRYPTO, name=ATRI, balanceEngine=BITBAY)
                API Balance entry:Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8469, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0E-8, totalFunds=0E-8, lockedFunds=0E-8, currency=ATRI, type=FIAT, name=ATRI, balanceEngine=BITBAY)""");
        assertThat(result.getMessagesCopy()).contains("""
                ERROR: Balance USERID mismatch:
                Local balance entry:Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8460, userId=4e9e9be4-aaaa-4444-7-c00c64bd8471, availableFunds=0E-8, totalFunds=0E-8, lockedFunds=0E-8, currency=DOGE, type=CRYPTO, name=DOGE, balanceEngine=BITBAY)
                API Balance entry:Balance(id=4e9e9be4-dbbf-41e6-97fb-c00c64bd8460, userId=eeeeeeee-aaaa-4444-7-c00c64bd8471, availableFunds=0E-8, totalFunds=0E-8, lockedFunds=0E-8, currency=DOGE, type=CRYPTO, name=DOGE, balanceEngine=BITBAY)""");
    }

    // case 6 mutable values changed (add to checker result)
    @Test
    void resultContainsMutableValuesThatDiffers() {
        //give
        List<Balance> localBalances = loadBalancesFromResourcesCsv("balances-local.csv");
        List<Balance> apiBalances = loadBalancesFromResourcesCsv("balances-api-chg-founds.csv");

        // basic check of test data
        assertThat(localBalances.size()).isEqualTo(apiBalances.size());
        assertThat(localBalances.isEmpty()).isFalse();
        assertThat(apiBalances.isEmpty()).isFalse();

        //when
        BalancesChecker balancesChecker = new BalancesChecker();
        BalancesCheckResult result = balancesChecker.check(localBalances, apiBalances);

        //then
        assertThat(result.getStatus()).isEqualByComparingTo(BalanceCheckStatus.OK);
        List<Balance> changed = result.getChangedBalancesCopy();
        assertThat(changed.size()).isEqualTo(3);
        Set<String> changedCurrencies = changed.stream().map(Balance::getCurrency).collect(Collectors.toSet());
        assertThat(changed).flatExtracting(Balance::getCurrency)
                .containsExactlyInAnyOrderElementsOf(Set.of("SOL", "COMP", "DOGE"));
    }

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