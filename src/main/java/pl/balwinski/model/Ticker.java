package pl.balwinski.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticker {

    private Market market;
    private long time;
    private String highestBid;
    private String lowestAsk;
    private String rate;
    private String previousRate;
}
