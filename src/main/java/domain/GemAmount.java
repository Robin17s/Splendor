package domain;

public class GemAmount {
    private final Crystal type;
    private final byte amount;

    public GemAmount(Crystal type, byte amount) {
        this.type = type;
        this.amount = amount;
    }

    public Crystal getType() { return type; }

    public byte getAmount() { return amount; }
}