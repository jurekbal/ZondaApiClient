package pl.balwinski.model.history;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

@Data
public class TransactionsFinalResult {
    private String status;
    private String totalRows;
    private String errors;
    private List<Transaction> items;

    public TransactionsFinalResult() {
        this.items = new LinkedList<>();
    }
}
