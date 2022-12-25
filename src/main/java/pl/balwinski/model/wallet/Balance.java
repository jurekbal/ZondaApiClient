package pl.balwinski.model.wallet;

import lombok.Data;

@Data
public class Balance {
    private String id;
    private String userId;
    //TODO parse these decimals to selected numeric values
    private String availableFunds; //decimal
    private String totalFunds; //decimal
    private String lockedFunds; //decimal
    private String currency;
    private String type;
    private String name;
    private String balanceEngine;
}
