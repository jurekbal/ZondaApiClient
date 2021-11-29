package pl.balwinski.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticker {

    private pl.neptis.yanosik24server.model.etolltest.Market market;
    private String time;

    private String rate;
}
