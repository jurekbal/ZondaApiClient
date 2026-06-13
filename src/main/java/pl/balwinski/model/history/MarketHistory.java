package pl.balwinski.model.history;

import lombok.Getter;

@Getter
public class MarketHistory {
    private final String left;
    private final String right;

    public MarketHistory(String marketString) {
        //TODO check null, format
        String[] pair = marketString.split("-");
        this.left = pair[0];
        this.right = pair[1];
    }

    @Override
    public String toString() {
        return left + '-' +right;
    }
}
