package pl.balwinski.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MarketPairMember {

    private String currency;
    private String minOffer;
    private String scale;
}
