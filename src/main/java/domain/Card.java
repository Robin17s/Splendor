package domain;

public abstract class Card {
    private final int prestige;

    public Card(int prestige) { this.prestige = prestige; }

    public int getPrestige() { return prestige; }
}