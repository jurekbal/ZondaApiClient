package pl.balwinski.model.history;

public enum UserActionType {

    BUY("Buy"),
    SELL("Sell");

    private final String value;

    UserActionType(String value) {
        this.value = value;
    }

    public static UserActionType parse(String value) {
        for (UserActionType userActionType : UserActionType.values()) {
            if (userActionType.getValue().equals(value)) {
                return userActionType;
            }
        }
        throw new IllegalArgumentException("Value " + value + " is not a valid User Action or initialisedBy string");
    }

    public String getValue() {
        return this.value;
    }
}
