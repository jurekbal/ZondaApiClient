package pl.balwinski.model.history;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MarketHistory {
    private final String left;
    private final String right;

    public MarketHistory(String marketString) {
        //TODO check null, format
        String[] pair = marketString.split("-");
        this.left = pair[0];
        this.right = pair[1];
    }
}
