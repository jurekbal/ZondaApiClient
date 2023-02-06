package pl.balwinski.model.history;

import lombok.Data;

import java.util.List;

@Data
public class TransactionsResponse {
    private String status;
    private String totalRows;
    private String errors;
    private ReturnedTransactionsQuery query;
    private String nextPageCursor;
    private List<Transaction> items;

}
