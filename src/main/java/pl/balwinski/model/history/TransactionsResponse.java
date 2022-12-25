package pl.balwinski.model.history;

import lombok.Data;

import java.util.List;

@Data
public class TransactionsResponse {
    private String status;
    private String totalRows;
    private List<Transaction> items;
    private String errors; //TODO check returned type

}
