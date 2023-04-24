package pl.balwinski.model.history;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
@Builder
public class DecodedTransaction implements Comparable<DecodedTransaction> {

    private final static ZoneId ZONE_ID = ZoneId.systemDefault();

    @ToString.Exclude
    private String id;
    private MarketHistory market;
    private LocalDateTime time;
    private BigDecimal amount;
    private BigDecimal rate;
    private UserActionType initializedBy;
    private boolean wasTaker;
    private UserActionType userAction;
    @ToString.Exclude
    private String offerId;
    private BigDecimal commissionValue;

    public static DecodedTransaction decode(Transaction t) {
        return DecodedTransaction.builder()
                .id(t.getId())
                .market(new MarketHistory(t.getMarket()))
                .time(
                        LocalDateTime.ofInstant(
                                Instant.ofEpochMilli(Long.parseLong(t.getTime())),
                                ZONE_ID)
                )
                .amount(new BigDecimal(t.getAmount()))
                .rate(new BigDecimal(t.getRate()))
                .initializedBy(UserActionType.parse(t.getInitializedBy()))
                .wasTaker(t.isWasTaker())
                .userAction(UserActionType.parse(t.getUserAction()))
                .offerId(t.getOfferId())
                .commissionValue(new BigDecimal(t.getCommissionValue()))
                .build();
    }

    @Override
    public int compareTo(@NotNull DecodedTransaction o) {
        return this.time.compareTo(o.getTime());
    }
}
