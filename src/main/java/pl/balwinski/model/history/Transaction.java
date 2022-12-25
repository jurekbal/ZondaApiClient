package pl.balwinski.model.history;

import lombok.Data;

@Data
public class Transaction {

    private String id;
    private String market;
    private String time;
    private String amount;
    private String rate;
    private String initializedBy;
    private boolean wasTaker;
    private String userAction;
    private String offerId;
    private String commissionValue;
}
