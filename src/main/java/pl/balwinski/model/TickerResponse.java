package pl.balwinski.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TickerResponse {

    private String status;
    private Ticker ticker;
}
