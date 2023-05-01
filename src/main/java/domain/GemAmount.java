package domain;

public class GemAmount {
    private final Crystal type;
    private int amount;

    public GemAmount(Crystal type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public String showGems(){
        return String.format("[%s: %d]", type, amount);
    }

    public Crystal getType() { return type; }

    public int getAmount() { return amount; }

    public void subtractOne(){
        amount--;
    }

    public void addOne(){
        amount++;
    }

    public void subtractTwo(){
        amount -= 2;
    }

    public void addTwo(){
        amount += 2;
    }
}