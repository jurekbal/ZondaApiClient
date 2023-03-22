package pl.balwinski.model.history;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.TimeZone;

@Data
@Builder
public class DecodedTransaction implements Comparable<DecodedTransaction> {

    //TODO
    // market -> MarketA, MarketB, type Enum (CRYPTO, FIAT, ?); may use Market object
    // time -> decode to LocalDateTime
    // amount, rate -> decode to BigDecimal ?
    // initializedBy, wasTaker, -> ignore now
    // userAction -> decode into enum
    // commission -> may decode into BigDecimal
    // Allow configuration of local TimeZone

    private final static ZoneId ZONE_ID = ZoneId.systemDefault();

    private String id;
    private String market;
    private LocalDateTime time;
    private String amount;
    private String rate;
    private String initializedBy;
    private boolean wasTaker;
    private String userAction;
    private String offerId;
    private String commissionValue;

    public static DecodedTransaction decode(Transaction t) {
        return DecodedTransaction.builder()
                .id(t.getId())
                .market(t.getMarket())
                .time(
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(Long.parseLong(t.getTime())),
                                ZONE_ID)
                )
                .amount(t.getAmount())
                .rate(t.getRate())
                .initializedBy(t.getInitializedBy())
                .wasTaker(t.isWasTaker())
                .userAction(t.getUserAction())
                .offerId(t.getOfferId())
                .commissionValue(t.getCommissionValue())
                .build();
    }

    @Override
    public int compareTo(@NotNull DecodedTransaction o) {
        return this.time.compareTo(o.getTime());
    }
}
