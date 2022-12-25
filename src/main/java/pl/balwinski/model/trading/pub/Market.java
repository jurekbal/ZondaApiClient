package pl.balwinski.model.trading.pub;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Market {
    private String code;
    private MarketPairMember first;
    private MarketPairMember second;
    private int amountPrecision;
    private int pricePrecision;
    private int ratePrecision;
}
