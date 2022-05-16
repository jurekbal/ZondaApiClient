package pl.balwinski.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Ticker {

    private Market market;
    private String time;

    private String rate;
}
