package pl.balwinski.model.wallet;

import lombok.Data;

import java.util.List;

@Data
public class BalanceResponse {
    private String status;
    private List<Balance> balances;
    private String errors; //TODO check returned type

}
