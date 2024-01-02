package pl.balwinski.model.history;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsQuery {

    private String nextPageCursor;
    private String fromTime;
    private String toTime;

    public TransactionsQuery getCopy() {
        return new TransactionsQuery(this.nextPageCursor, this.fromTime, this.toTime);
    }
}
