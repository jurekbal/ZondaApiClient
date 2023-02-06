package pl.balwinski.model.history;

import lombok.Data;

@Data
public class ReturnedTransactionsQuery {

    private String[][] markets;
    private double[] rateFrom;
    private double[] rateTo;
    private Long[] fromTime;
    private Long[] toTime;
    private String[] userAction;
    private String[] nextPageCursor;
}
