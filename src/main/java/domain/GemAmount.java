package domain;

/**
 * A Tuple of a Crystal, with an amount.
 */
public class GemAmount {
    private final Crystal type;
    private int amount;

    /**
     * Instantiates a new GemAmount.
     * @param type The Crystal type
     * @param amount The amount of said crystal
     */
    public GemAmount(Crystal type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * @return The Crystal type.
     */
    public Crystal getType() { return type; }

    /**
     * @return The amount.
     */
    public int getAmount() { return amount; }

    /**
     * Subtracts 1 from the amount.
     */
    public void subtractOne(){
        amount--;
    }

    /**
     * Subtracts 2 from the amount.
     */
    public void subtractTwo(){
        amount -= 2;
    }

    public GemAmountDTO toDTO() { return new GemAmountDTO(this.type, this.amount); }

    public record GemAmountDTO(Crystal type, int amount) {}
}