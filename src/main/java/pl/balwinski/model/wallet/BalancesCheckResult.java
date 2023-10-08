package pl.balwinski.model.wallet;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BalancesCheckResult {

    private final List<Balance> newBalances;
    private final List<Balance> orphanBalances;

    private final List<String> messages;

    private BalanceCheckStatus status;

    public BalancesCheckResult() {
        newBalances = new ArrayList<>();
        orphanBalances = new ArrayList<>();
        messages = new ArrayList<>();
        status = BalanceCheckStatus.NOT_SET;
    }

    public List<Balance> getNewBalancesCopy() {
        return new ArrayList<>(newBalances);
    }

    public List<Balance> getOrphanBalancesCopy() {
        return new ArrayList<>(orphanBalances);
    }

    public List<String> getMessagesCopy() {
        return new ArrayList<>(messages);
    }

    public @NotNull BalanceCheckStatus getStatus() {
        if (status == null) return BalanceCheckStatus.NOT_SET;
        return status;
    }

    public void setStatusOK() {
        if (status == null || status.ordinal() < BalanceCheckStatus.OK.ordinal()) {
            this.status = BalanceCheckStatus.OK;
        }
    }

    public void addAllNewBalances(List<Balance> balances) {
        this.newBalances.addAll(balances);
    }

    public void addAllOrphanBalances(List<Balance> balances) {
        this.orphanBalances.addAll(balances);
    }

    public void setInfoMessage(String message) {
        this.messages.add("INFO: " + message);
    }

    public void setWarning(String message) {
        elevateStatusToWarning();
        this.messages.add("WARN: " + message);
    }

    public void setError(String message) {
        elevateStatusToError();
        this.messages.add("ERROR: " + message);
    }

    private void elevateStatusToWarning() {
        if (status.ordinal() < BalanceCheckStatus.WARNING.ordinal()) {
            this.status = BalanceCheckStatus.WARNING;
        }
    }

    private void elevateStatusToError() {
        if (status.ordinal() < BalanceCheckStatus.ERROR.ordinal()) {
            this.status = BalanceCheckStatus.ERROR;
        }
    }

}
