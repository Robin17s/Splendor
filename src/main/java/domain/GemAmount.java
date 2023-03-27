package domain;

public class GemAmount {
    private final Crystal type;
    private final int amount;

    public GemAmount(Crystal type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public Crystal getType() { return type; }

    public int getAmount() { return amount; }
}