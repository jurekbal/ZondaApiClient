package pl.balwinski.model.history;

import lombok.Data;

@Data
public class TransactionsQuery {

    private String nextPageCursor;
    private String fromTime;
    private String toTime;
}
