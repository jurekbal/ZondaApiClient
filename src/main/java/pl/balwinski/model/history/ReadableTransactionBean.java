package pl.balwinski.model.history;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Przygotowuje csv gotowe do wczytania do excela z czytelną przez człowieka datą
 */
@Data
public class ReadableTransactionBean{

    @CsvBindByName
    private String id;
    @CsvBindByName
    private String market;
    @CsvBindByName(column = "aa_time")
    @CsvDate(writeFormatEqualsReadFormat = false ,writeFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    @CsvBindByName()
    private BigDecimal amount;
    @CsvBindByName()
    private BigDecimal rate;
    @CsvBindByName
    private String initializedBy;
    @CsvBindByName
    private String wasTaker;
    @CsvBindByName
    private String userAction;
    @CsvBindByName
    private String offerId;
    @CsvBindByName()
    private BigDecimal commissionValue;

    public static ReadableTransactionBean fromDecodedTransaction(DecodedTransaction transaction) {
        ReadableTransactionBean to = new ReadableTransactionBean();
        to.setId(transaction.getId());
        to.setMarket(transaction.getMarket().toString());
        to.setTime(transaction.getTime());
        to.setAmount(transaction.getAmount());
        to.setRate(transaction.getRate());
        to.setInitializedBy(transaction.getInitializedBy().name());
        to.setWasTaker(transaction.isWasTaker() ? "Taker" : "Maker");
        to.setUserAction(transaction.getUserAction().name());
        to.setOfferId(transaction.getOfferId());
        to.setCommissionValue(transaction.getCommissionValue());

        return to;
    }

}
